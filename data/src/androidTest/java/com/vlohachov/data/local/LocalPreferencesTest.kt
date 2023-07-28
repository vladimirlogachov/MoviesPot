package com.vlohachov.data.local

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LocalPreferencesTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val preferences = LocalPreferences(context = context)

    @Test
    fun isDynamicThemeAvailableTest() {
        val actual = preferences.isDynamicThemeAvailable

        Truth.assertThat(actual).isTrue()
    }

    @Test
    fun readApplyDynamicThemeTest() = runTest {
        preferences.applyDynamicTheme.test {
            val actual = awaitItem()

            Truth.assertThat(actual).isNotNull()
        }
    }

    @Test
    fun applyDynamicThemeTest() = runTest {
        preferences.applyDynamicTheme.test {
            val previous = awaitItem()

            preferences.applyDynamicTheme(apply = !previous)

            val actual = awaitItem()

            Truth.assertThat(actual).isNotEqualTo(previous)
        }
    }
}
