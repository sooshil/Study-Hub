package com.sukajee.feature.library.di

import com.sukajee.feature.library.data.repository.LibraryRepositoryImpl
import com.sukajee.feature.library.domain.repository.LibraryRepository
import com.sukajee.feature.library.presentation.LibraryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val libraryModule = module {
    singleOf(::LibraryRepositoryImpl) bind LibraryRepository::class
    viewModelOf(::LibraryViewModel)
}
