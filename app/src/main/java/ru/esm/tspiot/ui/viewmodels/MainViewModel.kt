package ru.esm.tspiot.ui.viewmodels

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.esm.tspiot.data.EsmServiceClient
import ru.esm.tspiot.domain.EsmResult
import ru.esm.tspiot.domain.EsmResult.Error
import ru.esm.tspiot.domain.EsmResult.Success
import ru.esp.esm.api.model.KktInfoExternal
import ru.esp.pmsr.v2.BuildConfig
import toPrettyString
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val esmServiceClient: EsmServiceClient
) : ViewModel() {

    val isESMServiceConnected: StateFlow<Boolean> = esmServiceClient.isConnected

    private val _serviceVersion = MutableStateFlow<Int?>(null)
    val serviceVersion: StateFlow<Int?> = _serviceVersion.asStateFlow()
    private val _getInfoResult = MutableStateFlow<EsmResult<String>?>(null)
    val getInfoResult: StateFlow<EsmResult<String>?> = _getInfoResult

    private val _pingESMServiceResult =
        MutableStateFlow<EsmResult<Boolean>>(EsmResult.ServiceUnavailable)
    val pingESMServiceResult: StateFlow<EsmResult<Boolean>> = _pingESMServiceResult

    init {
        viewModelScope.launch {
            isESMServiceConnected.collect { connected ->
                _serviceVersion.value = if (connected) readServiceVersion() else null
            }
        }
    }

    fun connectToESMService() {
        val intent = Intent(ESM_SERVICE_ACTION).apply {
            setPackage(getServicePackage())
        }
        esmServiceClient.connect(intent)
    }

    fun disconnectFromESMService() {
        esmServiceClient.disconnect()
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
                    val info = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        bundle.getParcelable(
                            "key",
                            KktInfoExternal::class.java
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        bundle.getParcelable("key")
                    }
                    _getInfoResult.value = Success("Успех: ${info?.toPrettyString()}")
                }

                is Error -> {
                    _getInfoResult.value = Error(result.code, result.message)
                }

                EsmResult.ServiceUnavailable -> {
                    _getInfoResult.value = EsmResult.ServiceUnavailable
                }

                EsmResult.Loading -> {}
            }
        }
    }

    fun refreshESMService() {
        viewModelScope.launch {
            _pingESMServiceResult.value = EsmResult.Success(esmServiceClient.ping())
        }
    }

    fun getServicePackage(): String {
        return when (BuildConfig.FLAVOR) {
            "atol" -> ATOL_PACKAGE
            else -> OTHER_PACKAGE
        }
    }

    private fun readServiceVersion(): Int? {
        return when (val result = esmServiceClient.getAidlVersion()) {
            is Success -> result.data
            else -> null
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
        private const val ESM_SERVICE_ACTION = "ru.esp.esm.action.ACTION_ESM_SERVICE"
        private const val ATOL_PACKAGE = "ru.atol.os.tspiot"
        private const val OTHER_PACKAGE = "ru.esp.tspiot"
    }
}
