package com.vlohachov.moviespot.ui.movies.now

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vlohachov.domain.usecase.movie.list.NowPlayingUseCase

class NowPlayingMoviesPager(
    useCase: NowPlayingUseCase,
    config: PagingConfig = PagingConfig(pageSize = 20),
) {

    val pagingDataFlow = Pager(config = config) {
        NowPlayingMoviesSource(useCase = useCase)
    }.flow
}
