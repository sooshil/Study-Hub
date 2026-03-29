package com.sukajee.feature.auth.di

import com.sukajee.feature.auth.data.local.AuthPreferencesManager
import com.sukajee.feature.auth.data.repository.AuthRepositoryImpl
import com.sukajee.feature.auth.domain.repository.AuthRepository
import com.sukajee.feature.auth.domain.usecases.LoginUseCase
import com.sukajee.feature.auth.domain.usecases.LogoutUseCase
import com.sukajee.feature.auth.domain.usecases.RegisterUseCase
import com.sukajee.feature.auth.presentation.login.LoginViewModel
import com.sukajee.feature.auth.presentation.register.RegisterViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    single { AuthPreferencesManager(androidContext()) }
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(::LoginUseCase)
    singleOf(::RegisterUseCase)
    singleOf(::LogoutUseCase)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
}