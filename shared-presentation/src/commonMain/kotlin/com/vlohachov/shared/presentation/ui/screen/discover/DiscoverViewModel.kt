package com.vlohachov.shared.presentation.ui.screen.discover

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.shared.domain.model.genre.Genre
import com.vlohachov.shared.domain.usecase.LoadGenres
import com.vlohachov.shared.presentation.core.WhileUiSubscribed
import com.vlohachov.shared.presentation.core.toViewState
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

    private val _selectedGenres = MutableStateFlow<List<Genre>>(value = listOf())
    private val _error = MutableStateFlow<Throwable?>(value = null)
    private val _year = MutableStateFlow(value = "")

    val uiState: StateFlow<DiscoverViewState> = combine(
        _year,
        _selectedGenres,
        loadGenres(param = LoadGenres.Param()),
        _error,
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
        _year.tryEmit(value = year.take(n = YearInputLength))
    }

    fun onSelect(genre: Genre) {
        _selectedGenres.update { genres -> genres + genre }
    }

    fun onClearSelection(genre: Genre) {
        _selectedGenres.update { genres -> genres - genre }
    }

    fun onError(error: Throwable) {
        _error.tryEmit(value = error)
    }

    fun onErrorConsumed() {
        _error.tryEmit(value = null)
    }

}
