package com.vlohachov.shared.presentation.ui.screen.details

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.shared.domain.usecase.credits.LoadDirector
import com.vlohachov.shared.domain.usecase.movie.LoadDetails
import com.vlohachov.shared.domain.usecase.movie.LoadKeywords
import com.vlohachov.shared.domain.usecase.movie.LoadRecommendations
import com.vlohachov.shared.presentation.core.WhileUiSubscribed
import com.vlohachov.shared.presentation.core.toViewState
import com.vlohachov.shared.presentation.core.toViewStatePaginated
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@Stable
internal class MovieDetailsViewModel(
    movieId: Long,
    loadDetails: LoadDetails,
    loadDirector: LoadDirector,
    loadKeywords: LoadKeywords,
    loadRecommendations: LoadRecommendations,
) : ViewModel() {

    private val _error = MutableStateFlow<Throwable?>(value = null)

    val uiState: StateFlow<MovieDetailsViewState> = combine(
        loadDetails(param = LoadDetails.Param(id = movieId)),
        loadDirector(param = LoadDirector.Param(id = movieId)),
        loadKeywords(param = LoadKeywords.Param(id = movieId)),
        loadRecommendations(param = LoadRecommendations.Param(id = movieId)),
        _error,
    ) { details, director, keywords, recommendations, error ->
        MovieDetailsViewState(
            detailsViewState = details.toViewState(),
            directorViewState = director.toViewState(),
            keywordsViewState = keywords.toViewState(),
            recommendationsViewState = recommendations.toViewStatePaginated(),
            error = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = MovieDetailsViewState(),
    )

    fun onError(error: Throwable) {
        _error.tryEmit(value = error)
    }

    fun onErrorConsumed() {
        _error.tryEmit(value = null)
    }

}
