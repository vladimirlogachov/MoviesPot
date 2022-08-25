package com.vlohachov.moviespot.di

import com.vlohachov.data.repository.MoviesRepositoryImpl
import com.vlohachov.domain.repository.MoviesRepository
import com.vlohachov.domain.usecase.TopRatedMoviesUseCase
import org.koin.dsl.module

val moviesModule = module {
    single<MoviesRepository> {
        MoviesRepositoryImpl(remote = get())
    }

    single {
        TopRatedMoviesUseCase(
            coroutineContext = get(),
            repository = get(),
        )
    }
}