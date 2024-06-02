package com.vlohachov.shared.presentation.ui.screen.settings

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.shared.domain.model.settings.Settings
import com.vlohachov.shared.domain.usecase.settings.ApplyDynamicTheme
import com.vlohachov.shared.domain.usecase.settings.LoadSettings
import com.vlohachov.shared.presentation.core.ViewState
import com.vlohachov.shared.presentation.core.WhileUiSubscribed
import com.vlohachov.shared.presentation.core.toViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

@Stable
internal class SettingsViewModel(
    loadSettings: LoadSettings,
    private val applyDynamicTheme: ApplyDynamicTheme,
) : ViewModel() {

    private val _error = MutableStateFlow<Throwable?>(value = null)

    val error: StateFlow<Throwable?> = _error
    val viewState: StateFlow<ViewState<Settings>> = loadSettings(param = Unit)
        .map { result -> result.toViewState() }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = ViewState.Loading
        )

    fun applyDynamicTheme(apply: Boolean): Unit = runBlocking(viewModelScope.coroutineContext) {
        applyDynamicTheme(param = ApplyDynamicTheme.Param(apply = apply))
            .collect()
    }

    fun onError(error: Throwable) {
        _error.tryEmit(value = error)
    }

    fun onErrorConsumed() {
        _error.tryEmit(value = null)
    }

}
