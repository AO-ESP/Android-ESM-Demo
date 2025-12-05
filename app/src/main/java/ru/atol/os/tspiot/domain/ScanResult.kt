package ru.atol.os.tspiot.domain

data class ScanResult(
    val text: String,
    val format: String,
    val timestamp: Long = System.currentTimeMillis()
)