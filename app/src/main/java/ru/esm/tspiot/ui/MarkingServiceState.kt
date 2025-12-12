package ru.esm.tspiot.ui

data class MarkingServiceState(
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val connectionStatus: String = "Не подключено",
    val lastAction: String = "",
    val lastResult: String = "",
    val lastError: String = "",
    val isValid: Boolean? = null
)