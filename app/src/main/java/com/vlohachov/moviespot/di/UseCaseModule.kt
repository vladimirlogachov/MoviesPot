package com.vlohachov.moviespot.di

import com.vlohachov.domain.usecase.DiscoverMovies
import com.vlohachov.domain.usecase.LoadGenres
import com.vlohachov.domain.usecase.SearchMovies
import com.vlohachov.domain.usecase.credits.LoadCast
import com.vlohachov.domain.usecase.credits.LoadCrew
import com.vlohachov.domain.usecase.credits.LoadDirector
import com.vlohachov.domain.usecase.movie.LoadCredits
import com.vlohachov.domain.usecase.movie.LoadDetails
import com.vlohachov.domain.usecase.movie.LoadKeywords
import com.vlohachov.domain.usecase.movie.LoadMoviesByCategory
import com.vlohachov.domain.usecase.movie.LoadRecommendations
import com.vlohachov.domain.usecase.settings.ApplyDynamicTheme
import com.vlohachov.domain.usecase.settings.LoadSettings
import org.koin.dsl.module

val useCaseModule = module {

    single {
        LoadGenres(repository = get())
    }

    single {
        LoadMoviesByCategory(repository = get())
    }

    single {
        LoadDetails(repository = get())
    }

    single {
        LoadCredits(repository = get())
    }

    single {
        LoadDirector(repository = get())
    }

    single {
        LoadCast(repository = get())
    }

    single {
        LoadCrew(repository = get())
    }

    single {
        LoadRecommendations(repository = get())
    }

    single {
        LoadKeywords(repository = get())
    }

    single {
        SearchMovies(repository = get())
    }

    single {
        DiscoverMovies(repository = get())
    }

    single {
        LoadSettings(repository = get())
    }

    single {
        ApplyDynamicTheme(repository = get())
    }
}
