package com.vlohachov.moviespot.ui.movies.similar

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vlohachov.domain.usecase.movie.MovieRecommendationsUseCase

class SimilarMoviesPager(
    movieId: Long,
    useCase: MovieRecommendationsUseCase,
    config: PagingConfig = PagingConfig(pageSize = 20),
) {

    val pagingDataFlow = Pager(config = config) {
        SimilarMoviesSource(movieId = movieId, useCase = useCase)
    }.flow
}
