package com.vlohachov.moviespot.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.moviespot.core.WhileUiSubscribed
import com.vlohachov.shared.domain.model.movie.MovieCategory
import com.vlohachov.shared.domain.usecase.movie.LoadMoviesByCategory
import com.vlohachov.shared.ui.state.toViewStatePaginated
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(loadMoviesByCategory: LoadMoviesByCategory) : ViewModel() {

    private val error = MutableStateFlow<Throwable?>(value = null)

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
        viewModelScope.launch { this@MainViewModel.error.emit(value = error) }
    }

    fun onErrorConsumed() {
        viewModelScope.launch { error.emit(value = null) }
    }

}
