package com.vlohachov.shared.data.local

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

public class LocalPreferences(private val settings: Settings) {

    internal companion object {
        const val KEY_DYNAMIC_THEME = "DynamicTheme"
    }

    private val _dynamicThemeFlow =
        MutableStateFlow(value = settings[KEY_DYNAMIC_THEME] ?: false)

    public val applyDynamicThemeFlow: Flow<Boolean> =
        _dynamicThemeFlow

    public fun applyDynamicTheme(apply: Boolean) {
        if (_dynamicThemeFlow.tryEmit(value = apply)) {
            settings[KEY_DYNAMIC_THEME] = apply
        }
    }

}
