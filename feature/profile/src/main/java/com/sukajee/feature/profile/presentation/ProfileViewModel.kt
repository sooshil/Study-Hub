package com.sukajee.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sukajee.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState(isLoading = true))
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _effects = Channel<ProfileEffect>()
    val effects = _effects.receiveAsFlow()

    init {
        repository.getUserProfile()
            .onEach { user -> _state.update { it.copy(user = user, isLoading = false) } }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.LogoutRequested -> _state.update { it.copy(showLogoutDialog = true) }
            is ProfileEvent.LogoutDismissed -> _state.update { it.copy(showLogoutDialog = false) }
            is ProfileEvent.LogoutConfirmed -> {
                _state.update { it.copy(showLogoutDialog = false) }
                viewModelScope.launch {
                    repository.logout()
                    _effects.send(ProfileEffect.NavigateToLogin)
                }
            }
            is ProfileEvent.NotificationsToggled -> _state.update { it.copy(notificationsEnabled = event.enabled) }
            is ProfileEvent.DarkModeToggled -> _state.update { it.copy(darkModeEnabled = event.enabled) }
            is ProfileEvent.WifiOnlyToggled -> _state.update { it.copy(downloadOverWifiOnly = event.enabled) }
        }
    }
}
