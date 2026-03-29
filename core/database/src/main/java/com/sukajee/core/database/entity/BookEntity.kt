package com.sukajee.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subject: String,
    val grade: String,
    val emoji: String,
    @ColumnInfo(name = "accent_color") val accentColorHex: String,
    @ColumnInfo(name = "total_pages") val totalPages: Int,
    @ColumnInfo(name = "current_page") val currentPage: Int = 0,
    @ColumnInfo(name = "is_bookmarked") val isBookmarked: Boolean = false,
    val status: String = "NOT_STARTED"
)
