package com.vlohachov.shared.ui.screen.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.usecase.SearchMovies
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(
    FlowPreview::class,
    ExperimentalCoroutinesApi::class,
)
internal class MoviesSearchPager(
    useCase: SearchMovies,
    debounce: Long = QUERY_CHANGE_DEBOUNCE,
    config: PagingConfig = PagingConfig(pageSize = 20),
) {

    private companion object {
        const val QUERY_CHANGE_DEBOUNCE = 400L
    }

    private val _query = MutableStateFlow(value = "")

    val pagingDataFlow: Flow<PagingData<Movie>> = _query
        .debounce(timeoutMillis = debounce)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            Pager(config = config) { MoviesSearchSource(query = query, useCase = useCase) }.flow
        }

    fun onQuery(query: String) {
        _query.tryEmit(value = query)
    }

}
