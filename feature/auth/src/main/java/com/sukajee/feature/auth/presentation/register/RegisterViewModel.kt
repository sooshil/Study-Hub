package com.sukajee.feature.auth.presentation.register

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val grade: String = "Grade 10",
    val isLoading: Boolean = false,
    val error: String? = null,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)

sealed interface RegisterEvent {
    data class NameChanged(val name: String) : RegisterEvent
    data class EmailChanged(val email: String) : RegisterEvent
    data class PasswordChanged(val password: String) : RegisterEvent
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegisterEvent
    data class GradeChanged(val grade: String) : RegisterEvent
    data object RegisterClicked : RegisterEvent
    data object ErrorDismissed : RegisterEvent
}

sealed interface RegisterEffect {
    data object NavigateToHome : RegisterEffect
    data object NavigateBack : RegisterEffect
}
