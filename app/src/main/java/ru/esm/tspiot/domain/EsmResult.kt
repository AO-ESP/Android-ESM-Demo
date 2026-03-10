package ru.esm.tspiot.domain

/**
 * Результат выполнения операции.
 */
sealed class EsmResult<out T> {
    data class Success<T>(val data: T) : EsmResult<T>()
    data class Error(val code: Int, val message: String?) : EsmResult<Nothing>()
    data object ServiceUnavailable : EsmResult<Nothing>()
    data object Loading : EsmResult<Nothing>()
}
