package ru.esm.tspiot.ui.viewmodels

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put
import ru.esm.tspiot.data.PiotManagerClient
import ru.esm.tspiot.data.models.CashierInfoModel
import ru.esm.tspiot.data.models.ErrorRequestModel
import ru.esm.tspiot.data.models.ImcData
import ru.esm.tspiot.data.models.IsmNoticeInfoModel
import ru.esm.tspiot.data.models.KktInfoModel
import ru.esm.tspiot.data.models.ReceiptInfoModel
import ru.esp.pmsr.v2.BuildConfig
import javax.inject.Inject

@HiltViewModel
class PiotViewModel @Inject constructor(
    private val piotManagerClient: PiotManagerClient
) : ViewModel() {

    val isPiotManagerConnected: StateFlow<Boolean> = piotManagerClient.isConnected

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

    fun setShiftState(isClosed: Boolean, kktInfo: KktInfoModel) {
        viewModelScope.launch {
            logSentEvent(
                "setShiftState",
                buildJsonObject {
                    put("isClosed", isClosed)
                    put("kktInfo", kktInfo.toJsonElement())
                }
            )
            piotManagerClient.setShiftState(
                isClosed, kktInfo
            )
        }
    }

    fun setImcData(imcData: ImcData, kktInfo: KktInfoModel, isOnline: Boolean) {
        viewModelScope.launch {
            logSentEvent(
                "setImcData",
                buildJsonObject {
                    put("imcData", imcData.toJsonElement())
                    put("kktInfo", kktInfo.toJsonElement())
                    put("isOnline", isOnline)
                }
            )
            piotManagerClient.setImcData(
                imcData, kktInfo, isOnline
            )
        }
    }

    fun setError(request: ErrorRequestModel, kktInfo: KktInfoModel) {
        viewModelScope.launch {
            logSentEvent(
                "setError",
                buildJsonObject {
                    put("request", request.toJsonElement())
                    put("kktInfo", kktInfo.toJsonElement())
                }
            )
            piotManagerClient.setError(
                request, kktInfo
            )
        }
    }

    fun setIsmNotice(info: IsmNoticeInfoModel, kktInfo: KktInfoModel) {
        viewModelScope.launch {
            logSentEvent(
                "setIsmNotice",
                buildJsonObject {
                    put("info", info.toJsonElement())
                    put("kktInfo", kktInfo.toJsonElement())
                }
            )
            piotManagerClient.setIsmNotice(
                info, kktInfo
            )
        }
    }

    fun setRawEvent(event: String) {
        viewModelScope.launch {
            val eventJson = runCatching { prettyJson.parseToJsonElement(event) }.getOrNull()
            logSentEvent(
                "setRawEvent",
                buildJsonObject {
                    if (eventJson != null) put("event", eventJson) else put("event", event)
                }
            )
            piotManagerClient.setRawEvent(event)
        }
    }

    fun setReceiptInfo(info: ReceiptInfoModel, kktInfo: KktInfoModel) {
        viewModelScope.launch {
            logSentEvent(
                "setReceiptInfo",
                buildJsonObject {
                    put("info", info.toJsonElement())
                    put("kktInfo", kktInfo.toJsonElement())
                }
            )
            piotManagerClient.setReceiptInfo(
                info, kktInfo
            )
        }
    }

    fun setKktInfo(kktInfo: KktInfoModel) {
        viewModelScope.launch {
            logSentEvent(
                "setKktInfo",
                buildJsonObject {
                    put("kktInfo", kktInfo.toJsonElement())
                }
            )
            piotManagerClient.setKktInfo(kktInfo)
        }
    }

    fun setCashier(cashierInfo: CashierInfoModel, kktInfo: KktInfoModel) {
        viewModelScope.launch {
            logSentEvent(
                "setCashier",
                buildJsonObject {
                    put("cashierInfo", cashierInfo.toJsonElement())
                    put("kktInfo", kktInfo.toJsonElement())
                }
            )
            piotManagerClient.setCashier(
                cashierInfo, kktInfo
            )
        }
    }

    fun getScreenManageConsumer(): PiotScreenManageConsumer {
        return PiotScreenManageConsumer(
            onConnect = { connectToPiotManager() },
            onDisconnect = { disconnectFromPiotManager() },
            onSetShiftStateAction = { isClosed, kktInfo ->
                setShiftState(isClosed, kktInfo)
            },
            onSetImcDataAction = { imcData, kktInfo, isOnline ->
                setImcData(imcData, kktInfo, isOnline)
            },
            onSetErrorAction = { request, kktInfo ->
                setError(request, kktInfo)
            },
            onSetIsmNoticeAction = { info, kktInfo ->
                setIsmNotice(info, kktInfo)
            },
            onSetRawEventAction = { event ->
                setRawEvent(event)
            },
            onSetReceiptInfoModelAction = { info, kktInfo ->
                setReceiptInfo(info, kktInfo)
            },
            onSetKktInfoModelAction = { kktInfo ->
                setKktInfo(kktInfo)
            },
            onSetCashierAction = { cashierInfo, kktInfo ->
                setCashier(cashierInfo, kktInfo)
            }
        )
    }

    private fun getServicePackage(): String {
        return when (BuildConfig.FLAVOR) {
            "atol" -> ATOL_PACKAGE
            else -> OTHER_PACKAGE
        }
    }

    private fun logSentEvent(method: String, payload: JsonElement) {
        Log.d(TAG, "$method\n${prettyJson.encodeToString(payload)}")
    }

    private inline fun <reified T> T.toJsonElement(): JsonElement =
        prettyJson.encodeToJsonElement(this)

    companion object {
        private const val TAG = "PiotViewModel"
        private val prettyJson = Json {
            prettyPrint = true
            encodeDefaults = true
        }
        private const val PIOT_ESM_MANAGER_ACTION = "ru.esm.tspiot.action.ACTION_PIOT_MANAGER"
        private const val PIOT_ATOL_MANAGER_ACTION = "ru.atol.os.tspiot.action.ACTION_PIOT_MANAGER"
        private const val ATOL_PACKAGE = "ru.atol.os.tspiot"
        private const val OTHER_PACKAGE = "ru.esp.tspiot"
    }
}

data class PiotScreenManageConsumer(
    val onConnect: () -> Unit,
    val onDisconnect: () -> Unit,
    val onSetShiftStateAction: (
        isClosed: Boolean,
        KktInfoModel: KktInfoModel,
    ) -> Unit,
    val onSetImcDataAction: (
        imcData: ImcData,
        KktInfoModel: KktInfoModel,
        isOnline: Boolean
    ) -> Unit,
    val onSetErrorAction: (
        request: ErrorRequestModel,
        KktInfoModel: KktInfoModel
    ) -> Unit,
    val onSetIsmNoticeAction: (
        info: IsmNoticeInfoModel,
        KktInfoModel: KktInfoModel
    ) -> Unit,
    val onSetRawEventAction: (
        event: String
    ) -> Unit,
    val onSetReceiptInfoModelAction: (
        info: ReceiptInfoModel,
        KktInfoModel: KktInfoModel
    ) -> Unit,
    val onSetKktInfoModelAction: (
        KktInfoModel: KktInfoModel
    ) -> Unit,
    val onSetCashierAction: (
        CashierInfoModel: CashierInfoModel,
        KktInfoModel: KktInfoModel
    ) -> Unit
)
