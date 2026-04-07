package ru.esm.tspiot.data.models

data class ReceiptInfoModel(
    val receiptId: String? = null,
    val imcData: List<ReceiptImcDataModel?>? = null
)
