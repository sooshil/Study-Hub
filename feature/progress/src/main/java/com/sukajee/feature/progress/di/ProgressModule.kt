package com.sukajee.feature.progress.di

import com.sukajee.feature.progress.data.repository.ProgressRepositoryImpl
import com.sukajee.feature.progress.domain.repository.ProgressRepository
import com.sukajee.feature.progress.presentation.ProgressViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val progressModule = module {
    singleOf(::ProgressRepositoryImpl) bind ProgressRepository::class
    viewModelOf(::ProgressViewModel)
}