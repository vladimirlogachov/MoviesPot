package com.vlohachov.moviespot.ui.credits.cast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.movie.credit.CastMember
import com.vlohachov.domain.usecase.CastUseCase
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.core.WhileUiSubscribed
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CastViewModel(
    movieId: Long,
    cast: CastUseCase,
) : ViewModel() {

    private val error = MutableStateFlow<Throwable?>(value = null)

    private val castResult: Flow<Result<List<CastMember>>> =
        cast.resultFlow(param = CastUseCase.Param(id = movieId))

    val uiState: StateFlow<CastViewState> = combine(
        castResult,
        error,
    ) { castResult, error ->
        val viewState = when (castResult) {
            Result.Loading -> ViewState.Loading
            is Result.Error -> ViewState.Error(error = castResult.exception)
            is Result.Success -> ViewState.Success(data = castResult.value)
        }

        CastViewState(
            viewState = viewState,
            error = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = CastViewState(),
    )

    fun onError(error: Throwable) {
        viewModelScope.launch { this@CastViewModel.error.emit(value = error) }
    }

    fun onErrorConsumed() {
        viewModelScope.launch { this@CastViewModel.error.emit(value = null) }
    }
}