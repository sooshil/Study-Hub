package com.sukajee.feature.profile.presentation

import com.sukajee.core.common.model.User

data class ProfileState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val downloadOverWifiOnly: Boolean = true
)

sealed interface ProfileEvent {
    data object LogoutRequested : ProfileEvent
    data object LogoutConfirmed : ProfileEvent
    data object LogoutDismissed : ProfileEvent
    data class NotificationsToggled(val enabled: Boolean) : ProfileEvent
    data class DarkModeToggled(val enabled: Boolean) : ProfileEvent
    data class WifiOnlyToggled(val enabled: Boolean) : ProfileEvent
}

sealed interface ProfileEffect {
    data object NavigateToLogin : ProfileEffect
}
