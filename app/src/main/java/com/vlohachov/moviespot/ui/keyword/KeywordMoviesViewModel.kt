package com.vlohachov.moviespot.ui.keyword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.launch

class KeywordMoviesViewModel(pager: KeywordMoviesPager) : ViewModel() {

    val movies = pager.pagingDataFlow.cachedIn(scope = viewModelScope)

    var error by mutableStateOf<Throwable?>(value = null)
        private set

    fun onError(error: Throwable) {
        viewModelScope.launch {
            this@KeywordMoviesViewModel.error = error
        }
    }

    fun onErrorConsumed() {
        viewModelScope.launch {
            this@KeywordMoviesViewModel.error = null
        }
    }

}
