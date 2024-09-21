package com.vlohachov.shared.presentation.di

import org.koin.core.module.Module

internal val appComponent: List<Module> = listOf(
    appModule,
    dataModule,
    useCaseModule,
    viewModelModule,
)
