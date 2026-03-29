package com.sukajee.feature.home.di

import com.sukajee.feature.home.data.repository.HomeRepositoryImpl
import com.sukajee.feature.home.domain.repository.HomeRepository
import com.sukajee.feature.home.presentation.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeModule = module {
    singleOf(::HomeRepositoryImpl) bind HomeRepository::class
    viewModelOf(::HomeViewModel)
}
