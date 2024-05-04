package com.vlohachov.shared.ui.di

import com.vlohachov.shared.ui.screen.discover.DiscoverViewModel
import com.vlohachov.shared.ui.screen.main.MainViewModel
import com.vlohachov.shared.ui.screen.settings.SettingsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val viewModelModule = module {
    singleOf(::MainViewModel)
    singleOf(::SettingsViewModel)
    singleOf(::DiscoverViewModel)
//    viewModel { params ->
//        MoviesViewModel(category = params.get(), pager = MoviesPager(useCase = get()))
//    }
//
//    viewModel { params ->
//        MovieDetailsViewModel(
//            movieId = params.get(),
//            loadDetails = get(),
//            loadDirector = get(),
//            loadKeywords = get(),
//            loadRecommendations = get(),
//        )
//    }
//
//    viewModel { params ->
//        CastViewModel(
//            movieId = params.get(),
//            loadCast = get(),
//        )
//    }
//
//    viewModel { params ->
//        CrewViewModel(
//            movieId = params.get(),
//            loadCrew = get(),
//        )
//    }
//
//    viewModel { params ->
//        SimilarMoviesViewModel(
//            pager = SimilarMoviesPager(
//                movieId = params.get(),
//                useCase = get(),
//            )
//        )
//    }
//
//    viewModel {
//        SearchMoviesViewModel(pager = SearchMoviesPager(useCase = get()))
//    }
//
//    viewModel {
//        DiscoverViewModel(loadGenres = get())
//    }
//
//    viewModel { params ->
//        DiscoverResultViewModel(
//            pager = DiscoverResultPager(
//                year = params.getOrNull(),
//                selectedGenres = params.get(),
//                useCase = get(),
//            )
//        )
//    }
//
//    viewModel { params ->
//        KeywordMoviesViewModel(
//            pager = KeywordMoviesPager(
//                keywordId = params.get(),
//                useCase = get(),
//            )
//        )
//    }

}
