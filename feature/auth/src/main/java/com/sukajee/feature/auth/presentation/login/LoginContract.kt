package com.sukajee.feature.auth.presentation.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null
)

sealed interface LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent
    data class PasswordChanged(val password: String) : LoginEvent
    data object LoginClicked : LoginEvent
    data object ErrorDismissed : LoginEvent
}

sealed interface LoginEffect {
    data object NavigateToHome : LoginEffect
    data object NavigateToRegister : LoginEffect
}
