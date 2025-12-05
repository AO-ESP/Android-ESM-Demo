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
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.atol.os.tspiot.api.model.ClientInfo
import ru.atol.os.tspiot.api.model.MarkingVerifyRequest
import ru.atol.os.tspiot.api.model.MarkingVerifyResponse
import ru.atol.os.tspiot.domain.ScanResult
import ru.atol.os.tspiot.ui.MarkingServiceState
import ru.esm.tspiot.api.IBundleResultCallback
import ru.esm.tspiot.api.IMarkingManager
import toPrettyString

class MainViewModel : ViewModel() {
    private var isBound: Boolean = false
    private var iMarkingManager: IMarkingManager? = null
    private val _uiState = MutableStateFlow(MarkingServiceState())
    val uiState: StateFlow<MarkingServiceState> = _uiState

    val isScanning = mutableStateOf(false)
    private val _scanResult = MutableSharedFlow<ScanResult?>(replay = 1)
    val scanResult = _scanResult.asSharedFlow()
    val errorMessage = mutableStateOf<String?>(null)

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
            val intent = Intent("ru.esm.tspiot.action.ACTION_MARKING_MANAGER").apply {
                setPackage("ru.esp.tspiot") // Пакет приложения-источника
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
            clearResult()
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

    fun testRequest(code: String?) {
        _uiState.update { state ->
            state.copy(
                lastResult = "",
                lastError = "",
                isValid = null
            )
        }
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

            val clientInfo = ClientInfo(
                "ESM Test",
                "1.0",
                "90911ffe-47da-4a71-86bb-be455f3d9614",
                "90911ffe-47da-4a71-86bb-be455f3d9614",
                null
            )

            val testRequest = code?.let { mark ->
                MarkingVerifyRequest(
                    listOf(if (mark.startsWith("\\u")) mark.substring(2) else mark),
                    clientInfo
                )
            }

            testRequest?.let { service.requestCheck(callback, testRequest) }
                ?: throw IllegalStateException("Марка не найдена")

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
        } catch (e: Exception) {
            Log.d("MainViewModel", e.message ?: e.stackTraceToString())
            _uiState.update { state ->
                state.copy(
                    lastError = "Ошибка: ${e.message ?: e.stackTraceToString()}",
                    lastAction = "Ошибка"
                )
            }
        }
    }

    override fun onCleared() {
        iMarkingManager = null
        super.onCleared()
    }

    fun startScanning() {
        viewModelScope.launch {
            isScanning.value = true
            _scanResult.emit(null)
            errorMessage.value = null
        }
    }

    fun stopScanning() {
        isScanning.value = false
    }

    fun onScanResult(result: ScanResult) {
        viewModelScope.launch {
            val charToRemove = "\u001D"
            val trimmedMark = result.text.removePrefix(charToRemove)
            _scanResult.emit(result.copy(text = trimmedMark))
            stopScanning()
        }
    }

    fun onScanError(error: String) {
        errorMessage.value = error
        stopScanning()
    }

    fun clearResult() {
        viewModelScope.launch {
            _scanResult.emit(null)
            errorMessage.value = null
        }
    }
}