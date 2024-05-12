package com.vlohachov.shared.ui.screen.search

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach

@Stable
internal class MoviesSearchViewModel(pager: MoviesSearchPager) : ViewModel() {

    private val _search = MutableStateFlow(value = "")
    private val _error = MutableStateFlow<Throwable?>(value = null)

    val search: Flow<String> = _search.onEach(pager::onQuery)
    val error: StateFlow<Throwable?> = _error
    val movies = pager.pagingDataFlow
        .catch { error -> onError(error = error) }
        .cachedIn(scope = viewModelScope)

    fun onClear() {
        _search.tryEmit(value = "")
    }

    fun onSearch(search: String) {
        _search.tryEmit(value = search)
    }

    fun onError(error: Throwable) {
        _error.tryEmit(value = error)
    }

    fun onErrorConsumed() {
        _error.tryEmit(value = null)
    }

}
