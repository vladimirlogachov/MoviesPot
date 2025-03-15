package com.vlohachov.shared.presentation.ui.screen.settings

import com.vlohachov.shared.domain.model.settings.Settings
import com.vlohachov.shared.presentation.ui.theme.isDynamicThemeAvailable

internal data class SettingsUiState(
    val isLoading: Boolean = false,
    val settings: Settings = Settings(),
    val isDynamicThemeAvailable: Boolean = isDynamicThemeAvailable(),
    val error: Throwable? = null,
)
