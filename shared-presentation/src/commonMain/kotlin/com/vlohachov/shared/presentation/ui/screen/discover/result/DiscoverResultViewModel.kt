package com.vlohachov.shared.presentation.ui.screen.discover.result

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Stable
internal class DiscoverResultViewModel(pager: DiscoverResultPager) : ViewModel() {

    private val _error = MutableStateFlow<Throwable?>(value = null)

    val error: StateFlow<Throwable?> = _error
    val movies = pager.pagingDataFlow.cachedIn(scope = viewModelScope)

    fun onError(error: Throwable) {
        _error.tryEmit(value = error)
    }

    fun onErrorConsumed() {
        _error.tryEmit(value = null)
    }

}
