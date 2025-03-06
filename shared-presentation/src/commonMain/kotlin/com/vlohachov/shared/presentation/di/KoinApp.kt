package com.vlohachov.shared.presentation.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes

public fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        printLogger()
        includes(configurations = arrayOf(config))
        modules(modules = appComponent)
    }
}
