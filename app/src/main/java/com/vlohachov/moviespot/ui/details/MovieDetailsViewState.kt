package com.vlohachov.moviespot.ui.details

import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.model.movie.MovieDetails
import com.vlohachov.moviespot.core.ViewState

data class MovieDetailsViewState(
    val detailsViewState: ViewState<MovieDetails> = ViewState.Loading,
    val recommendationsViewState: ViewState<List<Movie>> = ViewState.Loading,
    val error: Throwable? = null,
)
