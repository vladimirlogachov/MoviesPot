package com.vlohachov.moviespot.di

import com.vlohachov.data.repository.MoviesRepositoryImpl
import com.vlohachov.domain.repository.MoviesRepository
import com.vlohachov.domain.usecase.CastUseCase
import com.vlohachov.domain.usecase.CrewUseCase
import com.vlohachov.domain.usecase.DirectorUseCase
import com.vlohachov.domain.usecase.GenresUseCase
import com.vlohachov.domain.usecase.movie.MovieCreditsUseCase
import com.vlohachov.domain.usecase.movie.MovieDetailsUseCase
import com.vlohachov.domain.usecase.movie.MovieKeywordsUseCase
import com.vlohachov.domain.usecase.movie.MovieRecommendationsUseCase
import com.vlohachov.domain.usecase.movie.list.NowPlayingUseCase
import com.vlohachov.domain.usecase.movie.list.PopularUseCase
import com.vlohachov.domain.usecase.movie.list.TopRatedUseCase
import com.vlohachov.domain.usecase.movie.list.UpcomingUseCase
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

    single {
        MovieDetailsUseCase(
            coroutineContext = get(),
            repository = get(),
        )
    }

    single {
        MovieCreditsUseCase(
            coroutineContext = get(),
            repository = get(),
        )
    }

    single {
        DirectorUseCase(
            coroutineContext = get(),
            repository = get(),
        )
    }

    single {
        CastUseCase(
            coroutineContext = get(),
            repository = get(),
        )
    }

    single {
        CrewUseCase(
            coroutineContext = get(),
            repository = get(),
        )
    }

    single {
        MovieRecommendationsUseCase(
            coroutineContext = get(),
            repository = get(),
        )
    }

    single {
        MovieKeywordsUseCase(
            coroutineContext = get(),
            repository = get(),
        )
    }
}