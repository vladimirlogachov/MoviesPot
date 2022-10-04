package com.vlohachov.moviespot.di

import com.vlohachov.moviespot.ui.credits.cast.CastViewModel
import com.vlohachov.moviespot.ui.credits.crew.CrewViewModel
import com.vlohachov.moviespot.ui.details.MovieDetailsViewModel
import com.vlohachov.moviespot.ui.discover.DiscoverViewModel
import com.vlohachov.moviespot.ui.discover.result.DiscoverResultPager
import com.vlohachov.moviespot.ui.discover.result.DiscoverResultViewModel
import com.vlohachov.moviespot.ui.keyword.KeywordMoviesPager
import com.vlohachov.moviespot.ui.keyword.KeywordMoviesViewModel
import com.vlohachov.moviespot.ui.main.MainViewModel
import com.vlohachov.moviespot.ui.movies.now.NowPlayingMoviesPager
import com.vlohachov.moviespot.ui.movies.now.NowPlayingMoviesViewModel
import com.vlohachov.moviespot.ui.movies.popular.PopularMoviesPager
import com.vlohachov.moviespot.ui.movies.popular.PopularMoviesViewModel
import com.vlohachov.moviespot.ui.movies.similar.SimilarMoviesPager
import com.vlohachov.moviespot.ui.movies.similar.SimilarMoviesViewModel
import com.vlohachov.moviespot.ui.movies.top.TopRatedMoviesPager
import com.vlohachov.moviespot.ui.movies.top.TopRatedMoviesViewModel
import com.vlohachov.moviespot.ui.movies.upcoming.UpcomingMoviesPager
import com.vlohachov.moviespot.ui.movies.upcoming.UpcomingMoviesViewModel
import com.vlohachov.moviespot.ui.search.SearchMoviesPager
import com.vlohachov.moviespot.ui.search.SearchMoviesViewModel
import com.vlohachov.moviespot.ui.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MainViewModel(
            upcoming = get(),
            nowPlaying = get(),
            popular = get(),
            topRated = get(),
        )
    }

    viewModel {
        UpcomingMoviesViewModel(pager = UpcomingMoviesPager(useCase = get()))
    }

    viewModel {
        NowPlayingMoviesViewModel(pager = NowPlayingMoviesPager(useCase = get()))
    }

    viewModel {
        PopularMoviesViewModel(pager = PopularMoviesPager(useCase = get()))
    }

    viewModel {
        TopRatedMoviesViewModel(pager = TopRatedMoviesPager(useCase = get()))
    }

    viewModel { params ->
        MovieDetailsViewModel(
            movieId = params.get(),
            movieDetails = get(),
            director = get(),
            keywords = get(),
            movieRecommendations = get(),
        )
    }

    viewModel { params ->
        CastViewModel(
            movieId = params.get(),
            cast = get(),
        )
    }

    viewModel { params ->
        CrewViewModel(
            movieId = params.get(),
            crew = get(),
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
        DiscoverViewModel(useCase = get())
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
