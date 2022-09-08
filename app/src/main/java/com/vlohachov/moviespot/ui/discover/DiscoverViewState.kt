package com.vlohachov.moviespot.ui.discover

import androidx.paging.PagingData
import com.vlohachov.domain.model.genre.Genre
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.moviespot.core.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class DiscoverViewState(
    val year: String = "",
    val genresViewState: ViewState<List<Genre>> = ViewState.Loading,
    val selectedGenres: List<Genre> = listOf(),
    val movies: Flow<PagingData<Movie>> = emptyFlow(),
    val error: Throwable? = null,
)
