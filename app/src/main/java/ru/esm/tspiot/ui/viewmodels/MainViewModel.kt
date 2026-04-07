package ru.esm.tspiot.ui.viewmodels

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.esm.tspiot.data.EsmServiceClient
import ru.esm.tspiot.data.PiotManagerClient
import ru.esm.tspiot.data.models.CashierInfoModel
import ru.esm.tspiot.data.models.ErrorRequestModel
import ru.esm.tspiot.data.models.IsmNoticeInfoModel
import ru.esm.tspiot.data.models.KktInfoModel
import ru.esm.tspiot.data.models.ReceiptInfoModel
import ru.esm.tspiot.domain.EsmResult
import ru.esm.tspiot.domain.PiotResult
import ru.esp.pmsr.v2.BuildConfig
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val esmServiceClient: EsmServiceClient,
    private val piotManagerClient: PiotManagerClient
) : ViewModel() {

    val isESMServiceConnected: StateFlow<Boolean> = esmServiceClient.isConnected
    val isPiotManagerConnected: StateFlow<Boolean>
        = piotManagerClient.isConnected

    private val _pingESMServiceResult =
        MutableStateFlow<EsmResult<Boolean>>(EsmResult.ServiceUnavailable)
    val pingESMServiceResult: StateFlow<EsmResult<Boolean>> = _pingESMServiceResult
    private val _pingPiotManagerResult =
        MutableStateFlow<PiotResult<Boolean>>(PiotResult.ServiceUnavailable)
    val pingPiotManagerResult: StateFlow<PiotResult<Boolean>> = _pingPiotManagerResult

    fun connectToESMService() {
        val intent = Intent(ESM_SERVICE_ACTION).apply {
            setPackage(getServicePackage()) // Пакет приложения-источника
        }
        esmServiceClient.connect(intent)
    }

    fun disconnectFromESMService() {
        esmServiceClient.disconnect()
    }

    fun refreshESMService() {
        viewModelScope.launch {
            _pingESMServiceResult.value = EsmResult.Success(esmServiceClient.ping())
        }
    }

    fun connectToPiotManager() {
        val intent = if (BuildConfig.FLAVOR == "atol") {
             Intent(PIOT_ATOL_MANAGER_ACTION).apply {
                setPackage(getServicePackage()) // Пакет приложения-источника
            }
        } else {
            Intent(PIOT_ESM_MANAGER_ACTION).apply {
                setPackage(getServicePackage()) // Пакет приложения-источника
            }

        }
        piotManagerClient.connect(intent)
    }

    fun disconnectFromPiotManager() {
        piotManagerClient.disconnect()
    }

    fun refreshPiotManager() {
        viewModelScope.launch {
            piotManagerClient.let {
                _pingPiotManagerResult.value = PiotResult.Success(it.ping())
            }
        }
    }

    fun setShiftState(isClosed: Boolean, kktInfo: KktInfoModel){
        viewModelScope.launch {
            piotManagerClient.setShiftState(
                isClosed, kktInfo
            )
        }
    }

    fun setImcData(imcData: String, kktInfo: KktInfoModel, isOnline: Boolean){
        viewModelScope.launch {
            piotManagerClient.setImcData(
                imcData, kktInfo, isOnline
            )
        }
    }

    fun setError(request: ErrorRequestModel, kktInfo: KktInfoModel){
        viewModelScope.launch {
            piotManagerClient.setError(
                request, kktInfo
            )
        }
    }

    fun setIsmNotice(info: IsmNoticeInfoModel, kktInfo: KktInfoModel){
        viewModelScope.launch {
            piotManagerClient.setIsmNotice(
                info, kktInfo
            )
        }
    }

    fun setRawEvent(event: String){
        viewModelScope.launch {
            piotManagerClient.setRawEvent(event)
        }
    }

    fun setReceiptInfo(info: ReceiptInfoModel, kktInfo: KktInfoModel){
        viewModelScope.launch {
            piotManagerClient.setReceiptInfo(
                info, kktInfo
            )
        }
    }

    fun setKktInfo(kktInfo: KktInfoModel){
        viewModelScope.launch {
            piotManagerClient.setKktInfo(kktInfo)
        }
    }

    fun setCashier(cashierInfo: CashierInfoModel, kktInfo: KktInfoModel){
        viewModelScope.launch {
            piotManagerClient.setCashier(
                cashierInfo, kktInfo
            )
        }
    }

    fun getServicePackage(): String {
        return when (BuildConfig.FLAVOR) {
            "atol" -> ATOL_PACKAGE
            else -> OTHER_PACKAGE
        }
    }

    companion object {
        private const val ESM_SERVICE_ACTION = "ru.esp.esm.action.ACTION_ESM_SERVICE"
        private const val PIOT_ESM_MANAGER_ACTION = "ru.esm.tspiot.action.ACTION_PIOT_MANAGER"
        private const val PIOT_ATOL_MANAGER_ACTION = "ru.atol.os.tspiot.action.ACTION_PIOT_MANAGER"
        private const val ATOL_PACKAGE = "ru.atol.os.tspiot"
        private const val OTHER_PACKAGE = "ru.esp.tspiot"
    }
}