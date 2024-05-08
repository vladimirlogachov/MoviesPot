package com.vlohachov.shared.ui.screen.movies.similar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

internal class SimilarMoviesViewModel(pager: SimilarMoviesPager) : ViewModel() {

    val movies = pager.pagingDataFlow
        .catch { error -> onError(error = error) }
        .cachedIn(scope = viewModelScope)

    var error by mutableStateOf<Throwable?>(value = null)
        private set

    fun onError(error: Throwable) {
        viewModelScope.launch {
            this@SimilarMoviesViewModel.error = error
        }
    }

    fun onErrorConsumed() {
        viewModelScope.launch {
            error = null
        }
    }

}
