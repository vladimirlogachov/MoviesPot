package com.vlohachov.moviespot.di

import com.vlohachov.data.repository.MoviesRepositoryImpl
import com.vlohachov.domain.repository.MoviesRepository
import com.vlohachov.domain.usecase.*
import org.koin.dsl.module

val moviesModule = module {
    single<MoviesRepository> {
        MoviesRepositoryImpl(remote = get())
    }

    single {
        GenresUseCase(
            coroutineContext = get(),
            repository = get(),
        )
    }

    single {
        UpcomingUseCase(
            coroutineContext = get(),
            repository = get(),
        )
    }

    single {
        NowPlayingUseCase(
            coroutineContext = get(),
            repository = get(),
        )
    }

    single {
        PopularUseCase(
            coroutineContext = get(),
            repository = get(),
        )
    }

    single {
        TopRatedUseCase(
            coroutineContext = get(),
            repository = get(),
        )
    }
}