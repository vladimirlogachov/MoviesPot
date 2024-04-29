package com.vlohachov.shared.data.repository

import com.vlohachov.shared.data.local.LocalPreferences
import com.vlohachov.shared.data.local.isDynamicThemeAvailable
import com.vlohachov.shared.domain.model.settings.Settings
import com.vlohachov.shared.domain.repository.SettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
public class LocalSettingsRepository(
    private val preferences: LocalPreferences
) : SettingsRepository {

    override fun getSettings(): Flow<Settings> =
        preferences.applyDynamicThemeFlow.mapLatest { value ->
            Settings(
                dynamicTheme = value,
                supportsDynamicTheme = isDynamicThemeAvailable(),
            )
        }

    override suspend fun applyDynamicTheme(apply: Boolean) {
        preferences.applyDynamicTheme(apply = apply)
    }

}
