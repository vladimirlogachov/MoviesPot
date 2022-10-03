package com.vlohachov.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vlohachov.data.local.dao.SettingsDao
import com.vlohachov.data.local.entity.SettingsEntity

@Database(
    entities = [SettingsEntity::class],
    version = StoreConfig.StoreVersion,
)
abstract class LocalStore : RoomDatabase() {

    abstract fun settingsDao(): SettingsDao
}
