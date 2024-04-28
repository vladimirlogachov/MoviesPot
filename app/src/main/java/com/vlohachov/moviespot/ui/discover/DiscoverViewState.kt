package com.vlohachov.moviespot.ui.discover

import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.shared.domain.model.genre.Genre

data class DiscoverViewState(
    val year: String = "",
    val genresViewState: ViewState<List<Genre>> = ViewState.Loading,
    val selectedGenres: List<Genre> = listOf(),
    val discoverEnabled: Boolean = false,
    val error: Throwable? = null,
)
