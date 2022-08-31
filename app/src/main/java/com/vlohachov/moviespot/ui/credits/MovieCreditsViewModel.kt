package com.vlohachov.moviespot.ui.credits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.movie.MovieCredits
import com.vlohachov.domain.usecase.movies.MovieCreditsUseCase
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.core.WhileUiSubscribed
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MovieCreditsViewModel(
    movieId: Long,
    movieCredits: MovieCreditsUseCase,
) : ViewModel() {

    private val error = MutableStateFlow<Throwable?>(value = null)

    private val creditsResult: Flow<Result<MovieCredits>> =
        movieCredits.resultFlow(param = MovieCreditsUseCase.Param(id = movieId))

    val uiState: StateFlow<MovieCreditsViewState> = combine(
        creditsResult,
        error,
    ) { creditsResult, error ->
        val creditsViewState = when (creditsResult) {
            Result.Loading -> ViewState.Loading
            is Result.Error -> ViewState.Error(error = creditsResult.exception)
            is Result.Success -> ViewState.Success(data = creditsResult.value)
        }

        MovieCreditsViewState(
            creditsViewState = creditsViewState,
            error = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = MovieCreditsViewState(),
    )

    fun onError(error: Throwable) {
        viewModelScope.launch { this@MovieCreditsViewModel.error.emit(value = error) }
    }

    fun onErrorConsumed() {
        viewModelScope.launch { this@MovieCreditsViewModel.error.emit(value = null) }
    }
}