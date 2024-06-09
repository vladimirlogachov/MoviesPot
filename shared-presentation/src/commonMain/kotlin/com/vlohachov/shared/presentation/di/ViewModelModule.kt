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
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::MovieDetailsViewModel)
    viewModelOf(::CastViewModel)
    viewModelOf(::CrewViewModel)
    viewModelOf(::DiscoverViewModel)
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
