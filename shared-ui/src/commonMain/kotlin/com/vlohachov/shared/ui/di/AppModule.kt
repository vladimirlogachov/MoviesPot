package com.vlohachov.shared.ui.di

import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

internal val appModule = module {
    single<CoroutineContext> {
        Dispatchers.Main
    }
}
