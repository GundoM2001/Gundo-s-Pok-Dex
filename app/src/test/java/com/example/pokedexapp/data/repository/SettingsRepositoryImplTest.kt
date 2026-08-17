package com.example.pokedexapp.data.repository

import app.cash.turbine.test
import com.example.pokedexapp.data.local.dao.UserPreferencesDao
import com.example.pokedexapp.data.local.entities.UserPreferencesEntity
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SettingsRepositoryImplTest {

    private lateinit var repository: SettingsRepositoryImpl
    private val dao: UserPreferencesDao = mockk()

    @Before
    fun setup() {
        repository = SettingsRepositoryImpl(dao)
    }

    @Test
    fun `getUserPreferences returns flow from dao`() = runTest {
        val prefs = UserPreferencesEntity(0, "en", 1)
        every { dao.getUserPreferences() } returns flowOf(prefs)

        repository.getUserPreferences().test {
            assertEquals(prefs, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `saveUserPreferences calls dao save`() = runTest {
        val prefs = UserPreferencesEntity(0, "fr", 2)
        coEvery { dao.saveUserPreferences(prefs) } just Runs

        repository.saveUserPreferences(prefs)

        coVerify { dao.saveUserPreferences(prefs) }
    }
}
