package com.vlohachov.shared.ui.screen.movies

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vlohachov.shared.domain.model.movie.MovieCategory
import com.vlohachov.shared.domain.usecase.movie.LoadMoviesByCategory

internal class MoviesPager(
    private val useCase: LoadMoviesByCategory,
    private val config: PagingConfig = PagingConfig(pageSize = 20),
) {

    fun pagingDataFlow(category: MovieCategory) = Pager(config = config) {
        MoviesSource(category = category, useCase = useCase)
    }.flow

}
