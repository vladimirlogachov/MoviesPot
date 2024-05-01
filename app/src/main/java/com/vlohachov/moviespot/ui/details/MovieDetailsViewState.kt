package com.vlohachov.moviespot.ui.details

import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.model.movie.MovieDetails
import com.vlohachov.shared.domain.model.movie.keyword.Keyword
import com.vlohachov.shared.ui.state.ViewState

data class MovieDetailsViewState(
    val detailsViewState: ViewState<MovieDetails> = ViewState.Loading,
    val directorViewState: ViewState<String> = ViewState.Loading,
    val keywordsViewState: ViewState<List<Keyword>> = ViewState.Loading,
    val recommendationsViewState: ViewState<List<Movie>> = ViewState.Loading,
    val error: Throwable? = null,
)
