package com.vlohachov.moviespot.ui.search

import androidx.paging.PagingData
import com.vlohachov.domain.model.movie.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class SearchViewState(
    val search: String = "",
    val results: Flow<PagingData<Movie>> = flowOf(PagingData.empty()),
    val error: Throwable? = null,
)
