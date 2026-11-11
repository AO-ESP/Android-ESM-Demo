package ru.esm.tspiot.data.models

import kotlinx.serialization.Serializable

@Serializable
data class CashierInfoModel(
    val info: String? = null,
    val inn: String? = null
)
