package com.vlohachov.shared.presentation.ui.screen.main

import androidx.compose.runtime.Stable
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.presentation.core.ViewState

@Stable
internal data class MainViewState(
    val upcomingViewState: ViewState<List<Movie>> = ViewState.Loading,
    val nowPlayingViewState: ViewState<List<Movie>> = ViewState.Loading,
    val popularViewState: ViewState<List<Movie>> = ViewState.Loading,
    val topRatedViewState: ViewState<List<Movie>> = ViewState.Loading,
    val error: Throwable? = null,
)
