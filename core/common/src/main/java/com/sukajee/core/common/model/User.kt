package com.sukajee.core.common.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val grade: String,
    val avatarEmoji: String = "🎓",
    val booksRead: Int = 0,
    val hoursStudied: Int = 0,
    val streakDays: Int = 0,
    val gradeAverage: String = "A+"
)
