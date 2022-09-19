package com.vlohachov.moviespot.ui.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@OptIn(
    FlowPreview::class,
    ExperimentalCoroutinesApi::class,
)
class SearchMoviesPager(
    useCase: SearchMoviesUseCase,
    config: PagingConfig = PagingConfig(pageSize = 20),
) {

    private companion object {
        const val QUERY_CHANGE_DEBOUNCE = 400L
    }

    private val query = MutableStateFlow(value = "")

    val pagingDataFlow: Flow<PagingData<Movie>> = query
        .debounce(timeoutMillis = QUERY_CHANGE_DEBOUNCE)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            Pager(config = config) { SearchMoviesSource(query = query, useCase = useCase) }.flow
        }

    fun onQuery(query: String) {
        this.query.value = query
    }
}