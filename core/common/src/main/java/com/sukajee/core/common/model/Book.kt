package com.sukajee.core.common.model

data class Book(
    val id: String,
    val title: String,
    val subject: String,
    val grade: String,
    val emoji: String,
    val accentColorHex: String,
    val totalPages: Int,
    val currentPage: Int = 0,
    val isBookmarked: Boolean = false,
    val status: BookStatus = BookStatus.NOT_STARTED
) {
    val progress: Float get() = if (totalPages == 0) 0f else currentPage.toFloat() / totalPages
    val progressPercent: Int get() = (progress * 100).toInt()
}

enum class BookStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED
}