package com.vlohachov.moviespot.ui.main

import com.vlohachov.domain.model.genre.Genre
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.ui.movies.MoviesSection

data class MainViewState(
    val isLoading: Boolean = false,
    val moviesViewStates: Map<MoviesSection, ViewState<List<Movie>>> = emptyMap(),
    val genresViewState: ViewState<List<Genre>> = ViewState.Loading,
    val error: Throwable? = null,
)
