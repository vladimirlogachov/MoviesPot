package com.vlohachov.moviespot.ui.movies.upcoming

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vlohachov.domain.usecase.movie.list.UpcomingUseCase

class UpcomingMoviesPager(
    useCase: UpcomingUseCase,
    config: PagingConfig = PagingConfig(pageSize = 20),
) {

    val pagingDataFlow = Pager(config = config) {
        UpcomingMoviesSource(useCase = useCase)
    }.flow
}
