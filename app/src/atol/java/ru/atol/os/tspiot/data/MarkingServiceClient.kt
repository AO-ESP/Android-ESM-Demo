package ru.atol.os.tspiot.data

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
import ru.atol.os.tspiot.api.IBundleResultCallback
import ru.atol.os.tspiot.api.IMarkingManager
import ru.atol.os.tspiot.api.model.MarkingVerifyRequest
import ru.esm.tspiot.domain.EsmResult
import kotlin.coroutines.resume

/**
 * Клиент для взаимодействия с IMarkingManager через AIDL.
 * @note Управление lifecycle (connect/disconnect) должно быть реализовано снаружи.
 */
class MarkingServiceClient(private val context: Context) {

    private var markingManager: IMarkingManager? = null
    private val lock = Any()
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            synchronized(lock) {
                markingManager = IMarkingManager.Stub.asInterface(service)
                _isConnected.value = true
                Log.d(TAG, "MarkingService Connected V")
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            synchronized(lock) {
                markingManager = null
                _isConnected.value = false
                Log.d(TAG, "MarkingService Disconnected X")
            }
        }
    }

    /**
     * Подключается к сервису. Игнорирует повторные вызовы.
     */
    fun connect(intent: Intent) {
        synchronized(lock) {
            if (markingManager != null) return
        }
        try {
            val result = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            Log.d(TAG, "MarkingService bindService result - $result")
        } catch (e: Exception) {
            Log.d(TAG, e.message ?: e.stackTraceToString())
        }
    }

    /**
     * Отключается от сервиса. Безопасно вызывать при любом состоянии.
     */
    fun disconnect() {
        synchronized(lock) {
            markingManager?.let {
                context.unbindService(serviceConnection)
                markingManager = null
                _isConnected.value = false
            }
        }
    }

    suspend fun requestCheck(info: MarkingVerifyRequest) =
        executeCallback<Bundle> { m, cb -> m.requestCheck(cb, info) }

    private suspend fun <T> executeCallback(
        action: (IMarkingManager, IBundleResultCallback) -> Unit
    ): EsmResult<T> {
        val manager = synchronized(lock) { markingManager }
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
        private const val TAG = "MarkingServiceClient"
    }
}