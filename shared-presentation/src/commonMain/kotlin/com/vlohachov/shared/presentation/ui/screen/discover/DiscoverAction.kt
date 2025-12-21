package com.vlohachov.shared.presentation.ui.screen.discover

import com.vlohachov.shared.domain.model.genre.Genre

internal sealed interface DiscoverAction {
    data class EnterYear(val year: String) : DiscoverAction
    data class SelectGenre(val genre: Genre) : DiscoverAction
    data class RemoveGenre(val genre: Genre) : DiscoverAction
    object Back : DiscoverAction
    object Discover : DiscoverAction
    object HideError : DiscoverAction
}
