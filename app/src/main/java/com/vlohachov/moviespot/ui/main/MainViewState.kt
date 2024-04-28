package com.vlohachov.moviespot.ui.main

import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.shared.domain.model.movie.Movie

data class MainViewState(
    val upcomingViewState: ViewState<List<Movie>> = ViewState.Loading,
    val nowPlayingViewState: ViewState<List<Movie>> = ViewState.Loading,
    val popularViewState: ViewState<List<Movie>> = ViewState.Loading,
    val topRatedViewState: ViewState<List<Movie>> = ViewState.Loading,
    val error: Throwable? = null,
)
