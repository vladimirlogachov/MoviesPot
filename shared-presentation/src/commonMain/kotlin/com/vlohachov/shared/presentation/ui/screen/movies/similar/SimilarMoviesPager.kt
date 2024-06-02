package com.vlohachov.shared.presentation.ui.screen.movies.similar

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vlohachov.shared.domain.usecase.movie.LoadRecommendations

internal class SimilarMoviesPager(
    movieId: Long,
    useCase: LoadRecommendations,
    config: PagingConfig = PagingConfig(pageSize = 20),
) {

    val pagingDataFlow = Pager(config = config) {
        SimilarMoviesSource(movieId = movieId, useCase = useCase)
    }.flow

}
