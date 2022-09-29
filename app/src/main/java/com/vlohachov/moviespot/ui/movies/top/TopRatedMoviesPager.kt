package com.vlohachov.moviespot.ui.movies.top

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vlohachov.domain.usecase.movie.list.TopRatedUseCase

class TopRatedMoviesPager(
    useCase: TopRatedUseCase,
    config: PagingConfig = PagingConfig(pageSize = 20),
) {

    val pagingDataFlow = Pager(config = config) {
        TopRatedMoviesSource(useCase = useCase)
    }.flow
}
