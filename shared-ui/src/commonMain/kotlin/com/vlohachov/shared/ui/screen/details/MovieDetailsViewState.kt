package com.vlohachov.shared.ui.screen.details

import androidx.compose.runtime.Stable
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.model.movie.MovieDetails
import com.vlohachov.shared.domain.model.movie.keyword.Keyword
import com.vlohachov.shared.ui.state.ViewState

@Stable
internal data class MovieDetailsViewState(
    val detailsViewState: ViewState<MovieDetails> = ViewState.Loading,
    val directorViewState: ViewState<String> = ViewState.Loading,
    val keywordsViewState: ViewState<List<Keyword>> = ViewState.Loading,
    val recommendationsViewState: ViewState<List<Movie>> = ViewState.Loading,
    val error: Throwable? = null,
)
