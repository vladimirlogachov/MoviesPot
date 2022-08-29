package com.vlohachov.moviespot.ui.movies.now

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.vlohachov.domain.usecase.NowPlayingUseCase

class NowPlayingMoviesViewModel(useCase: NowPlayingUseCase) : ViewModel() {

    private companion object Constants {
        const val PageSize = 20
    }

    val movies = Pager(config = PagingConfig(pageSize = PageSize)) {
        NowPlayingMoviesSource(useCase = useCase)
    }.flow.cachedIn(viewModelScope)
}