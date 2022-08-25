package com.vlohachov.moviespot.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.Movie
import com.vlohachov.domain.usecase.TopRatedMoviesUseCase
import com.vlohachov.moviespot.core.WhileUiSubscribed
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    topRatedMovies: TopRatedMoviesUseCase
) : ViewModel() {

    private var topRatedViewState by mutableStateOf(value = MoviesViewState())

    private val isLoading = MutableStateFlow(value = false)

    private val error = MutableStateFlow<Throwable?>(value = null)

    private val topRatedResult: Flow<Result<List<Movie>>> =
        topRatedMovies.resultFlow(param = TopRatedMoviesUseCase.Param())

    val uiState: StateFlow<MainViewState> = combine(
        topRatedResult,
        isLoading,
        error,
    ) { topRatedResult, isLoading, error ->

        topRatedViewState = when (topRatedResult) {
            is Result.Error ->
                topRatedViewState.copy(
                    isLoading = false,
                    error = topRatedResult.exception,
                )
            Result.Loading ->
                topRatedViewState.copy(
                    isLoading = true,
                    error = null,
                )
            is Result.Success ->
                topRatedViewState.copy(
                    isLoading = false,
                    data = topRatedResult.data,
                    error = null,
                )
        }

        MainViewState(
            topRatedViewState = topRatedViewState,
            isLoading = isLoading,
            error = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = MainViewState(),
    )

    fun onRefresh() {
        viewModelScope.launch {

        }
    }

    fun onErrorConsumed() {
        viewModelScope.launch { error.emit(value = null) }
    }
}