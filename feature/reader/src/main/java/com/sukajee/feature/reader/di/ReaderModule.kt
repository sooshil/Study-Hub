package com.sukajee.feature.reader.di

import com.sukajee.feature.reader.data.repository.ReaderRepositoryImpl
import com.sukajee.feature.reader.domain.repository.ReaderRepository
import com.sukajee.feature.reader.presentation.ReaderViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val readerModule = module {
    singleOf(::ReaderRepositoryImpl) bind ReaderRepository::class
    viewModel { params -> ReaderViewModel(get(), params.get()) }
}