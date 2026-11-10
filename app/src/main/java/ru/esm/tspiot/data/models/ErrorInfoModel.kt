package ru.esm.tspiot.data.models

data class ErrorInfoModel(
    val code: Int = 0,
    val message: String? = null,
    val module: String? = null,
    val type: String? = null
)