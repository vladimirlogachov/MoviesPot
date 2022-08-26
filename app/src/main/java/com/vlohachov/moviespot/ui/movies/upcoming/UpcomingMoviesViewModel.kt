package com.vlohachov.moviespot.ui.movies.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.vlohachov.domain.usecase.UpcomingUseCase

class UpcomingMoviesViewModel(upcoming: UpcomingUseCase) : ViewModel() {

    val movies = Pager(config = PagingConfig(pageSize = 20)) {
        UpcomingMoviesDataSource(upcoming = upcoming)
    }.flow.cachedIn(viewModelScope)
}