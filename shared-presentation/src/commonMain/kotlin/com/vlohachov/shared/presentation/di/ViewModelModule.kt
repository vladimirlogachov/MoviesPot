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
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val viewModelModule = module {
    singleOf(::MainViewModel)
    singleOf(::SettingsViewModel)
    factoryOf(::MovieDetailsViewModel)
    factoryOf(::CastViewModel)
    factoryOf(::CrewViewModel)
    factoryOf(::DiscoverViewModel)
    factory { params ->
        DiscoverResultViewModel(
            pager = DiscoverResultPager(
                year = params.getOrNull(),
                selectedGenres = params.get(),
                useCase = get(),
            )
        )
    }
    factory {
        MoviesSearchViewModel(
            pager = MoviesSearchPager(useCase = get()),
        )
    }
    factory { params ->
        MoviesViewModel(
            category = params.get(),
            pager = MoviesPager(useCase = get()),
        )
    }
    factory { params ->
        SimilarMoviesViewModel(
            pager = SimilarMoviesPager(
                movieId = params.get(),
                useCase = get(),
            )
        )
    }
    factory { params ->
        KeywordMoviesViewModel(
            pager = KeywordMoviesPager(
                keywordId = params.get(),
                useCase = get(),
            )
        )
    }
}
