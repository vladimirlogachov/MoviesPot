package com.vlohachov.moviespot.di

import com.vlohachov.moviespot.ui.credits.cast.CastViewModel
import com.vlohachov.moviespot.ui.credits.crew.CrewViewModel
import com.vlohachov.moviespot.ui.details.MovieDetailsViewModel
import com.vlohachov.moviespot.ui.discover.DiscoverViewModel
import com.vlohachov.moviespot.ui.discover.result.DiscoverResultViewModel
import com.vlohachov.moviespot.ui.main.MainViewModel
import com.vlohachov.moviespot.ui.movies.now.NowPlayingMoviesViewModel
import com.vlohachov.moviespot.ui.movies.popular.PopularMoviesViewModel
import com.vlohachov.moviespot.ui.movies.similar.SimilarMoviesViewModel
import com.vlohachov.moviespot.ui.movies.top.TopRatedMoviesViewModel
import com.vlohachov.moviespot.ui.movies.upcoming.UpcomingMoviesViewModel
import com.vlohachov.moviespot.ui.search.SearchViewModel
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
        UpcomingMoviesViewModel(useCase = get())
    }

    viewModel {
        NowPlayingMoviesViewModel(useCase = get())
    }

    viewModel {
        PopularMoviesViewModel(useCase = get())
    }

    viewModel {
        TopRatedMoviesViewModel(useCase = get())
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
            movieId = params.get(),
            useCase = get(),
        )
    }

    viewModel {
        SearchViewModel(useCase = get())
    }

    viewModel {
        DiscoverViewModel(useCase = get())
    }

    viewModel { params ->
        DiscoverResultViewModel(
            param = params.get(),
            useCase = get(),
        )
    }
}