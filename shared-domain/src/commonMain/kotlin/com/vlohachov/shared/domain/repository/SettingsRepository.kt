package com.vlohachov.shared.domain.repository

import com.vlohachov.shared.domain.model.settings.Settings
import kotlinx.coroutines.flow.Flow

public interface SettingsRepository {

    public fun getSettings(): Flow<Settings>

    public suspend fun applyDynamicTheme(apply: Boolean)

}
