package com.vlohachov.shared.ui.screen.main

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.shared.core.WhileUiSubscribed
import com.vlohachov.shared.core.toViewStatePaginated
import com.vlohachov.shared.domain.model.movie.MovieCategory
import com.vlohachov.shared.domain.usecase.movie.LoadMoviesByCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@Stable
internal class MainViewModel(loadMoviesByCategory: LoadMoviesByCategory) : ViewModel() {

    private val _error = MutableStateFlow<Throwable?>(value = null)

    val error: StateFlow<Throwable?> = _error
    val uiState: StateFlow<MainViewState> = combine(
        loadMoviesByCategory(param = LoadMoviesByCategory.Param(category = MovieCategory.UPCOMING)),
        loadMoviesByCategory(param = LoadMoviesByCategory.Param(category = MovieCategory.NOW_PLAYING)),
        loadMoviesByCategory(param = LoadMoviesByCategory.Param(category = MovieCategory.POPULAR)),
        loadMoviesByCategory(param = LoadMoviesByCategory.Param(category = MovieCategory.TOP_RATED)),
        error,
    ) { upcoming, nowPlaying, popular, topRated, error ->
        MainViewState(
            upcomingViewState = upcoming.toViewStatePaginated(),
            nowPlayingViewState = nowPlaying.toViewStatePaginated(),
            popularViewState = popular.toViewStatePaginated(),
            topRatedViewState = topRated.toViewStatePaginated(),
            error = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = MainViewState(),
    )

    fun onError(error: Throwable) {
        _error.tryEmit(value = error)
    }

    fun onErrorConsumed() {
        _error.tryEmit(value = null)
    }

}
