package com.vlohachov.moviespot.ui.discover.result

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vlohachov.domain.usecase.DiscoverMovies

class DiscoverResultPager(
    year: Int?,
    selectedGenres: IntArray?,
    useCase: DiscoverMovies,
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
