package com.vlohachov.shared.presentation.ui.screen.discover.result

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vlohachov.shared.domain.usecase.DiscoverMovies

internal class DiscoverResultPager(
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
