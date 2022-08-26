package com.vlohachov.moviespot.di

import com.vlohachov.moviespot.ui.genres.GenresViewModel
import com.vlohachov.moviespot.ui.main.MainViewModel
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
        GenresViewModel(genresUseCase = get())
    }
}