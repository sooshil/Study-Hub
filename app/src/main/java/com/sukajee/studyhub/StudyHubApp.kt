package com.sukajee.studyhub

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class StudyHubApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@StudyHubApp)
            modules(
                networkModule,
                databaseModule,
                authModule,
                homeModule,
                libraryModule,
                readerModule,
                progressModule,
                profileModule
            )
        }
    }
}