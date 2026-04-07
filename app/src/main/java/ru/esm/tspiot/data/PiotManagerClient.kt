package ru.esm.tspiot.data

import android.content.Intent
import android.content.ServiceConnection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.esm.tspiot.data.models.CashierInfoModel
import ru.esm.tspiot.data.models.ErrorRequestModel
import ru.esm.tspiot.data.models.IsmNoticeInfoModel
import ru.esm.tspiot.data.models.KktInfoModel
import ru.esm.tspiot.data.models.ReceiptInfoModel
import ru.esm.tspiot.domain.PiotResult

interface PiotManagerClient {

    val lock: Any
    val _isConnected: MutableStateFlow<Boolean>
    val isConnected: StateFlow<Boolean>

    val serviceConnection: ServiceConnection

    fun connect(intent: Intent)
    fun disconnect()

    suspend fun ping(): Boolean

    suspend fun getAidlVersion(): PiotResult<Int>

    suspend fun setShiftState(isClosed: Boolean, kktInfo: KktInfoModel): PiotResult<Boolean>
    suspend fun setImcData(imcData: String, kktInfo: KktInfoModel, isOnline: Boolean): PiotResult<Unit>
    suspend fun setError(request: ErrorRequestModel, kktInfo: KktInfoModel): PiotResult<Unit>
    suspend fun setIsmNotice(info: IsmNoticeInfoModel, kktInfo: KktInfoModel): PiotResult<Unit>
    suspend fun setRawEvent(event: String): PiotResult<Unit>
    suspend fun setReceiptInfo(info: ReceiptInfoModel, kktInfo: KktInfoModel): PiotResult<Unit>
    suspend fun setKktInfo(kktInfo: KktInfoModel): PiotResult<Unit>
    suspend fun setCashier(cashierInfo: CashierInfoModel, kktInfo: KktInfoModel): PiotResult<Unit>
}