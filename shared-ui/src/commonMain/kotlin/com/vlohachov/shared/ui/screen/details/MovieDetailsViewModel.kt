package com.vlohachov.shared.ui.screen.details

import androidx.compose.runtime.Stable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.vlohachov.shared.domain.usecase.credits.LoadDirector
import com.vlohachov.shared.domain.usecase.movie.LoadDetails
import com.vlohachov.shared.domain.usecase.movie.LoadKeywords
import com.vlohachov.shared.domain.usecase.movie.LoadRecommendations
import com.vlohachov.shared.ui.core.WhileUiSubscribed
import com.vlohachov.shared.ui.state.toViewState
import com.vlohachov.shared.ui.state.toViewStatePaginated
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Stable
internal class MovieDetailsViewModel(
    movieId: Long,
    loadDetails: LoadDetails,
    loadDirector: LoadDirector,
    loadKeywords: LoadKeywords,
    loadRecommendations: LoadRecommendations,
) : ScreenModel {

    private val error = MutableStateFlow<Throwable?>(value = null)

    val uiState: StateFlow<MovieDetailsViewState> = combine(
        loadDetails(param = LoadDetails.Param(id = movieId)),
        loadDirector(param = LoadDirector.Param(id = movieId)),
        loadKeywords(param = LoadKeywords.Param(id = movieId)),
        loadRecommendations(param = LoadRecommendations.Param(id = movieId)),
        error,
    ) { details, director, keywords, recommendations, error ->
        MovieDetailsViewState(
            detailsViewState = details.toViewState(),
            directorViewState = director.toViewState(),
            keywordsViewState = keywords.toViewState(),
            recommendationsViewState = recommendations.toViewStatePaginated(),
            error = error,
        )
    }.stateIn(
        scope = screenModelScope,
        started = WhileUiSubscribed,
        initialValue = MovieDetailsViewState(),
    )

    fun onError(error: Throwable) {
        screenModelScope.launch { this@MovieDetailsViewModel.error.emit(value = error) }
    }

    fun onErrorConsumed() {
        screenModelScope.launch { this@MovieDetailsViewModel.error.emit(value = null) }
    }

}
