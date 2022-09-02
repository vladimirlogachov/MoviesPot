package com.vlohachov.moviespot.ui.movies.similar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.vlohachov.domain.usecase.movie.MovieRecommendationsUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SimilarMoviesViewModel(
    movieId: Long,
    useCase: MovieRecommendationsUseCase,
) : ViewModel() {

    private companion object Constants {
        const val PageSize = 20
    }

    val movies = Pager(config = PagingConfig(pageSize = PageSize)) {
        SimilarMoviesSource(movieId = movieId, useCase = useCase)
    }.flow
        .catch { e -> error = e }
        .cachedIn(viewModelScope)

    var error by mutableStateOf<Throwable?>(value = null)
        private set

    fun onError(e: Throwable) {
        viewModelScope.launch {
            error = e
        }
    }

    fun onErrorConsumed() {
        viewModelScope.launch {
            error = null
        }
    }
}