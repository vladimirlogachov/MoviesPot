package com.vlohachov.data.local

import android.content.Context
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private const val PreferencesName = "Settings"
private const val KeyDynamicTheme = "DynamicTheme"

private val Context.dataStore by preferencesDataStore(name = PreferencesName)

class LocalPreferences(private val context: Context) {

    private companion object {
        val DynamicTheme = booleanPreferencesKey(name = KeyDynamicTheme)
    }

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
