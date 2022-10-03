package com.vlohachov.data.local.entity

import android.os.Build
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vlohachov.data.local.StoreTables
import com.vlohachov.domain.model.settings.Settings

@Entity(tableName = StoreTables.Settings)
data class SettingsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "dynamic_theme")
    val dynamicTheme: Boolean,
)

internal fun SettingsEntity.toDomain(): Settings =
    Settings(
        dynamicTheme = dynamicTheme,
        supportsDynamicTheme = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
    )
