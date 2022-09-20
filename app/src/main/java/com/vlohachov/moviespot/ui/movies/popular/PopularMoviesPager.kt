package com.vlohachov.moviespot.ui.movies.popular

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vlohachov.domain.usecase.movie.list.PopularUseCase

class PopularMoviesPager(
    useCase: PopularUseCase,
    config: PagingConfig = PagingConfig(pageSize = 20),
) {

    val pagingDataFlow = Pager(config = config) {
        PopularMoviesSource(useCase = useCase)
    }.flow
}