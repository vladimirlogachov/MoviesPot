package com.vlohachov.moviespot.di

import android.content.Context
import com.vlohachov.data.local.LocalPreferences
import com.vlohachov.data.remote.TmdbConfig
import com.vlohachov.data.remote.api.TmdbDiscoverApi
import com.vlohachov.data.remote.api.TmdbGenreApi
import com.vlohachov.data.remote.api.TmdbMovieApi
import com.vlohachov.data.remote.api.TmdbSearchApi
import com.vlohachov.data.repository.DiscoverRepositoryImpl
import com.vlohachov.data.repository.GenreRepositoryImpl
import com.vlohachov.data.repository.MovieRepositoryImpl
import com.vlohachov.data.repository.SearchRepositoryImpl
import com.vlohachov.data.repository.SettingsRepositoryImpl
import com.vlohachov.domain.repository.DiscoverRepository
import com.vlohachov.domain.repository.GenreRepository
import com.vlohachov.domain.repository.MovieRepository
import com.vlohachov.domain.repository.SearchRepository
import com.vlohachov.domain.repository.SettingsRepository
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

private const val CACHE_MAX_SIZE = 20L * 1024 * 1024

val dataModule = module {
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        Cache(directory = get<Context>().cacheDir, maxSize = CACHE_MAX_SIZE)
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .cache(get())
            .build()
    }

    single {
        GsonConverterFactory.create()
    }

    single {
        Retrofit.Builder()
            .baseUrl(TmdbConfig.BASE_URL)
            .addConverterFactory(get<GsonConverterFactory>())
            .client(get())
            .build()
    }

    single<TmdbDiscoverApi> {
        get<Retrofit>().create()
    }

    single<TmdbGenreApi> {
        get<Retrofit>().create()
    }

    single<TmdbMovieApi> {
        get<Retrofit>().create()
    }

    single<TmdbSearchApi> {
        get<Retrofit>().create()
    }

    single<DiscoverRepository> {
        DiscoverRepositoryImpl(remote = get())
    }

    single<GenreRepository> {
        GenreRepositoryImpl(remote = get())
    }

    single<MovieRepository> {
        MovieRepositoryImpl(remote = get())
    }

    single<SearchRepository> {
        SearchRepositoryImpl(remote = get())
    }

    single { LocalPreferences(context = get()) }

    single<SettingsRepository> {
        SettingsRepositoryImpl(preferences = get())
    }
}
