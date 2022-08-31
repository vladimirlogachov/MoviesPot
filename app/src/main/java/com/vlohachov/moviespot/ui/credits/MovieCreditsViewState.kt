package com.vlohachov.moviespot.ui.credits

import com.vlohachov.domain.model.movie.MovieCredits
import com.vlohachov.moviespot.core.ViewState

data class MovieCreditsViewState(
    val creditsViewState: ViewState<MovieCredits> = ViewState.Loading,
    val error: Throwable? = null,
)
