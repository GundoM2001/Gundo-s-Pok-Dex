package com.example.pokedexapp.presentation

import androidx.appcompat.app.AppCompatDelegate
import com.example.pokedexapp.data.local.entities.UserPreferencesEntity
import com.example.pokedexapp.domain.repository.SettingsRepository
import com.example.pokedexapp.presentation.feature.settings.ui.SettingsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val repository: SettingsRepository = mockk()
    private lateinit var viewModel: SettingsViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(AppCompatDelegate::class)
        every { AppCompatDelegate.setApplicationLocales(any()) } returns Unit
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(AppCompatDelegate::class)
    }

    @Test
    fun `onThemeChanged should call repository with updated theme`() = runTest {
        val initialPrefs = UserPreferencesEntity(themeMode = 1)
        every { repository.getUserPreferences() } returns flowOf(initialPrefs)
        coEvery { repository.saveUserPreferences(any()) } returns Unit
        
        viewModel = SettingsViewModel(repository)
        advanceUntilIdle()

        viewModel.onThemeChanged(2)
        advanceUntilIdle()

        coVerify { repository.saveUserPreferences(initialPrefs.copy(themeMode = 2)) }
    }

    @Test
    fun `onLanguageChanged should update repository and AppCompatDelegate`() = runTest {
        val initialPrefs = UserPreferencesEntity(languageCode = "en")
        every { repository.getUserPreferences() } returns flowOf(initialPrefs)
        coEvery { repository.saveUserPreferences(any()) } returns Unit
        
        viewModel = SettingsViewModel(repository)
        advanceUntilIdle()

        viewModel.onLanguageChanged("fr")
        advanceUntilIdle()

        coVerify { repository.saveUserPreferences(initialPrefs.copy(languageCode = "fr")) }
        coVerify { AppCompatDelegate.setApplicationLocales(any()) }
    }
}
