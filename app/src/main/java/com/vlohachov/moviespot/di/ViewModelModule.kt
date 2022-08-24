package com.vlohachov.moviespot.di

import com.vlohachov.moviespot.ui.screens.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel(topRatedMovies = get())
    }
}