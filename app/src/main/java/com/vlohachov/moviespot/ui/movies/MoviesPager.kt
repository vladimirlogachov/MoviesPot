package com.vlohachov.moviespot.ui.movies

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vlohachov.domain.model.movie.MovieCategory
import com.vlohachov.domain.usecase.movie.LoadMoviesByCategory

class MoviesPager(
    private val useCase: LoadMoviesByCategory,
    private val config: PagingConfig = PagingConfig(pageSize = 20),
) {

    fun pagingDataFlow(category: MovieCategory) = Pager(config = config) {
        MoviesSource(category = category, useCase = useCase)
    }.flow

}
