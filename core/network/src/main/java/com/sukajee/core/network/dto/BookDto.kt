package com.sukajee.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookDto(
    val id: String,
    val title: String,
    val subject: String,
    val grade: String,
    val emoji: String,
    @SerialName("accent_color") val accentColorHex: String,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("current_page") val currentPage: Int = 0,
    @SerialName("is_bookmarked") val isBookmarked: Boolean = false,
    val status: String = "NOT_STARTED"
)

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequestDto(
    val name: String,
    val email: String,
    val password: String,
    val grade: String
)

@Serializable
data class LoginResponseDto(
    val token: String,
    val user: UserDto
)

@Serializable
data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val grade: String,
    @SerialName("avatar_emoji") val avatarEmoji: String = "🎓",
    @SerialName("books_read") val booksRead: Int = 0,
    @SerialName("hours_studied") val hoursStudied: Int = 0,
    @SerialName("streak_days") val streakDays: Int = 0,
    @SerialName("grade_average") val gradeAverage: String = "A+"
)
