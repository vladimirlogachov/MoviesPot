package com.vlohachov.moviespot.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.domain.model.genre.Genre
import com.vlohachov.domain.usecase.DiscoverMoviesUseCase
import com.vlohachov.moviespot.core.WhileUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DiscoverViewModel(discoverMovies: DiscoverMoviesUseCase) : ViewModel() {

    private companion object Constants {
        const val PageSize = 20
    }

    private val year = MutableStateFlow(value = "")

    private val genres = MutableStateFlow<List<Genre>>(value = listOf())

    private val error = MutableStateFlow<Throwable?>(value = null)

    val uiState: StateFlow<DiscoverViewState> = combine(
        year,
        genres,
        error,
    ) { year, genres, error ->

        DiscoverViewState(
            year = year,
            genres = genres,
            error = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = DiscoverViewState(),
    )

    fun onDiscover() {
        viewModelScope.launch {

        }
    }

    fun onYear(year: String) {
        viewModelScope.launch {
            this@DiscoverViewModel.year.emit(value = year)
        }
    }

    fun onGenres(genres: List<Genre>) {
        viewModelScope.launch {
            this@DiscoverViewModel.genres.emit(value = genres)
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