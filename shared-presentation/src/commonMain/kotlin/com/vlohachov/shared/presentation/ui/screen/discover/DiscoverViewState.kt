package com.vlohachov.shared.presentation.ui.screen.discover

import androidx.compose.runtime.Immutable
import com.vlohachov.shared.domain.model.genre.Genre

@Immutable
internal data class DiscoverViewState(
    val year: String = "",
    val genres: List<Genre> = emptyList(),
    val selectedGenres: List<Genre> = listOf(),
    val showProgress: Boolean = false,
    val error: Throwable? = null,
) {
    val discoverEnabled: Boolean get() = year.isNotEmpty() || selectedGenres.isNotEmpty()
}
