package com.vlohachov.moviespot.ui.movies

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.vlohachov.shared.domain.model.movie.MovieCategory
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MoviesViewModel(category: MovieCategory, pager: MoviesPager) : ViewModel() {

    val movies = pager.pagingDataFlow(category = category)
        .catch { error -> onError(error = error) }
        .cachedIn(scope = viewModelScope)

    var error by mutableStateOf<Throwable?>(value = null)
        private set

    fun onError(error: Throwable) {
        viewModelScope.launch {
            this@MoviesViewModel.error = error
        }
    }

    fun onErrorConsumed() {
        viewModelScope.launch {
            error = null
        }
    }

}
