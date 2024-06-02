package com.vlohachov.shared.presentation.di

import org.koin.core.module.Module

public val appComponent: List<Module> = listOf(
    appModule,
    dataModule,
    useCaseModule,
    viewModelModule,
)
