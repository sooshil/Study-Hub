package com.sukajee.core.database.di

import androidx.room.Room
import com.sukajee.core.database.StudyHubDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import kotlin.jvm.java

val databaseModule = module {

    single {
        Room.databaseBuilder(
                androidContext(),
                StudyHubDatabase::class.java,
                "studyhub_database"
            ).fallbackToDestructiveMigration(false).build()
    }

    single { get<StudyHubDatabase>().bookDao() }
    single { get<StudyHubDatabase>().userDao() }
}
