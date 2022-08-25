package com.vlohachov.moviespot.ui.screens

import com.vlohachov.domain.model.Movie

data class MoviesViewState(
    val isLoading: Boolean = false,
    val data: List<Movie> = listOf(),
    val error: Throwable? = null,
)
