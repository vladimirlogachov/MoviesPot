package com.vlohachov.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vlohachov.data.local.StoreTables
import com.vlohachov.data.local.entity.SettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    @Insert
    suspend fun insertSettings(settings: SettingsEntity)

    @Query("SELECT * FROM ${StoreTables.Settings} LIMIT 1")
    fun querySettings(): Flow<SettingsEntity?>

    @Query("UPDATE ${StoreTables.Settings} SET dynamic_theme = :dynamicTheme")
    suspend fun updateDynamicTheme(dynamicTheme: Boolean)
}
