package com.sukajee.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val grade: String,
    @ColumnInfo(name = "avatar_emoji") val avatarEmoji: String = "🎓",
    @ColumnInfo(name = "books_read") val booksRead: Int = 0,
    @ColumnInfo(name = "hours_studied") val hoursStudied: Int = 0,
    @ColumnInfo(name = "streak_days") val streakDays: Int = 0,
    @ColumnInfo(name = "grade_average") val gradeAverage: String = "A+"
)
