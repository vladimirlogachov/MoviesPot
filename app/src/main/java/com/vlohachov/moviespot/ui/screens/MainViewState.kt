package com.vlohachov.moviespot.ui.screens

data class MainViewState(
    val isLoading: Boolean = false,
    val topRatedViewState: MoviesViewState = MoviesViewState(),
    val error: Throwable? = null,
)
