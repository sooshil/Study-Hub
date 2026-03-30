package com.sukajee.studyhub

import android.app.Application
import com.sukajee.core.database.di.databaseModule
import com.sukajee.core.network.di.networkModule
import com.sukajee.feature.auth.di.authModule
import com.sukajee.feature.home.di.homeModule
import com.sukajee.feature.library.di.libraryModule
import com.sukajee.feature.profile.di.profileModule
import com.sukajee.feature.progress.di.progressModule
import com.sukajee.feature.reader.di.readerModule
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