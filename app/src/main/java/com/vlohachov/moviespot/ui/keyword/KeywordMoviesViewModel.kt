package com.vlohachov.moviespot.ui.keyword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.vlohachov.domain.usecase.DiscoverMoviesUseCase
import kotlinx.coroutines.launch

class KeywordMoviesViewModel(
    keywordId: Int,
    useCase: DiscoverMoviesUseCase,
) : ViewModel() {

    private companion object Constants {
        const val PageSize = 20
    }

    val movies = Pager(config = PagingConfig(pageSize = PageSize)) {
        KeywordMoviesSource(
            keywordId = keywordId,
            useCase = useCase,
        )
    }.flow.cachedIn(scope = viewModelScope)

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