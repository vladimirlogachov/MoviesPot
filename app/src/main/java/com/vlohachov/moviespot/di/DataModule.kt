package com.vlohachov.moviespot.di

import androidx.room.Room
import com.vlohachov.data.local.LocalStore
import com.vlohachov.data.local.StoreConfig
import com.vlohachov.data.remote.TmdbApi
import com.vlohachov.data.remote.TmdbConfig
import com.vlohachov.data.repository.MoviesRepositoryImpl
import com.vlohachov.data.repository.SettingsRepositoryImpl
import com.vlohachov.domain.repository.MoviesRepository
import com.vlohachov.domain.repository.SettingsRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
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

    single<TmdbApi> {
        get<Retrofit>().create(TmdbApi::class.java)
    }

    single<MoviesRepository> {
        MoviesRepositoryImpl(remote = get())
    }

    single {
        Room.databaseBuilder(
            get(),
            LocalStore::class.java,
            StoreConfig.StoreName,
        ).fallbackToDestructiveMigration()
            .build()
    }

    single {
        get<LocalStore>().settingsDao()
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(dao = get())
    }
}
