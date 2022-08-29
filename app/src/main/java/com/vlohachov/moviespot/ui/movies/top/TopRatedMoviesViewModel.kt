package com.vlohachov.moviespot.ui.movies.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.vlohachov.domain.usecase.TopRatedUseCase

class TopRatedMoviesViewModel(useCase: TopRatedUseCase) : ViewModel() {

    private companion object Constants {
        const val PageSize = 20
    }

    val movies = Pager(config = PagingConfig(pageSize = PageSize)) {
        TopRatedMoviesSource(useCase = useCase)
    }.flow.cachedIn(viewModelScope)
}