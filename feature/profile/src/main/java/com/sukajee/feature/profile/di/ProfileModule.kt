package com.sukajee.feature.profile.di

import com.sukajee.feature.profile.data.repository.ProfileRepositoryImpl
import com.sukajee.feature.profile.domain.repository.ProfileRepository
import com.sukajee.feature.profile.presentation.ProfileViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.bind

val profileModule = module {
    singleOf(::ProfileRepositoryImpl) bind ProfileRepository::class
    viewModelOf(::ProfileViewModel)
}