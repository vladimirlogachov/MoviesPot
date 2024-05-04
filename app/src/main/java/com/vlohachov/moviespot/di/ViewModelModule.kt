package com.vlohachov.moviespot.di

import com.vlohachov.moviespot.ui.credits.cast.CastViewModel
import com.vlohachov.moviespot.ui.credits.crew.CrewViewModel
import com.vlohachov.moviespot.ui.details.MovieDetailsViewModel
import com.vlohachov.moviespot.ui.discover.result.DiscoverResultPager
import com.vlohachov.moviespot.ui.discover.result.DiscoverResultViewModel
import com.vlohachov.moviespot.ui.keyword.KeywordMoviesPager
import com.vlohachov.moviespot.ui.keyword.KeywordMoviesViewModel
import com.vlohachov.moviespot.ui.main.MainViewModel
import com.vlohachov.moviespot.ui.movies.MoviesPager
import com.vlohachov.moviespot.ui.movies.MoviesViewModel
import com.vlohachov.moviespot.ui.movies.similar.SimilarMoviesPager
import com.vlohachov.moviespot.ui.movies.similar.SimilarMoviesViewModel
import com.vlohachov.moviespot.ui.search.SearchMoviesPager
import com.vlohachov.moviespot.ui.search.SearchMoviesViewModel
import com.vlohachov.moviespot.ui.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MainViewModel(loadMoviesByCategory = get())
    }

    viewModel { params ->
        MoviesViewModel(category = params.get(), pager = MoviesPager(useCase = get()))
    }

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
        SimilarMoviesViewModel(
            pager = SimilarMoviesPager(
                movieId = params.get(),
                useCase = get(),
            )
        )
    }

    viewModel {
        SearchMoviesViewModel(pager = SearchMoviesPager(useCase = get()))
    }

    viewModel {
        com.vlohachov.moviespot.ui.discover.DiscoverViewModel(loadGenres = get())
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

    viewModel { params ->
        KeywordMoviesViewModel(
            pager = KeywordMoviesPager(
                keywordId = params.get(),
                useCase = get(),
            )
        )
    }

    viewModel {
        SettingsViewModel(
            getSettings = get(),
            applyDynamicTheme = get(),
        )
    }
}
