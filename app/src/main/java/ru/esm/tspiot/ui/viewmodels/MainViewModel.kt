package ru.esm.tspiot.ui.viewmodels

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.esm.tspiot.data.EsmServiceClient
import ru.esm.tspiot.domain.EsmResult
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val esmServiceClient: EsmServiceClient
) : ViewModel() {
    val isConnected: StateFlow<Boolean> = esmServiceClient.isConnected
    private val _pingResult =
        MutableStateFlow<EsmResult<Boolean>>(EsmResult.ServiceUnavailable)
    val pingResult: StateFlow<EsmResult<Boolean>> = _pingResult

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
            _pingResult.value = EsmResult.Success(esmServiceClient.ping())
        }
    }

    fun getServicePackage(): String {
        return when (ru.esp.esmdemo.BuildConfig.FLAVOR) {
            "atol" -> ATOL_PACKAGE
            else -> OTHER_PACKAGE
        }
    }

    companion object {
        private const val ESM_SERVICE_ACTION = "ru.esp.esm.action.ACTION_ESM_SERVICE"
        private const val ATOL_PACKAGE = "ru.atol.os.tspiot"
        private const val OTHER_PACKAGE = "ru.esp.tspiot"
    }
}