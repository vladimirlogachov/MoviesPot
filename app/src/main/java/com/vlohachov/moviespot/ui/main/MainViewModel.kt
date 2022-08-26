package com.vlohachov.moviespot.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.Movie
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.usecase.NowPlayingUseCase
import com.vlohachov.domain.usecase.PopularUseCase
import com.vlohachov.domain.usecase.TopRatedUseCase
import com.vlohachov.domain.usecase.UpcomingUseCase
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.core.WhileUiSubscribed
import com.vlohachov.moviespot.ui.movies.MoviesSection
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    upcoming: UpcomingUseCase,
    nowPlaying: NowPlayingUseCase,
    popular: PopularUseCase,
    topRated: TopRatedUseCase,
) : ViewModel() {

    private val isLoading = MutableStateFlow(value = false)

    private val error = MutableStateFlow<Throwable?>(value = null)

    private val upcomingResult: Flow<Result<PaginatedData<Movie>>> =
        upcoming.resultFlow(param = UpcomingUseCase.Param())

    private val nowPlayingResult: Flow<Result<PaginatedData<Movie>>> =
        nowPlaying.resultFlow(param = NowPlayingUseCase.Param())

    private val popularResult: Flow<Result<PaginatedData<Movie>>> =
        popular.resultFlow(param = PopularUseCase.Param())

    private val topRatedResult: Flow<Result<PaginatedData<Movie>>> =
        topRated.resultFlow(param = TopRatedUseCase.Param())

    private val moviesViewStates: StateFlow<Map<MoviesSection, ViewState<List<Movie>>>> = combine(
        upcomingResult,
        nowPlayingResult,
        popularResult,
        topRatedResult,
    ) { upcomingResult, nowPlayingResult, popularResult, topRatedResult ->
        val upcomingViewState: ViewState<List<Movie>> = when (upcomingResult) {
            Result.Loading ->
                ViewState.Loading
            is Result.Error ->
                ViewState.Error(error = upcomingResult.exception)
            is Result.Success ->
                ViewState.Success(data = upcomingResult.value.data)
        }
        val nowPlayingViewState: ViewState<List<Movie>> = when (nowPlayingResult) {
            Result.Loading ->
                ViewState.Loading
            is Result.Error ->
                ViewState.Error(error = nowPlayingResult.exception)
            is Result.Success ->
                ViewState.Success(data = nowPlayingResult.value.data)
        }
        val popularViewState: ViewState<List<Movie>> = when (popularResult) {
            Result.Loading ->
                ViewState.Loading
            is Result.Error ->
                ViewState.Error(error = popularResult.exception)
            is Result.Success ->
                ViewState.Success(data = popularResult.value.data)
        }
        val topRatedViewState: ViewState<List<Movie>> = when (topRatedResult) {
            Result.Loading ->
                ViewState.Loading
            is Result.Error ->
                ViewState.Error(error = topRatedResult.exception)
            is Result.Success ->
                ViewState.Success(data = topRatedResult.value.data)
        }
        mapOf(
            MoviesSection.Upcoming to upcomingViewState,
            MoviesSection.NowPlaying to nowPlayingViewState,
            MoviesSection.Popular to popularViewState,
            MoviesSection.TopRated to topRatedViewState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = emptyMap(),
    )

    val uiState: StateFlow<MainViewState> = combine(
        moviesViewStates,
        isLoading,
        error,
    ) { moviesViewStates, isLoading, error ->
        MainViewState(
            moviesViewStates = moviesViewStates,
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