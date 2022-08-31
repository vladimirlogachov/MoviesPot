package com.vlohachov.moviespot.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.usecase.movies.list.NowPlayingUseCase
import com.vlohachov.domain.usecase.movies.list.PopularUseCase
import com.vlohachov.domain.usecase.movies.list.TopRatedUseCase
import com.vlohachov.domain.usecase.movies.list.UpcomingUseCase
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.core.WhileUiSubscribed
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    upcoming: UpcomingUseCase,
    nowPlaying: NowPlayingUseCase,
    popular: PopularUseCase,
    topRated: TopRatedUseCase,
) : ViewModel() {

    private val error = MutableStateFlow<Throwable?>(value = null)

    private val upcomingResult: Flow<Result<PaginatedData<Movie>>> =
        upcoming.resultFlow(param = UpcomingUseCase.Param())

    private val nowPlayingResult: Flow<Result<PaginatedData<Movie>>> =
        nowPlaying.resultFlow(param = NowPlayingUseCase.Param())

    private val popularResult: Flow<Result<PaginatedData<Movie>>> =
        popular.resultFlow(param = PopularUseCase.Param())

    private val topRatedResult: Flow<Result<PaginatedData<Movie>>> =
        topRated.resultFlow(param = TopRatedUseCase.Param())

    val uiState: StateFlow<MainViewState> = combine(
        upcomingResult,
        nowPlayingResult,
        popularResult,
        topRatedResult,
        error,
    ) { upcomingResult, nowPlayingResult, popularResult, topRatedResult, error ->
        val upcomingViewState = when (upcomingResult) {
            Result.Loading ->
                ViewState.Loading
            is Result.Error ->
                ViewState.Error(error = upcomingResult.exception)
            is Result.Success ->
                ViewState.Success(data = upcomingResult.value.data)
        }
        val nowPlayingViewState = when (nowPlayingResult) {
            Result.Loading ->
                ViewState.Loading
            is Result.Error ->
                ViewState.Error(error = nowPlayingResult.exception)
            is Result.Success ->
                ViewState.Success(data = nowPlayingResult.value.data)
        }
        val popularViewState = when (popularResult) {
            Result.Loading ->
                ViewState.Loading
            is Result.Error ->
                ViewState.Error(error = popularResult.exception)
            is Result.Success ->
                ViewState.Success(data = popularResult.value.data)
        }
        val topRatedViewState = when (topRatedResult) {
            Result.Loading ->
                ViewState.Loading
            is Result.Error ->
                ViewState.Error(error = topRatedResult.exception)
            is Result.Success ->
                ViewState.Success(data = topRatedResult.value.data)
        }

        MainViewState(
            upcomingViewState = upcomingViewState,
            nowPlayingViewState = nowPlayingViewState,
            popularViewState = popularViewState,
            topRatedViewState = topRatedViewState,
            error = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = MainViewState(),
    )

    fun onErrorConsumed() {
        viewModelScope.launch { error.emit(value = null) }
    }
}