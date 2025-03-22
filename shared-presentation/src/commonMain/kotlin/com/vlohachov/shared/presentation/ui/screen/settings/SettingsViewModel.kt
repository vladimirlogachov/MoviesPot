package com.vlohachov.shared.presentation.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.model.settings.Settings
import com.vlohachov.shared.domain.usecase.settings.ApplyDynamicTheme
import com.vlohachov.shared.domain.usecase.settings.LoadSettings
import com.vlohachov.shared.presentation.core.WhileUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class SettingsViewModel(
    loadSettings: LoadSettings,
    private val applyDynamicTheme: ApplyDynamicTheme,
) : ViewModel() {

    private val _uiState = MutableStateFlow(value = SettingsUiState())

    val uiState: StateFlow<SettingsUiState> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = SettingsUiState(),
        )

    init {
        loadSettings()
            .onEach(::processResult)
            .launchIn(scope = viewModelScope)
    }

    fun applyDynamicTheme(apply: Boolean) {
        viewModelScope.launch {
            applyDynamicTheme(param = ApplyDynamicTheme.Param(apply = apply))
        }
    }

    fun resetError() = _uiState.update { state ->
        state.copy(error = null)
    }

    private fun processResult(result: Result<Settings>) = _uiState.update { state ->
        when (result) {
            Result.Loading ->
                state.copy(isLoading = true)

            is Result.Error ->
                state.copy(isLoading = false, error = result.exception)

            is Result.Success<Settings> ->
                state.copy(isLoading = false, settings = result.value, error = null)
        }
    }

}
