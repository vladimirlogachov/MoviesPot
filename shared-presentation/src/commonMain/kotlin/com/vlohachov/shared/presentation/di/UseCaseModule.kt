package com.vlohachov.shared.presentation.di

import com.vlohachov.shared.domain.usecase.DiscoverMovies
import com.vlohachov.shared.domain.usecase.LoadGenres
import com.vlohachov.shared.domain.usecase.SearchMovies
import com.vlohachov.shared.domain.usecase.credits.LoadCast
import com.vlohachov.shared.domain.usecase.credits.LoadCrew
import com.vlohachov.shared.domain.usecase.credits.LoadDirector
import com.vlohachov.shared.domain.usecase.movie.LoadCredits
import com.vlohachov.shared.domain.usecase.movie.LoadDetails
import com.vlohachov.shared.domain.usecase.movie.LoadKeywords
import com.vlohachov.shared.domain.usecase.movie.LoadMoviesByCategory
import com.vlohachov.shared.domain.usecase.movie.LoadRecommendations
import com.vlohachov.shared.domain.usecase.settings.ApplyDynamicTheme
import com.vlohachov.shared.domain.usecase.settings.LoadSettings
import org.koin.dsl.module

internal val useCaseModule = module {

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
