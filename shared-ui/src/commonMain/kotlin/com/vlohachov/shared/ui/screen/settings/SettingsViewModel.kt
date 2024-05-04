package com.vlohachov.shared.ui.screen.settings

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.vlohachov.shared.domain.model.settings.Settings
import com.vlohachov.shared.domain.usecase.settings.ApplyDynamicTheme
import com.vlohachov.shared.domain.usecase.settings.LoadSettings
import com.vlohachov.shared.ui.state.ViewState
import com.vlohachov.shared.ui.state.toViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Stable
internal class SettingsViewModel(
    getSettings: LoadSettings,
    private val applyDynamicTheme: ApplyDynamicTheme,
) : ScreenModel {

    val viewState: Flow<ViewState<Settings>> = getSettings(param = Unit)
        .map { result -> result.toViewState() }

    var error by mutableStateOf<Throwable?>(value = null)
        private set

    fun applyDynamicTheme(apply: Boolean) {
        screenModelScope.launch {
            applyDynamicTheme(param = ApplyDynamicTheme.Param(apply = apply))
                .collect()
        }
    }

    fun onError(error: Throwable) {
        screenModelScope.launch {
            this@SettingsViewModel.error = error
        }
    }

    fun onErrorConsumed() {
        screenModelScope.launch {
            this@SettingsViewModel.error = null
        }
    }

}
