package com.vlohachov.moviespot.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.movie.MovieDetails
import com.vlohachov.domain.usecase.movies.MovieDetailsUseCase
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.core.WhileUiSubscribed
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    movieId: Long,
    movieDetails: MovieDetailsUseCase,
) : ViewModel() {

    private val error = MutableStateFlow<Throwable?>(value = null)

    private val detailsResult: Flow<Result<MovieDetails>> =
        movieDetails.resultFlow(param = MovieDetailsUseCase.Param(id = movieId))

    val uiState: StateFlow<MovieDetailsViewState> = combine(
        detailsResult,
        error,
    ) { detailsResult, error ->
        val detailsViewState = when (detailsResult) {
            Result.Loading -> ViewState.Loading
            is Result.Error -> ViewState.Error(error = detailsResult.exception)
            is Result.Success -> ViewState.Success(data = detailsResult.value)
        }

        MovieDetailsViewState(
            detailsViewState = detailsViewState,
            error = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = MovieDetailsViewState(),
    )

    fun onError(error: Throwable) {
        viewModelScope.launch { this@MovieDetailsViewModel.error.emit(value = error) }
    }

    fun onErrorConsumed() {
        viewModelScope.launch { this@MovieDetailsViewModel.error.emit(value = null) }
    }
}