package ru.esm.tspiot.data.models

import kotlinx.serialization.Serializable

@Serializable
data class IsmNoticeInfoModel(
    val issueDate: String? = null,
    val sendDate: String? = null,
    val noticeId: Int = 0,
    val receiptId: String? = null
)
