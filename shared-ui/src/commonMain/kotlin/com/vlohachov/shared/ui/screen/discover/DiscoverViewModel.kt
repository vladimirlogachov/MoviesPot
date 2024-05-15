package com.vlohachov.shared.ui.screen.discover

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.shared.core.WhileUiSubscribed
import com.vlohachov.shared.core.toViewState
import com.vlohachov.shared.domain.model.genre.Genre
import com.vlohachov.shared.domain.usecase.LoadGenres
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@Stable
internal class DiscoverViewModel(loadGenres: LoadGenres) : ViewModel() {

    private companion object {
        const val YearInputLength = 4
    }

    private val selectedGenres = MutableStateFlow<List<Genre>>(value = listOf())
    private val error = MutableStateFlow<Throwable?>(value = null)
    private val year = MutableStateFlow(value = "")

    val uiState: StateFlow<DiscoverViewState> = combine(
        year,
        selectedGenres,
        loadGenres(param = LoadGenres.Param()),
        error,
    ) { year, selectedGenres, genres, error ->
        DiscoverViewState(
            year = year,
            genresViewState = genres.toViewState(),
            selectedGenres = selectedGenres,
            discoverEnabled = year.isNotBlank() || selectedGenres.isNotEmpty(),
            error = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = DiscoverViewState(),
    )

    fun onYear(year: String) {
        this.year.tryEmit(value = year.take(n = YearInputLength))
    }

    fun onSelect(genre: Genre) {
        selectedGenres.update { genres -> genres + genre }
    }

    fun onClearSelection(genre: Genre) {
        selectedGenres.update { genres -> genres - genre }
    }

    fun onError(error: Throwable) {
        this.error.tryEmit(value = error)
    }

    fun onErrorConsumed() {
        error.tryEmit(value = null)
    }

}
