package com.vlohachov.moviespot.ui.discover.result

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.launch

class DiscoverResultViewModel(pager: DiscoverResultPager) : ViewModel() {

    val movies = pager.pagingDataFlow.cachedIn(scope = viewModelScope)

    var error by mutableStateOf<Throwable?>(value = null)
        private set

    fun onError(error: Throwable) {
        viewModelScope.launch {
            this@DiscoverResultViewModel.error = error
        }
    }

    fun onErrorConsumed() {
        viewModelScope.launch {
            this@DiscoverResultViewModel.error = null
        }
    }
}