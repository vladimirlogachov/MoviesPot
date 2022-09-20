package com.vlohachov.moviespot.ui.discover.result

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vlohachov.domain.usecase.DiscoverMoviesUseCase

class DiscoverResultPager(
    year: Int?,
    selectedGenres: IntArray?,
    useCase: DiscoverMoviesUseCase,
    config: PagingConfig = PagingConfig(pageSize = 20),
) {

    val pagingDataFlow = Pager(config = config) {
        DiscoverResultSource(
            year = year,
            selectedGenres = selectedGenres?.toList(),
            useCase = useCase,
        )
    }.flow
}