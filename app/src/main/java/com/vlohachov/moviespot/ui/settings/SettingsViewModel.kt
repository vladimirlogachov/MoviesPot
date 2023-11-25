package com.vlohachov.moviespot.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.domain.model.settings.Settings
import com.vlohachov.domain.usecase.settings.ApplyDynamicTheme
import com.vlohachov.domain.usecase.settings.LoadSettings
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.core.toViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingsViewModel(
    getSettings: LoadSettings,
    private val applyDynamicTheme: ApplyDynamicTheme,
) : ViewModel() {

    val viewState: Flow<ViewState<Settings>> = getSettings(param = Unit)
        .map { result -> result.toViewState() }

    var error by mutableStateOf<Throwable?>(value = null)
        private set

    fun applyDynamicTheme(apply: Boolean) {
        viewModelScope.launch {
            applyDynamicTheme(param = ApplyDynamicTheme.Param(apply = apply))
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
