package com.sukajee.core.common.utils

data class UiState<T>(
    val data: T? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
) {
    companion object {
        fun <T> loading() = UiState<T>(isLoading = true)
        fun <T> success(data: T) = UiState(data = data, isSuccess = true)
        fun <T> error(message: String) = UiState<T>(error = message)
        fun <T> idle() = UiState<T>()
    }
}
