package com.vlohachov.shared.ui.screen.settings

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.shared.core.ViewState
import com.vlohachov.shared.core.toViewState
import com.vlohachov.shared.domain.model.settings.Settings
import com.vlohachov.shared.domain.usecase.settings.ApplyDynamicTheme
import com.vlohachov.shared.domain.usecase.settings.LoadSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Stable
internal class SettingsViewModel(
    loadSettings: LoadSettings,
    private val applyDynamicTheme: ApplyDynamicTheme,
) : ViewModel() {

    val viewState: Flow<ViewState<Settings>> = loadSettings(param = Unit)
        .map { result -> result.toViewState() }

    var error by mutableStateOf<Throwable?>(value = null)
        private set

    fun applyDynamicTheme(apply: Boolean): Unit = runBlocking(viewModelScope.coroutineContext) {
        applyDynamicTheme(param = ApplyDynamicTheme.Param(apply = apply))
            .collect()
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
