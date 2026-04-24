package ru.esm.tspiot.data

import android.content.Intent
import android.content.ServiceConnection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.esm.tspiot.data.models.CashierInfoModel
import ru.esm.tspiot.data.models.ErrorRequestModel
import ru.esm.tspiot.data.models.ImcData
import ru.esm.tspiot.data.models.IsmNoticeInfoModel
import ru.esm.tspiot.data.models.KktInfoModel
import ru.esm.tspiot.data.models.ReceiptInfoModel

interface PiotManagerClient {

    val lock: Any
    val _isConnected: MutableStateFlow<Boolean>
    val isConnected: StateFlow<Boolean>

    val serviceConnection: ServiceConnection

    fun connect(intent: Intent)
    fun disconnect()

    suspend fun setShiftState(isClosed: Boolean, kktInfo: KktInfoModel)
    suspend fun setImcData(imcData: ImcData, kktInfo: KktInfoModel, isOnline: Boolean)
    suspend fun setError(request: ErrorRequestModel, kktInfo: KktInfoModel)
    suspend fun setIsmNotice(info: IsmNoticeInfoModel, kktInfo: KktInfoModel)
    suspend fun setRawEvent(event: String)
    suspend fun setReceiptInfo(info: ReceiptInfoModel, kktInfo: KktInfoModel)
    suspend fun setKktInfo(kktInfo: KktInfoModel)
    suspend fun setCashier(cashierInfo: CashierInfoModel, kktInfo: KktInfoModel)
}