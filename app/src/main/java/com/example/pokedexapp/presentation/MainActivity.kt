package com.example.pokedexapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.os.LocaleListCompat
import com.example.pokedexapp.presentation.theme.PokeDexAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val preferences by viewModel.preferences.collectAsState()

            LaunchedEffect(preferences.languageCode) {
                val currentLocales = AppCompatDelegate.getApplicationLocales()
                if (currentLocales.toLanguageTags() != preferences.languageCode) {
                    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(preferences.languageCode)
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }
            }

            val darkTheme = when (preferences.themeMode) {
                1 -> false
                2 -> true
                else -> isSystemInDarkTheme()
            }

            PokeDexAppTheme(darkTheme = darkTheme) {
                MainScreen()
            }
        }
    }
}
