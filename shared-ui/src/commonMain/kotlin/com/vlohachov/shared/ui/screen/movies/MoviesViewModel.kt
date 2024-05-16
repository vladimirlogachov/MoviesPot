package com.vlohachov.shared.ui.screen.movies

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.vlohachov.shared.domain.model.movie.MovieCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch

@Stable
internal class MoviesViewModel(category: MovieCategory, pager: MoviesPager) : ViewModel() {

    private val _error = MutableStateFlow<Throwable?>(value = null)

    val error: StateFlow<Throwable?> = _error
    val movies = pager.pagingDataFlow(category = category)
        .catch { error -> onError(error = error) }
        .cachedIn(scope = viewModelScope)

    fun onError(error: Throwable) {
        _error.tryEmit(value = error)
    }

    fun onErrorConsumed() {
        _error.tryEmit(value = null)
    }

}
