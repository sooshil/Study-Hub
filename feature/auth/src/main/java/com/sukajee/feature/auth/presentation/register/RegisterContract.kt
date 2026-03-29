package com.sukajee.feature.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sukajee.core.common.utils.Resource
import com.sukajee.feature.auth.domain.usecases.RegisterUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerUseCase: RegisterUseCase) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    private val _effects = Channel<RegisterEffect>()
    val effects = _effects.receiveAsFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.NameChanged -> _state.update { it.copy(name = event.name, nameError = null) }
            is RegisterEvent.EmailChanged -> _state.update { it.copy(email = event.email, emailError = null) }
            is RegisterEvent.PasswordChanged -> _state.update { it.copy(password = event.password, passwordError = null) }
            is RegisterEvent.ConfirmPasswordChanged -> _state.update { it.copy(confirmPassword = event.confirmPassword, confirmPasswordError = null) }
            is RegisterEvent.GradeChanged -> _state.update { it.copy(grade = event.grade) }
            is RegisterEvent.RegisterClicked -> register()
            is RegisterEvent.ErrorDismissed -> _state.update { it.copy(error = null) }
        }
    }

    private fun register() {
        val current = _state.value
        var hasError = false

        if (current.name.isBlank()) {
            _state.update { it.copy(nameError = "Name is required") }
            hasError = true
        }
        if (current.email.isBlank()) {
            _state.update { it.copy(emailError = "Email is required") }
            hasError = true
        }
        if (current.password.length < 6) {
            _state.update { it.copy(passwordError = "Password must be at least 6 characters") }
            hasError = true
        }
        if (current.password != current.confirmPassword) {
            _state.update { it.copy(confirmPasswordError = "Passwords do not match") }
            hasError = true
        }
        if (hasError) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = registerUseCase(current.name, current.email, current.password, current.grade)) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _effects.send(RegisterEffect.NavigateToHome)
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                }
                is Resource.Loading -> Unit
            }
        }
    }
}
