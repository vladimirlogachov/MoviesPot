package com.vlohachov.moviespot.ui.keyword

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vlohachov.domain.usecase.DiscoverMoviesUseCase

class KeywordMoviesPager(
    keywordId: Int,
    useCase: DiscoverMoviesUseCase,
    config: PagingConfig = PagingConfig(pageSize = 20),
) {

    val pagingDataFlow = Pager(config = config) {
        KeywordMoviesSource(keywordId = keywordId, useCase = useCase)
    }.flow
}