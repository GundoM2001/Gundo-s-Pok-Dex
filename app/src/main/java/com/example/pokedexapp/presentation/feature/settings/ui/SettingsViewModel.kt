package com.example.pokedexapp.presentation.feature.settings.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.data.local.entities.UserPreferencesEntity
import com.example.pokedexapp.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

    val preferences: StateFlow<UserPreferencesEntity> = repository.getUserPreferences()
        .map { it ?: UserPreferencesEntity() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferencesEntity()
        )

    fun onThemeChanged(themeMode: Int) {
        viewModelScope.launch {
            val current = preferences.value
            repository.saveUserPreferences(current.copy(themeMode = themeMode))
        }
    }

    fun onLanguageChanged(languageCode: String) {
        viewModelScope.launch {
            val current = preferences.value
            repository.saveUserPreferences(current.copy(languageCode = languageCode))

            // Apply language globally
            val appLocale: LocaleListCompat = if (languageCode.isEmpty()) {
                LocaleListCompat.getEmptyLocaleList()
            } else {
                LocaleListCompat.forLanguageTags(languageCode)
            }
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
    }
}
