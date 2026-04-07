package ru.esm.tspiot.domain

/**
 * Результат выполнения операции.
 */
sealed class PiotResult<out T> {
    data class Success<T>(val data: T) : PiotResult<T>()
    data class Error(val code: Int, val message: String?) : PiotResult<Nothing>()
    data object ServiceUnavailable : PiotResult<Nothing>()
    data object Loading : PiotResult<Nothing>()
}
