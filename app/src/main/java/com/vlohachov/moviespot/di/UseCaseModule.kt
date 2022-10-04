package com.vlohachov.moviespot.di

import com.vlohachov.domain.usecase.DiscoverMoviesUseCase
import com.vlohachov.domain.usecase.GenresUseCase
import com.vlohachov.domain.usecase.SearchMoviesUseCase
import com.vlohachov.domain.usecase.credits.CastUseCase
import com.vlohachov.domain.usecase.credits.CrewUseCase
import com.vlohachov.domain.usecase.credits.DirectorUseCase
import com.vlohachov.domain.usecase.movie.MovieCreditsUseCase
import com.vlohachov.domain.usecase.movie.MovieDetailsUseCase
import com.vlohachov.domain.usecase.movie.MovieKeywordsUseCase
import com.vlohachov.domain.usecase.movie.MovieRecommendationsUseCase
import com.vlohachov.domain.usecase.movie.list.NowPlayingUseCase
import com.vlohachov.domain.usecase.movie.list.PopularUseCase
import com.vlohachov.domain.usecase.movie.list.TopRatedUseCase
import com.vlohachov.domain.usecase.movie.list.UpcomingUseCase
import com.vlohachov.domain.usecase.settings.ApplyDynamicThemeUseCase
import com.vlohachov.domain.usecase.settings.GetSettingsUseCase
import org.koin.dsl.module

val useCaseModule = module {

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

    single {
        SearchMoviesUseCase(
            coroutineContext = get(),
            repository = get(),
        )
    }

    single {
        DiscoverMoviesUseCase(
            coroutineContext = get(),
            repository = get(),
        )
    }

    single {
        GetSettingsUseCase(
            coroutineContext = get(),
            repository = get(),
        )
    }

    single {
        ApplyDynamicThemeUseCase(
            coroutineContext = get(),
            repository = get(),
        )
    }
}
