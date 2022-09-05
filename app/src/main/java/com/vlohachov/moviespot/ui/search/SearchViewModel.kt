package com.vlohachov.moviespot.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.vlohachov.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel(useCase: SearchMoviesUseCase) : ViewModel() {

    private companion object Constants {
        const val PageSize = 20
    }

    var search = MutableStateFlow(value = "")
        private set

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val movies = search
        .debounce(timeoutMillis = 400L)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            Pager(config = PagingConfig(pageSize = PageSize)) {
                SearchSource(query = query, useCase = useCase)
            }.flow
        }
        .catch { error -> onError(error = error) }
        .cachedIn(viewModelScope)


    var error by mutableStateOf<Throwable?>(value = null)
        private set

    fun onClear() {
        viewModelScope.launch {
            search.value = ""
        }
    }

    fun onSearch(search: String) {
        viewModelScope.launch {
            this@SearchViewModel.search.value = search
        }
    }

    fun onError(error: Throwable) {
        viewModelScope.launch {
            this@SearchViewModel.error = error
        }
    }

    fun onErrorConsumed() {
        viewModelScope.launch {
            error = null
        }
    }
}