package com.sukajee.feature.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sukajee.core.common.utils.Resource
import com.sukajee.feature.auth.domain.usecases.LoginUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effects = Channel<LoginEffect>()
    val effects = _effects.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> _state.update { it.copy(email = event.email, emailError = null) }
            is LoginEvent.PasswordChanged -> _state.update { it.copy(password = event.password, passwordError = null) }
            is LoginEvent.LoginClicked -> login()
            is LoginEvent.ErrorDismissed -> _state.update { it.copy(error = null) }
        }
    }

    private fun login() {
        val current = _state.value
        var hasError = false

        if (current.email.isBlank()) {
            _state.update { it.copy(emailError = "Email is required") }
            hasError = true
        }
        if (current.password.isBlank()) {
            _state.update { it.copy(passwordError = "Password is required") }
            hasError = true
        }
        if (hasError) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = loginUseCase(current.email, current.password)) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _effects.send(LoginEffect.NavigateToHome)
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                }
                is Resource.Loading -> Unit
            }
        }
    }
}
