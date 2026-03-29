package com.sukajee.feature.progress.domain.model

data class WeeklyStudyData(
    val day: String,
    val hours: Float,
    val isToday: Boolean = false
)

data class SubjectProgress(
    val subject: String,
    val emoji: String,
    val progress: Float,
    val accentColorHex: String,
    val completedPages: Int,
    val totalPages: Int
)

data class StudyStats(
    val totalHours: Int,
    val currentStreak: Int,
    val booksCompleted: Int,
    val gradeAverage: String,
    val weeklyGoalProgress: Float,
    val weeklyGoalHours: Int = 20
)
