package com.vlohachov.shared.presentation.di

import com.russhwolf.settings.Settings
import com.vlohachov.shared.data.local.LocalPreferences
import com.vlohachov.shared.data.repository.LocalSettingsRepository
import com.vlohachov.shared.data.repository.RemoteDiscoverRepository
import com.vlohachov.shared.data.repository.RemoteGenreRepository
import com.vlohachov.shared.data.repository.RemoteMovieRepository
import com.vlohachov.shared.data.repository.RemoteSearchRepository
import com.vlohachov.shared.domain.repository.DiscoverRepository
import com.vlohachov.shared.domain.repository.GenreRepository
import com.vlohachov.shared.domain.repository.MovieRepository
import com.vlohachov.shared.domain.repository.SearchRepository
import com.vlohachov.shared.domain.repository.SettingsRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val dataModule = module {
    single(createdAtStart = true) {
        HttpClient(engine = get()) {
            install(plugin = HttpCache)
            install(plugin = ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }
            install(plugin = Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
        }
    }
    singleOf(constructor = ::RemoteMovieRepository) {
        bind<MovieRepository>()
        createdAtStart()
    }
    singleOf(constructor = ::RemoteDiscoverRepository) {
        bind<DiscoverRepository>()
    }
    singleOf(constructor = ::RemoteDiscoverRepository) {
        bind<DiscoverRepository>()
    }
    singleOf(constructor = ::RemoteGenreRepository) {
        bind<GenreRepository>()
    }
    singleOf(constructor = ::RemoteSearchRepository) {
        bind<SearchRepository>()
    }
    singleOf(constructor = ::LocalSettingsRepository) {
        bind<SettingsRepository>()
    }
    singleOf(constructor = ::LocalPreferences)
//    singleOf(constructor = ::Settings)
    single {
        Settings()
    }
}
