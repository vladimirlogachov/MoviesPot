package com.vlohachov.moviespot.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.vlohachov.domain.usecase.SearchMoviesUseCase
import com.vlohachov.moviespot.core.WhileUiSubscribed
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel(useCase: SearchMoviesUseCase) : ViewModel() {

    private companion object Constants {
        const val PageSize = 20
    }

    var search = MutableStateFlow(value = "")
        private set

    @OptIn(FlowPreview::class)
    private val query = search
        .debounce(timeoutMillis = 400L)
        .distinctUntilChanged()


    var error = MutableStateFlow<Throwable?>(value = null)
        private set

    val uiState: StateFlow<SearchViewState> = combine(
        query,
        error,
    ) { query, error ->
        val results = Pager(config = PagingConfig(pageSize = PageSize)) {
            SearchSource(query = query, useCase = useCase)
        }.flow.cachedIn(viewModelScope)

        SearchViewState(
            results = results,
            error = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = SearchViewState(),
    )

    fun onSearch(search: String) {
        viewModelScope.launch {
            this@SearchViewModel.search.value = search
        }
    }

    fun onError(error: Throwable) {
        viewModelScope.launch { this@SearchViewModel.error.emit(error) }
    }

    fun onErrorConsumed() {
        viewModelScope.launch { error.emit(value = null) }
    }
}