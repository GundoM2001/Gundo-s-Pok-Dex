package com.example.pokedexapp.presentation

import app.cash.turbine.test
import com.example.pokedexapp.data.local.entities.UserPreferencesEntity
import com.example.pokedexapp.domain.repository.SettingsRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val repository: SettingsRepository = mockk()
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `preferences should emit default value initially`() = runTest {
        every { repository.getUserPreferences() } returns flowOf(null)
        
        viewModel = MainViewModel(repository)

        viewModel.preferences.test {
            assertEquals(UserPreferencesEntity(), awaitItem())
        }
    }

    @Test
    fun `preferences should emit updated values from repository`() = runTest {
        val customPreferences = UserPreferencesEntity(languageCode = "en", themeMode = 2)
        every { repository.getUserPreferences() } returns flowOf(customPreferences)

        viewModel = MainViewModel(repository)

        viewModel.preferences.test {
            assertEquals(UserPreferencesEntity(), awaitItem()) 
            assertEquals(customPreferences, awaitItem()) 
        }
    }
}
