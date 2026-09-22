package ru.esm.tspiot.ui.viewmodels

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.esm.tspiot.data.PiotManagerClient
import ru.esm.tspiot.domain.PiotResult
import ru.esm.tspiot.driver.api.model.ean.EanCheckRequest
import ru.esp.pmsr.v2.BuildConfig
import toPrettyString
import javax.inject.Inject

@HiltViewModel
class EanViewModel @Inject constructor(
    private val piotManagerClient: PiotManagerClient
) : ViewModel() {

    val isPiotManagerConnected: StateFlow<Boolean> = piotManagerClient.isConnected

    private val _eanCheckResult = MutableStateFlow<PiotResult<String>?>(null)
    val eanCheckResult: StateFlow<PiotResult<String>?> = _eanCheckResult

    fun connectToPiotManager() {
        val intent = if (BuildConfig.FLAVOR == "atol") {
            Intent(PIOT_ATOL_MANAGER_ACTION).apply {
                setPackage(getServicePackage())
            }
        } else {
            Intent(PIOT_ESM_MANAGER_ACTION).apply {
                setPackage(getServicePackage())
            }
        }
        piotManagerClient.connect(intent)
    }

    fun disconnectFromPiotManager() {
        piotManagerClient.disconnect()
    }

    fun eanCheck(request: EanCheckRequest) {
        viewModelScope.launch {
            _eanCheckResult.value = PiotResult.Loading
            when (val result = piotManagerClient.eanCheck(request)) {
                is PiotResult.Success -> {
                    _eanCheckResult.value = PiotResult.Success(
                        "Успех: ${result.data.toPrettyString()}"
                    )
                }

                is PiotResult.Error -> {
                    _eanCheckResult.value = PiotResult.Error(result.code, result.message)
                }

                PiotResult.ServiceUnavailable -> {
                    _eanCheckResult.value = PiotResult.ServiceUnavailable
                }

                PiotResult.Loading -> {}
            }
        }
    }

    fun getServicePackage(): String {
        return when (BuildConfig.FLAVOR) {
            "atol" -> ATOL_PACKAGE
            else -> OTHER_PACKAGE
        }
    }

    companion object {
        private const val PIOT_ESM_MANAGER_ACTION = "ru.esm.tspiot.action.ACTION_PIOT_MANAGER"
        private const val PIOT_ATOL_MANAGER_ACTION = "ru.atol.os.tspiot.action.ACTION_PIOT_MANAGER"
        private const val ATOL_PACKAGE = "ru.atol.os.tspiot"
        private const val OTHER_PACKAGE = "ru.esp.tspiot"
    }
}
