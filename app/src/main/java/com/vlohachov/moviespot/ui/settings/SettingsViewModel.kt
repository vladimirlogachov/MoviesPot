package com.vlohachov.moviespot.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.settings.Settings
import com.vlohachov.domain.usecase.settings.ApplyDynamicThemeUseCase
import com.vlohachov.domain.usecase.settings.GetSettingsUseCase
import com.vlohachov.moviespot.core.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingsViewModel(
    getSettings: GetSettingsUseCase,
    private val applyDynamicTheme: ApplyDynamicThemeUseCase,
) : ViewModel() {

    val viewState: Flow<ViewState<Settings>> = getSettings.resultFlow(param = Unit)
        .map { result ->
            when (result) {
                Result.Loading -> ViewState.Loading
                is Result.Error -> ViewState.Error(error = result.exception)
                is Result.Success -> ViewState.Success(data = result.value)
            }
        }

    var error by mutableStateOf<Throwable?>(value = null)
        private set

    fun applyDynamicTheme(apply: Boolean) {
        viewModelScope.launch {
            applyDynamicTheme
                .resultFlow(param = ApplyDynamicThemeUseCase.Param(apply = apply))
                .collect()
        }
    }

    fun onError(error: Throwable) {
        viewModelScope.launch {
            this@SettingsViewModel.error = error
        }
    }

    fun onErrorConsumed() {
        viewModelScope.launch {
            this@SettingsViewModel.error = null
        }
    }
}
