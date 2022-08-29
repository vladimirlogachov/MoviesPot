package com.vlohachov.moviespot.ui.movies.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.vlohachov.domain.usecase.PopularUseCase

class PopularMoviesViewModel(useCase: PopularUseCase) : ViewModel() {

    private companion object Constants {
        const val PageSize = 20
    }

    val movies = Pager(config = PagingConfig(pageSize = PageSize)) {
        PopularMoviesSource(useCase = useCase)
    }.flow.cachedIn(viewModelScope)
}