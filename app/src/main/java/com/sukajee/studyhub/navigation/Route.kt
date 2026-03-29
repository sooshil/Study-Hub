package com.sukajee.studyhub.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Login : Route

    @Serializable
    data object Register : Route

    @Serializable
    data object Home : Route

    @Serializable
    data object Library : Route

    @Serializable
    data class Reader(val bookId: String) : Route

    @Serializable
    data object Progress : Route

    @Serializable
    data object Profile : Route
}
