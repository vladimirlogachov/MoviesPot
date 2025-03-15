package com.vlohachov.shared.presentation.ui.screen.settings

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.model.settings.Settings
import com.vlohachov.shared.domain.usecase.settings.ApplyDynamicTheme
import com.vlohachov.shared.domain.usecase.settings.LoadSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@Stable
internal class SettingsViewModel(
    loadSettings: LoadSettings,
    private val applyDynamicTheme: ApplyDynamicTheme,
) : ViewModel() {

    private val _settings = loadSettings(param = Unit)
        .onEach { result ->
            uiState.update { state ->
                when (result) {
                    Result.Loading -> state.copy(isLoading = true)
                    is Result.Error -> state.copy(isLoading = false, error = result.exception)
                    is Result.Success<Settings> -> SettingsUiState(settings = result.value)
                }
            }
        }

    val uiState: StateFlow<SettingsUiState>
        field: MutableStateFlow<SettingsUiState> = MutableStateFlow(value = SettingsUiState())

    init {
        _settings.launchIn(scope = viewModelScope)
    }

    fun applyDynamicTheme(apply: Boolean) {
        applyDynamicTheme(param = ApplyDynamicTheme.Param(apply = apply))
            .launchIn(scope = viewModelScope)
    }

    fun resetError() {
        uiState.update { state -> state.copy(error = null) }
    }

}
