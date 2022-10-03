package com.vlohachov.data.repository

import com.google.common.truth.Truth
import com.vlohachov.data.data.SettingsTestEntity
import com.vlohachov.data.local.dao.SettingsDao
import com.vlohachov.data.local.entity.toDomain
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsRepositoryTest {

    private val dao = mockk<SettingsDao>()

    private val repository = SettingsRepositoryImpl(dao = dao)

    @Test
    fun `Settings loading success`() = runTest {
        val expected = SettingsTestEntity.toDomain()

        every { dao.querySettings() } returns flowOf(value = SettingsTestEntity)

        val actual = repository.getSettings().first()

        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test(expected = NullPointerException::class)
    fun `Settings loading failure`() = runTest {
        every { dao.querySettings() } throws NullPointerException()

        repository.getSettings().first()
    }

    @Test
    fun `Update dynamic theme success`() = runTest {
        coJustRun { dao.updateDynamicTheme(dynamicTheme = any()) }

        repository.updateDynamicTheme(dynamicTheme = true)

        coVerify(exactly = 1) { dao.updateDynamicTheme(dynamicTheme = any()) }
    }

    @Test(expected = NullPointerException::class)
    fun `Update dynamic theme failure`() = runTest {
        coEvery { dao.updateDynamicTheme(dynamicTheme = any()) } throws NullPointerException()

        repository.updateDynamicTheme(dynamicTheme = true)
    }
}
