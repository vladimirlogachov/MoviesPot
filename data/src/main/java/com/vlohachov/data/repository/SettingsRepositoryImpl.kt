package com.vlohachov.data.repository

import com.vlohachov.data.local.LocalPreferences
import com.vlohachov.shared.domain.model.settings.Settings
import com.vlohachov.shared.domain.repository.SettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsRepositoryImpl(private val preferences: LocalPreferences) : SettingsRepository {

    override fun getSettings(): Flow<Settings> {
        return preferences.applyDynamicTheme.mapLatest { value ->
            Settings(
                dynamicTheme = value,
                supportsDynamicTheme = preferences.isDynamicThemeAvailable,
            )
        }
    }

    override suspend fun applyDynamicTheme(apply: Boolean) {
        preferences.applyDynamicTheme(apply = apply)
    }
}
