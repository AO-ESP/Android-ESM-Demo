package ru.esm.tspiot.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ErrorRequestModel(
    val errors: List<ErrorInfoModel?>? = null
)
