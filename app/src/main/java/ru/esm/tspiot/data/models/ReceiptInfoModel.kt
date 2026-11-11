package ru.esm.tspiot.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ReceiptInfoModel(
    val receiptId: String? = null,
    val imcData: List<ReceiptImcDataModel?>? = null
)
