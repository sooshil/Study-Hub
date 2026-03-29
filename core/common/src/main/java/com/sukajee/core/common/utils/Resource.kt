package com.sukajee.core.common.utils

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()

    val isSuccess get() = this is Success
    val isError get() = this is Error
    val isLoading get() = this is Loading

    fun getOrNull(): T? = if (this is Success) data else null

    fun <R> map(transform: (T) -> R): Resource<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> this
        is Loading -> Loading
    }
}

suspend fun <T> safeApiCall(call: suspend () -> T): Resource<T> = try {
    Resource.Success(call())
} catch (e: Exception) {
    Resource.Error(e.message ?: "An unexpected error occurred", e)
}
