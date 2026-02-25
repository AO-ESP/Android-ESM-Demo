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
import ru.esm.tspiot.ui.MarkingServiceState
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val esmServiceClient: EsmServiceClient
) : ViewModel() {
    private val _uiState = MutableStateFlow(MarkingServiceState())
    val uiState: StateFlow<MarkingServiceState> = _uiState


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

    private fun getServicePackage(): String {
        return when (ru.esp.esmdemo.BuildConfig.FLAVOR) {
            "atol" -> ATOL_PACKAGE
            else -> MSPOS_PACKAGE
        }
    }

    companion object {
        private const val ESM_SERVICE_ACTION = "ru.esp.esm.action.ACTION_ESM_SERVICE"
        private const val ATOL_PACKAGE = "ru.atol.os.tspiot"
        private const val ATOL_ACTION = "ru.atol.os.tspiot.action.ACTION_MARKING_MANAGER"
        private const val MSPOS_PACKAGE = "ru.esp.tspiot"
        private const val MSPOS_ACTION = "ru.esm.tspiot.action.ACTION_MARKING_MANAGER"
    }
}