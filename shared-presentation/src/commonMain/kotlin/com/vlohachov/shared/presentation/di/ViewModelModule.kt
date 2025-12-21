package com.vlohachov.shared.presentation.di

import com.vlohachov.shared.presentation.ui.screen.credits.cast.CastViewModel
import com.vlohachov.shared.presentation.ui.screen.credits.crew.CrewViewModel
import com.vlohachov.shared.presentation.ui.screen.details.MovieDetailsViewModel
import com.vlohachov.shared.presentation.ui.screen.discover.DiscoverViewModel
import com.vlohachov.shared.presentation.ui.screen.discover.result.DiscoverResultPager
import com.vlohachov.shared.presentation.ui.screen.discover.result.DiscoverResultViewModel
import com.vlohachov.shared.presentation.ui.screen.keyword.KeywordMoviesPager
import com.vlohachov.shared.presentation.ui.screen.keyword.KeywordMoviesViewModel
import com.vlohachov.shared.presentation.ui.screen.main.MainViewModel
import com.vlohachov.shared.presentation.ui.screen.movies.MoviesPager
import com.vlohachov.shared.presentation.ui.screen.movies.MoviesViewModel
import com.vlohachov.shared.presentation.ui.screen.movies.similar.SimilarMoviesPager
import com.vlohachov.shared.presentation.ui.screen.movies.similar.SimilarMoviesViewModel
import com.vlohachov.shared.presentation.ui.screen.search.MoviesSearchPager
import com.vlohachov.shared.presentation.ui.screen.search.MoviesSearchViewModel
import com.vlohachov.shared.presentation.ui.screen.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val viewModelModule = module {
    viewModelOf(constructor = ::MainViewModel) {
//        createdAtStart()
    }
    viewModelOf(constructor = ::SettingsViewModel)
    viewModelOf(constructor = ::DiscoverViewModel)
    viewModel { params ->
        MovieDetailsViewModel(
            movieId = params.get(),
            loadDetails = get(),
            loadDirector = get(),
            loadKeywords = get(),
            loadRecommendations = get(),
        )
    }
    viewModel { params ->
        CastViewModel(
            movieId = params.get(),
            loadCast = get(),
        )
    }
    viewModel { params ->
        CrewViewModel(
            movieId = params.get(),
            loadCrew = get(),
        )
    }
    viewModel { params ->
        DiscoverResultViewModel(
            pager = DiscoverResultPager(
                year = params.getOrNull(),
                selectedGenres = params.get(),
                useCase = get(),
            )
        )
    }
    viewModel {
        MoviesSearchViewModel(
            pager = MoviesSearchPager(useCase = get()),
        )
    }
    viewModel { params ->
        MoviesViewModel(
            category = params.get(),
            pager = MoviesPager(useCase = get()),
        )
    }
    viewModel { params ->
        SimilarMoviesViewModel(
            pager = SimilarMoviesPager(
                movieId = params.get(),
                useCase = get(),
            )
        )
    }
    viewModel { params ->
        KeywordMoviesViewModel(
            pager = KeywordMoviesPager(
                keywordId = params.get(),
                useCase = get(),
            )
        )
    }
}
