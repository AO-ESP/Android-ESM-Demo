package ru.atol.os.tspiot.driver.api.mapper

import ru.esm.tspiot.data.models.CashierInfoModel
import ru.esm.tspiot.data.models.ErrorInfoModel
import ru.esm.tspiot.data.models.ErrorRequestModel
import ru.esm.tspiot.data.models.IsmNoticeInfoModel
import ru.esm.tspiot.data.models.KktInfoModel
import ru.esm.tspiot.data.models.ReceiptImcDataModel
import ru.esm.tspiot.data.models.ReceiptInfoModel
import ru.atol.os.tspiot.driver.api.model.CashierInfo
import ru.atol.os.tspiot.driver.api.model.ErrorInfo
import ru.atol.os.tspiot.driver.api.model.ErrorRequest
import ru.atol.os.tspiot.driver.api.model.IsmNoticeInfo
import ru.atol.os.tspiot.driver.api.model.KktInfo
import ru.atol.os.tspiot.driver.api.model.ReceiptImcData
import ru.atol.os.tspiot.driver.api.model.ReceiptInfo

fun CashierInfoModel.mapToESMModel(): CashierInfo {
    return CashierInfo(
        this.info,
        this.inn
    )
}

fun ErrorInfoModel.mapToESMModel(): ErrorInfo {
    return ErrorInfo(
        this.code,
        this.message,
        this.module,
        this.type,
    )
}

fun ErrorRequestModel.mapToESMModel(): ErrorRequest {
    return ErrorRequest(
        this.errors?.map {
            ErrorInfo(
                it?.code ?: 0,
                it?.message,
                it?.module,
                it?.type,
            )
        }
    )
}

fun IsmNoticeInfoModel.mapToESMModel(): IsmNoticeInfo {
    return IsmNoticeInfo(
        this.issueDate,
        this.sendDate,
        this.noticeId,
        this.receiptId,
    )
}

fun KktInfoModel.mapToESMModel(): KktInfo {
    return KktInfo(
        this.fnSerial,
        this.kktInn,
        this.kktSerial,
        this.firmwareVersion,
        this.fnVersion,
    )
}

fun ReceiptImcDataModel.mapToESMModel(): ReceiptImcData {
    return ReceiptImcData(
        this.ki,
        this.ofdStatus,
    )
}

fun ReceiptInfoModel.mapToESMModel(): ReceiptInfo {
    return ReceiptInfo(
        this.receiptId,
        this.imcData?.map {
            ReceiptImcData(
                it?.ki,
                it?.ofdStatus,
            )
        },
    )
}