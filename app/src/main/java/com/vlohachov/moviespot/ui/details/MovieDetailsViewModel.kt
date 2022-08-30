package com.vlohachov.moviespot.ui.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.movie.MovieDetails
import com.vlohachov.domain.usecase.movies.MovieDetailsUseCase
import com.vlohachov.moviespot.core.ViewState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    movieId: Long,
    movieDetails: MovieDetailsUseCase,
) : ViewModel() {

    var viewState by mutableStateOf<ViewState<MovieDetails>>(value = ViewState.Loading)
        private set

    init {
        movieDetails.resultFlow(param = MovieDetailsUseCase.Param(id = movieId))
            .onEach { result ->
                viewState = when (result) {
                    Result.Loading -> ViewState.Loading
                    is Result.Error -> ViewState.Error(error = result.exception)
                    is Result.Success -> ViewState.Success(data = result.value)
                }
            }.launchIn(scope = viewModelScope)
    }

    fun onErrorConsumed() {
        viewModelScope.launch {
            viewState = ViewState.Error(error = null)
        }
    }
}