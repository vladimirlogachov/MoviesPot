package com.vlohachov.moviespot.ui.keyword

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vlohachov.shared.domain.usecase.DiscoverMovies

class KeywordMoviesPager(
    keywordId: Int,
    useCase: DiscoverMovies,
    config: PagingConfig = PagingConfig(pageSize = 20),
) {

    val pagingDataFlow = Pager(config = config) {
        KeywordMoviesSource(keywordId = keywordId, useCase = useCase)
    }.flow

}
