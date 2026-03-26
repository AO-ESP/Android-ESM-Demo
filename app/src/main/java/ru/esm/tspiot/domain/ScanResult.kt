package ru.esm.tspiot.domain

import java.io.Serializable

@kotlinx.serialization.Serializable
data class ScanResult(
    val text: String,
    val format: String,
    val timestamp: Long = System.currentTimeMillis()
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ScanResult) return false

        if (text != other.text) return false
        if (format != other.format) return false

        return true
    }

    override fun hashCode(): Int {
        var result = text.hashCode()
        result = 31 * result + format.hashCode()
        return result
    }
}