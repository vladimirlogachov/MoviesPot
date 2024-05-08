package com.vlohachov.shared.ui.screen.credits.cast

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.shared.core.WhileUiSubscribed
import com.vlohachov.shared.core.toViewState
import com.vlohachov.shared.domain.usecase.credits.LoadCast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Stable
internal class CastViewModel(
    movieId: Long,
    loadCast: LoadCast,
) : ViewModel() {

    private val error = MutableStateFlow<Throwable?>(value = null)

    val uiState: StateFlow<CastViewState> = combine(
        loadCast(param = LoadCast.Param(id = movieId)),
        error,
    ) { cast, error ->
        CastViewState(
            viewState = cast.toViewState(),
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
