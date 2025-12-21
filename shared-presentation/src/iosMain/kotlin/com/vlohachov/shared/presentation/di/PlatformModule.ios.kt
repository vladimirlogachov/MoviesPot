package com.vlohachov.shared.presentation.di

import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

internal actual val platformModule = module {
    single(createdAtStart = true) {
        Darwin.create()
    }
}
