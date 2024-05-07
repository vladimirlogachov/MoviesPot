package com.vlohachov.shared.ui.screen.search

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Stable
internal class MoviesSearchViewModel(pager: MoviesSearchPager) : ViewModel() {

    private val _search = MutableStateFlow(value = "")

    val search: Flow<String> = _search.onEach(pager::onQuery)

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
            this@MoviesSearchViewModel.error = error
        }
    }

    fun onErrorConsumed() {
        viewModelScope.launch {
            error = null
        }
    }

}
