package com.vlohachov.data.local

import android.content.Context
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class LocalPreferences(private val context: Context) {

    private companion object {
        const val PreferencesName = "Settings"
        const val KeyDynamicTheme = "DynamicTheme"

        val DynamicTheme = booleanPreferencesKey(name = KeyDynamicTheme)
    }

    private val Context.dataStore by preferencesDataStore(name = PreferencesName)

    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    val isDynamicThemeAvailable: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val applyDynamicTheme = context.dataStore.data.map { preferences ->
        preferences[DynamicTheme] ?: isDynamicThemeAvailable
    }

    suspend fun applyDynamicTheme(apply: Boolean) {
        context.dataStore.edit { mutablePreferences ->
            mutablePreferences[DynamicTheme] = apply
        }
    }
}
