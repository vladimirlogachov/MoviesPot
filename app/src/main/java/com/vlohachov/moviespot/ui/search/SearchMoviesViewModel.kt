package com.vlohachov.moviespot.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.vlohachov.moviespot.core.WhileUiSubscribed
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchMoviesViewModel(pager: SearchMoviesPager) : ViewModel() {

    private val _search = MutableStateFlow(value = "")

    val search: StateFlow<String> = _search
        .onEach(pager::onQuery)
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = "",
        )

    val movies = pager.pagingDataFlow
        .catch { error -> onError(error = error) }
        .cachedIn(viewModelScope)

    var error by mutableStateOf<Throwable?>(value = null)
        private set

    fun onClear() {
        viewModelScope.launch {
            _search.value = ""
        }
    }

    fun onSearch(search: String) {
        viewModelScope.launch {
            _search.value = search
        }
    }

    fun onError(error: Throwable) {
        viewModelScope.launch {
            this@SearchMoviesViewModel.error = error
        }
    }

    fun onErrorConsumed() {
        viewModelScope.launch {
            error = null
        }
    }
}