package com.sukajee.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sukajee.core.database.dao.BookDao
import com.sukajee.core.database.dao.UserDao
import com.sukajee.core.database.entity.BookEntity
import com.sukajee.core.database.entity.UserEntity

@Database(
    entities = [BookEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StudyHubDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun userDao(): UserDao
}
