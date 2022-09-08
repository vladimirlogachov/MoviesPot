package com.vlohachov.moviespot.ui.discover.result

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.vlohachov.domain.usecase.DiscoverMoviesUseCase
import com.vlohachov.moviespot.ui.discover.DiscoverParam
import kotlinx.coroutines.launch

class DiscoverResultViewModel(
    param: DiscoverParam,
    useCase: DiscoverMoviesUseCase,
) : ViewModel() {

    private companion object Constants {
        const val PageSize = 20
    }

    val movies = Pager(config = PagingConfig(pageSize = PageSize)) {
        DiscoverResultSource(
            selectedGenres = param.selectedGenres,
            year = param.year,
            useCase = useCase,
        )
    }.flow.cachedIn(scope = viewModelScope)

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