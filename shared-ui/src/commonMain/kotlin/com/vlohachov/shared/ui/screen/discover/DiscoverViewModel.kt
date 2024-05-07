package com.vlohachov.shared.ui.screen.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.shared.domain.model.genre.Genre
import com.vlohachov.shared.domain.usecase.LoadGenres
import com.vlohachov.shared.ui.core.WhileUiSubscribed
import com.vlohachov.shared.ui.state.toViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class DiscoverViewModel(loadGenres: LoadGenres) : ViewModel() {

    private companion object Constants {
        const val YearInputLength = 4
    }

    private val year = MutableStateFlow(value = "")

    private val selectedGenres = MutableStateFlow<List<Genre>>(value = listOf())

    private val error = MutableStateFlow<Throwable?>(value = null)

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
        viewModelScope.launch {
            this@DiscoverViewModel.year.emit(value = year.take(n = YearInputLength))
        }
    }

    fun onSelect(genre: Genre) {
        viewModelScope.launch {
            this@DiscoverViewModel.selectedGenres.update { genres -> genres + genre }
        }
    }

    fun onClearSelection(genre: Genre) {
        viewModelScope.launch {
            this@DiscoverViewModel.selectedGenres.update { genres -> genres - genre }
        }
    }

    fun onError(error: Throwable) {
        viewModelScope.launch {
            this@DiscoverViewModel.error.emit(value = error)
        }
    }

    fun onErrorConsumed() {
        viewModelScope.launch {
            this@DiscoverViewModel.error.emit(value = null)
        }
    }

}
