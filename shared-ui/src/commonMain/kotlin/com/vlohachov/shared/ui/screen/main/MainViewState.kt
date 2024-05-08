package com.vlohachov.shared.ui.screen.main

import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.core.ViewState

internal data class MainViewState(
    val upcomingViewState: ViewState<List<Movie>> = ViewState.Loading,
    val nowPlayingViewState: ViewState<List<Movie>> = ViewState.Loading,
    val popularViewState: ViewState<List<Movie>> = ViewState.Loading,
    val topRatedViewState: ViewState<List<Movie>> = ViewState.Loading,
    val error: Throwable? = null,
)
