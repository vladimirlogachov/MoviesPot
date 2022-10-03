package com.vlohachov.domain.repository

import com.vlohachov.domain.model.settings.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getSettings(): Flow<Settings>

    suspend fun updateDynamicTheme(dynamicTheme: Boolean)
}
