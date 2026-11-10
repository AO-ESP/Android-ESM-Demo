package ru.esm.tspiot.data

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.esm.tspiot.data.mapper.mapToESMModel
import ru.esm.tspiot.data.mapper.toJsonString
import ru.esm.tspiot.data.models.CashierInfoModel
import ru.esm.tspiot.data.models.ErrorRequestModel
import ru.esm.tspiot.data.models.ImcData
import ru.esm.tspiot.data.models.IsmNoticeInfoModel
import ru.esm.tspiot.data.models.KktInfoModel
import ru.esm.tspiot.data.models.ReceiptInfoModel
import ru.esm.tspiot.domain.PiotResult
import ru.esm.tspiot.driver.api.IPiotManager
import ru.esm.tspiot.driver.api.callback.IBoolCallback
import ru.esm.tspiot.driver.api.callback.IBundleResultCallback
import ru.esm.tspiot.driver.api.callback.IResultCallback
import ru.esm.tspiot.driver.api.model.ean.EanCheckRequest
import ru.esm.tspiot.driver.api.model.ean.EanCheckResponse
import javax.inject.Inject
import kotlin.coroutines.resume

class PiotESMManagerClientImpl @Inject constructor(
    @param:ApplicationContext val context: Context
) : PiotManagerClient {

    var iPiotManager: IPiotManager? = null
    override val lock = Any()
    override val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    override val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            synchronized(lock) {
                iPiotManager = IPiotManager.Stub.asInterface(service)
                _isConnected.value = true
                Log.d(TAG, "IPiotManager Connected ✓")
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            synchronized(lock) {
                iPiotManager = null
                _isConnected.value = false
                Log.d(TAG, "IPiotManager Disconnected ✗")
            }
        }
    }

    /**
     * Подключается к сервису. Игнорирует повторные вызовы.
     */
    override fun connect(intent: Intent) {
        synchronized(lock) {
            if (iPiotManager != null) return
        }
        try {
            val result = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            Log.d(TAG, "IPiotManager bindService result - $result")
        } catch (e: Exception) {
            Log.d(TAG, e.message ?: e.stackTraceToString())
        }
    }

    /**
     * Отключается от сервиса. Безопасно вызывать при любом состоянии.
     */
    override fun disconnect() {
        synchronized(lock) {
            iPiotManager?.let {
                context.unbindService(serviceConnection)
                iPiotManager = null
                _isConnected.value = false
            }
        }
    }

    override suspend fun setShiftState(isClosed: Boolean, kktInfo: KktInfoModel) {
        executeCallback { m, cb -> m.setShiftState(cb, isClosed, kktInfo.mapToESMModel()) }
    }


    override suspend fun setImcData(imcData: ImcData, kktInfo: KktInfoModel, isOnline: Boolean) =
        executeUnitCallback { m, cb ->
            m.setImcData(
                cb,
                imcData.toJsonString(),
                kktInfo.mapToESMModel(),
                isOnline
            )
        }

    override suspend fun setError(request: ErrorRequestModel, kktInfo: KktInfoModel) =
        executeUnitCallback { m, cb ->
            m.setError(
                cb,
                request.mapToESMModel(),
                kktInfo.mapToESMModel()
            )
        }

    override suspend fun setIsmNotice(info: IsmNoticeInfoModel, kktInfo: KktInfoModel) =
        executeUnitCallback { m, cb ->
            m.setIsmNotice(
                cb,
                info.mapToESMModel(),
                kktInfo.mapToESMModel()
            )
        }

    override suspend fun setRawEvent(event: String) =
        executeUnitCallback { m, cb -> m.setRawEvent(cb, event) }

    override suspend fun setReceiptInfo(info: ReceiptInfoModel, kktInfo: KktInfoModel) =
        executeUnitCallback { m, cb ->
            m.setReceiptInfo(
                cb,
                info.mapToESMModel(),
                kktInfo.mapToESMModel()
            )
        }

    override suspend fun setKktInfo(kktInfo: KktInfoModel) =
        executeUnitCallback { m, cb -> m.setKktInfo(cb, kktInfo.mapToESMModel()) }

    override suspend fun setCashier(cashierInfo: CashierInfoModel, kktInfo: KktInfoModel) =
        executeUnitCallback { m, cb ->
            m.setCashier(
                cb,
                cashierInfo.mapToESMModel(),
                kktInfo.mapToESMModel()
            )
        }

    override suspend fun eanCheck(request: EanCheckRequest): PiotResult<EanCheckResponse> {
        val result = executeBundleCallback { m, cb -> m.eanCheck(cb, request) }
        return when (result) {
            is PiotResult.Success -> parseEanCheckResponse(result.data)
            is PiotResult.Error -> result
            PiotResult.Loading -> PiotResult.Loading
            PiotResult.ServiceUnavailable -> PiotResult.ServiceUnavailable
        }
    }

    /**
     * Универсальный метод для колбэк-ориентированных вызовов.
     */
    suspend fun executeCallback(
        action: (IPiotManager, IBoolCallback) -> Unit
    ) {
        val manager = synchronized(lock) { iPiotManager }
        if (manager == null) return

        return suspendCancellableCoroutine {
            val callback = object : IBoolCallback.Stub() {
                override fun onSuccess(status: Boolean) {
                    Log.i("Logcat", "executeCallback Success $status")
                }

                override fun onFailure(code: Int, message: String?) {
                    Log.e("Logcat", "executeCallback code $code, message $message")
                }
            }

            try {
                action(manager, callback)
            } catch (e: RemoteException) {
                Log.e("Logcat", "executeCallback catch, message ${e.message}")
            }
        }
    }

    suspend fun executeUnitCallback(
        action: (IPiotManager, IResultCallback) -> Unit
    ) {
        val manager = synchronized(lock) { iPiotManager }
        if (manager == null) return

        return suspendCancellableCoroutine {
            val callback = object : IResultCallback.Stub() {
                override fun onSuccess() {
                    Log.i("Logcat", "executeUnitCallback Success")
                }

                override fun onFailure(code: Int, message: String?) {
                    Log.e("Logcat", "executeUnitCallback code $code, message $message")
                }
            }

            try {
                action(manager, callback)
            } catch (e: RemoteException) {
                Log.e("Logcat", "executeCallback catch, message ${e.message}")
            }
        }
    }

    private suspend fun executeBundleCallback(
        action: (IPiotManager, IBundleResultCallback) -> Unit
    ): PiotResult<Bundle> {
        val manager = synchronized(lock) { iPiotManager }
        if (manager == null) return PiotResult.ServiceUnavailable

        return suspendCancellableCoroutine { continuation ->
            val callback = object : IBundleResultCallback.Stub() {
                override fun onSuccess(bundle: Bundle) {
                    bundle.classLoader = EanCheckResponse::class.java.classLoader
                    if (continuation.isActive) {
                        continuation.resume(PiotResult.Success(bundle))
                    }
                }

                override fun onError(code: Int, message: String?) {
                    if (continuation.isActive) {
                        continuation.resume(PiotResult.Error(code, message))
                    }
                }
            }

            try {
                action(manager, callback)
            } catch (e: RemoteException) {
                if (continuation.isActive) {
                    continuation.resume(PiotResult.Error(-1, e.message))
                }
            }
        }
    }

    private fun parseEanCheckResponse(bundle: Bundle): PiotResult<EanCheckResponse> {
        bundle.classLoader = EanCheckResponse::class.java.classLoader
        val response = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(BUNDLE_KEY, EanCheckResponse::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(BUNDLE_KEY)
        }
        return if (response != null) {
            PiotResult.Success(response)
        } else {
            PiotResult.Error(-3, "Empty EanCheckResponse")
        }
    }

    companion object {
        private val TAG = PiotESMManagerClientImpl::class.simpleName
        private const val BUNDLE_KEY = "key"
    }
}