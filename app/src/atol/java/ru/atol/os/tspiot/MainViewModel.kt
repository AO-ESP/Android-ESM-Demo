package ru.atol.os.tspiot

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ru.atol.os.tspiot.api.IBundleResultCallback
import ru.atol.os.tspiot.api.IMarkingManager
import ru.atol.os.tspiot.api.model.MarkingVerifyRequest
import ru.atol.os.tspiot.api.model.MarkingVerifyResponse
import ru.atol.os.tspiot.ui.MarkingServiceState
import toPrettyString

class MainViewModel : ViewModel() {
    private var isBound: Boolean = false
    private var iMarkingManager: IMarkingManager? = null
    private val _uiState = MutableStateFlow(MarkingServiceState())
    val uiState: StateFlow<MarkingServiceState> = _uiState
    private var serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            iMarkingManager = IMarkingManager.Stub.asInterface(service)
            isBound = true
            Log.d("MarkingManager", "Service connected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            iMarkingManager = null
            isBound = false
            Log.d("MarkingManager", "Service crashed or killed")
        }
    }

    fun connectService(context: Context) {
        if (_uiState.value.isConnected) return

        _uiState.update { it.copy(isConnecting = true, connectionStatus = "Подключение...") }

        try {
            val intent = Intent("ru.atol.os.tspiot.action.ACTION_MARKING_MANAGER").apply {
                setPackage("ru.atol.os.tspiot") // Пакет приложения-источника
            }
            val bound = context.bindService(
                intent,
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )

            if (!bound) {
                _uiState.update { state ->
                    state.copy(
                        isConnecting = false,
                        connectionStatus = "Ошибка подключения",
                        lastError = "Не удалось подключиться к сервису"
                    )
                }
            } else {
                _uiState.update { state ->
                    state.copy(
                        isConnected = true,
                        isConnecting = false,
                        connectionStatus = "Подключено успешно",
                        lastAction = "Сервис подключен"
                    )
                }
            }
        } catch (e: Exception) {
            _uiState.update { state ->
                state.copy(
                    isConnecting = false,
                    connectionStatus = "Ошибка подключения",
                    lastError = "Ошибка: ${e.message}"
                )
            }
        }
    }

    fun disconnectService(context: Context) {
        try {
            context.unbindService(serviceConnection)
            Log.d("MarkingManager", "Service disconnected")
            iMarkingManager = null
            isBound = false
            _uiState.update { state ->
                state.copy(
                    isConnected = false,
                    isConnecting = false,
                    connectionStatus = "Отключено",
                    lastAction = "Сервис отключен",
                    lastResult = "",
                    lastError = "",
                    isValid = null
                )
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun testRequest() {
        val service = iMarkingManager
        if (service == null) {
            _uiState.update { state ->
                state.copy(
                    lastError = "Сервис не подключен",
                    lastAction = "Попытка запроса без подключения"
                )
            }
            return
        }

        try {
            val testRequest = MarkingVerifyRequest(
                listOf("0104602220006549215opFcmK\u001d93dGVz"),
            )

            val callback = object : IBundleResultCallback.Stub() {
                override fun onSuccess(bundle: Bundle) {
                    bundle.classLoader = MarkingVerifyResponse::class.java.classLoader
                    val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        bundle.getParcelable(
                            "key",
                            MarkingVerifyResponse::class.java
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        bundle.getParcelable("key")
                    }

                    _uiState.update { state ->
                        state.copy(
                            lastResult = "Успех: ${result?.toPrettyString()}",
                            lastAction = "Запрос выполнен успешно",
                            isValid = true
                        )
                    }
                }

                override fun onError(code: Int, message: String) {
                    _uiState.update { state ->
                        state.copy(
                            lastError = "Ошибка $code: $message",
                            lastAction = "Запрос завершился ошибкой"
                        )
                    }
                }
            }

            service.requestCheck(callback, testRequest)
            _uiState.update { state ->
                state.copy(
                    lastAction = "Запрос отправлен..."
                )
            }

        } catch (e: RemoteException) {
            _uiState.update { state ->
                state.copy(
                    lastError = "Ошибка связи: ${e.message}",
                    lastAction = "Ошибка при вызове сервиса"
                )
            }
        }
    }

    override fun onCleared() {
        iMarkingManager = null
        super.onCleared()
    }
}