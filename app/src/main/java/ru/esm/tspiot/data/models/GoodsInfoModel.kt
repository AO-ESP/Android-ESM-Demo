package ru.esm.tspiot.data.models

import kotlinx.serialization.Serializable

@Serializable
data class GoodsInfoModel(
    val goodsName: String? = null,
    val price: Double = -1.0,
    val scannedCode: String? = null,
    val quantity: Double = -1.0,
    val paymentType: Int = -1,
    val itemType: Int = -1,
    val unitMeasurementType: Int = -1,
    val goodsInfoCheckResult: Int = -1,
    val taxRate: Int = -1,
    val amount: Double = -1.0,
)
