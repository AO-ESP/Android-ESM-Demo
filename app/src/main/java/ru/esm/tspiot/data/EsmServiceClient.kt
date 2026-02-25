package ru.esm.tspiot.data

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.esm.tspiot.domain.EsmResult
import ru.esp.esm.api.IBundleResultCallback
import ru.esp.esm.api.IEsmService
import ru.esp.esm.api.model.CodesCheckRequest
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * Клиент для взаимодействия с IEsmService через AIDL.
 * @note Управление lifecycle (connect/disconnect) должно быть реализовано снаружи.
 */
class EsmServiceClient @Inject constructor(private val context: Context) {

    private var esmService: IEsmService? = null
    private val lock = Any()
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            synchronized(lock) {
                esmService = IEsmService.Stub.asInterface(service)
                _isConnected.value = true
                Log.d(TAG, "EsmService Connected V")
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            synchronized(lock) {
                esmService = null
                _isConnected.value = false
                Log.d(TAG, "EsmService Disconnected X")
            }
        }
    }

    /**
     * Подключается к сервису. Игнорирует повторные вызовы.
     */
    fun connect(intent: Intent) {
        synchronized(lock) {
            if (esmService != null) return
        }
        try {
            val result = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            Log.d(TAG, "EsmService bindService result - $result")
        } catch (e: Exception) {
            Log.d(TAG, e.message ?: e.stackTraceToString())
        }
    }

    /**
     * Отключается от сервиса. Безопасно вызывать при любом состоянии.
     */
    fun disconnect() {
        synchronized(lock) {
            esmService?.let {
                context.unbindService(serviceConnection)
                esmService = null
                _isConnected.value = false
            }
        }
    }

    suspend fun codesCheck(codes: CodesCheckRequest) =
        executeCallback<Bundle> { m, cb -> m.codesCheck(cb, codes) }

    /**
     * Проверяет активность сервиса через вызов getAidlVersion().
     */
    suspend fun ping(): Boolean {
        return when (getAidlVersion()) {
            is EsmResult.Success -> true
            else -> false
        }
    }

    /**
     * Получает версию AIDL интерфейса.
     */
    suspend fun getAidlVersion(): EsmResult<Int> {
        val manager = synchronized(lock) { esmService }
        return manager?.runCatching {
            getAidlVersion()
        }?.fold(
            onSuccess = { EsmResult.Success(it) },
            onFailure = { e ->
                if (e is RemoteException) EsmResult.Error(-1, e.message)
                else EsmResult.Error(-2, e.message)
            }
        ) ?: EsmResult.ServiceUnavailable
    }

    suspend fun cisSell(cisList: List<String>) =
        executeCallback<Bundle> { m, cb -> m.cisSell(cb, cisList) }

    suspend fun cisReturn(cisList: List<String>) =
        executeCallback<Bundle> { m, cb -> m.cisReturn(cb, cisList) }

    suspend fun cisSold(skip: Int, limit: Int) =
        executeCallback<Bundle> { m, cb -> m.cisSold(cb, skip, limit) }

    /**
     * Универсальный метод для колбэк-ориентированных вызовов.
     */
    private suspend fun <T> executeCallback(
        action: (IEsmService, IBundleResultCallback) -> Unit
    ): EsmResult<T> {
        val manager = synchronized(lock) { esmService }
        if (manager == null) return EsmResult.ServiceUnavailable

        return suspendCancellableCoroutine { continuation ->
            val callback = object : IBundleResultCallback.Stub() {
                override fun onSuccess(bundle: Bundle) {
                    if (continuation.isActive) {
                        continuation.resume(
                            EsmResult.Success(bundle as T) // Safe: Bundle is T for all methods
                        )
                    }
                }

                override fun onError(code: Int, message: String?) {
                    if (continuation.isActive) {
                        continuation.resume(EsmResult.Error(code, message))
                    }
                }
            }

            try {
                action(manager, callback)
            } catch (e: RemoteException) {
                if (continuation.isActive) {
                    continuation.resume(EsmResult.Error(-1, e.message))
                }
            }
        }
    }

    companion object {
        private val TAG = EsmServiceClient::class.simpleName
    }
}