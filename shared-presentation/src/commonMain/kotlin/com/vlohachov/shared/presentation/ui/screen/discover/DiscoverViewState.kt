package com.vlohachov.shared.presentation.ui.screen.discover

import androidx.compose.runtime.Stable
import com.vlohachov.shared.domain.model.genre.Genre
import com.vlohachov.shared.presentation.core.ViewState

@Stable
internal data class DiscoverViewState(
    val year: String = "",
    val genresViewState: ViewState<List<Genre>> = ViewState.Loading,
    val selectedGenres: List<Genre> = listOf(),
    val discoverEnabled: Boolean = false,
    val error: Throwable? = null,
)
