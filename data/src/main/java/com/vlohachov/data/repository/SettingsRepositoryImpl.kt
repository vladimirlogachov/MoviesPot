package com.vlohachov.data.repository

import com.vlohachov.data.local.dao.SettingsDao
import com.vlohachov.data.local.entity.SettingsEntity
import com.vlohachov.data.local.entity.toDomain
import com.vlohachov.domain.model.settings.Settings
import com.vlohachov.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.*

class SettingsRepositoryImpl(private val dao: SettingsDao) : SettingsRepository {

    override fun getSettings(): Flow<Settings> {
        return dao.querySettings()
            .distinctUntilChanged()
            .onEach { entity ->
                if (entity == null) {
                    dao.insertSettings(settings = SettingsEntity(id = 0, dynamicTheme = true))
                }
            }
            .filterNotNull()
            .map(SettingsEntity::toDomain)
    }

    override suspend fun updateDynamicTheme(dynamicTheme: Boolean) {
        dao.updateDynamicTheme(dynamicTheme = dynamicTheme)
    }
}
