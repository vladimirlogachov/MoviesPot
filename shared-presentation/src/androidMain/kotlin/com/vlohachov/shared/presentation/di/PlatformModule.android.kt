package com.vlohachov.shared.presentation.di

import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

internal actual val platformModule = module {
    single(createdAtStart = true) {
        OkHttp.create()
    }
}
