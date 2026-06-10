package ru.esm.tspiot.ui.viewmodels

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.esm.tspiot.data.EsmServiceClient
import ru.esm.tspiot.domain.EsmResult
import ru.esm.tspiot.domain.EsmResult.Error
import ru.esm.tspiot.domain.EsmResult.Success
import ru.esp.esm.api.model.KktInfoExternal
import toPrettyString
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val esmServiceClient: EsmServiceClient
) : ViewModel() {
    val isConnected: StateFlow<Boolean> = esmServiceClient.isConnected
    private val _pingResult =
        MutableStateFlow<EsmResult<Boolean>>(EsmResult.ServiceUnavailable)
    val pingResult: StateFlow<EsmResult<Boolean>> = _pingResult
    private val _getInfoResult = MutableStateFlow<EsmResult<String>?>(null)
    val getInfoResult: StateFlow<EsmResult<String>?> = _getInfoResult

    fun connect() {
        val intent = Intent(ESM_SERVICE_ACTION).apply {
            setPackage(getServicePackage()) // Пакет приложения-источника
        }
        esmServiceClient.connect(intent)
    }

    fun disconnect() {
        esmServiceClient.disconnect()
    }

    fun refresh() {
        viewModelScope.launch {
            _pingResult.value = Success(esmServiceClient.ping())
        }
    }

    fun getInfo() {
        Log.d(TAG, "getInfo called")
        viewModelScope.launch {
            _getInfoResult.value = EsmResult.Loading
            val result = try {
                esmServiceClient.getInfo()
            } catch (e: UnsupportedOperationException) {
                Log.d(
                    TAG, "Method getInfo() not supported. Check EsmService version\n" +
                            "${e.message}"
                )
                _getInfoResult.value = Error(-1, e.message)
                return@launch
            } catch (e: Exception) {
                Log.d(TAG, "${e.message}")
                _getInfoResult.value = Error(-1, e.message)
                return@launch
            }
            when (result) {
                is Success -> {
                    val bundle = result.data
                    bundle.classLoader = KktInfoExternal::class.java.classLoader
                    val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        bundle.getParcelable(
                            "key",
                            KktInfoExternal::class.java
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        bundle.getParcelable("key")
                    }
                    _getInfoResult.value = Success("Успех: ${result?.toPrettyString()}")
                }

                is Error -> {
                    _getInfoResult.value = Error(result.code, result.message)
                }

                EsmResult.ServiceUnavailable -> {
                    _getInfoResult.value = EsmResult.ServiceUnavailable
                }

                EsmResult.Loading -> {} // do nothing
            }
        }
    }

    fun getServicePackage(): String {
        return when (ru.esp.pmsr.v2.BuildConfig.FLAVOR) {
            "atol" -> ATOL_PACKAGE
            else -> OTHER_PACKAGE
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
        private const val ESM_SERVICE_ACTION = "ru.esp.esm.action.ACTION_ESM_SERVICE"
        private const val ATOL_PACKAGE = "ru.atol.os.tspiot"
        private const val OTHER_PACKAGE = "ru.esp.tspiot"
    }
}