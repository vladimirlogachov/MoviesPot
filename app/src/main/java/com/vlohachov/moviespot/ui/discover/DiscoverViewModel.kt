package com.vlohachov.moviespot.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.genre.Genre
import com.vlohachov.domain.usecase.GenresUseCase
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.core.WhileUiSubscribed
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DiscoverViewModel(useCase: GenresUseCase) : ViewModel() {

    private companion object Constants {
        const val YearInputLength = 4
    }

    private val year = MutableStateFlow(value = "")

    private val selectedGenres = MutableStateFlow<List<Genre>>(value = listOf())

    private val error = MutableStateFlow<Throwable?>(value = null)

    private val genresResult: Flow<Result<List<Genre>>> =
        useCase.resultFlow(param = GenresUseCase.Param())

    val uiState: StateFlow<DiscoverViewState> = combine(
        year,
        genresResult,
        selectedGenres,
        error,
    ) { year, genresResult, selectedGenres, error ->
        val genresViewState = when (genresResult) {
            Result.Loading -> ViewState.Loading
            is Result.Error -> ViewState.Error(error = genresResult.exception)
            is Result.Success -> ViewState.Success(data = genresResult.value)
        }

        DiscoverViewState(
            year = year,
            genresViewState = genresViewState,
            selectedGenres = selectedGenres,
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