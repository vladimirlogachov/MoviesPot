package com.vlohachov.shared.presentation.ui.screen.discover

internal sealed interface DiscoverEvent {
    data object NavigateBack : DiscoverEvent
    data class NavigateToResults(val year: Int?, val genres: List<Int>) : DiscoverEvent
}
