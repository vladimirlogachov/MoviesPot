package com.vlohachov.shared.ui.screen.credits.cast

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.shared.core.ViewState
import com.vlohachov.shared.core.WhileUiSubscribed
import com.vlohachov.shared.core.toViewState
import com.vlohachov.shared.domain.usecase.credits.LoadCast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Stable
internal class CastViewModel(
    movieId: Long,
    loadCast: LoadCast,
) : ViewModel() {

    private val _error = MutableStateFlow<Throwable?>(value = null)

    val error: StateFlow<Throwable?> = _error
    val cast = loadCast(param = LoadCast.Param(id = movieId))
        .map { result -> result.toViewState() }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = ViewState.Loading
        )

    fun onError(error: Throwable) {
        _error.tryEmit(value = error)
    }

    fun onErrorConsumed() {
        _error.tryEmit(value = null)
    }

}
