package com.example.pokedexapp.presentation.feature.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.pokedexapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val preferences by viewModel.preferences.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            ThemeSection(
                selectedTheme = preferences.themeMode,
                onThemeSelected = { viewModel.onThemeChanged(it) }
            )

            LanguageSection(
                selectedLanguage = preferences.languageCode,
                onLanguageSelected = { viewModel.onLanguageChanged(it) }
            )
        }
    }
}

@Composable
fun ThemeSection(
    selectedTheme: Int,
    onThemeSelected: (Int) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.theme_setting),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ThemeOption(
                label = stringResource(R.string.theme_system),
                isSelected = selectedTheme == 0,
                onClick = { onThemeSelected(0) },
                modifier = Modifier.weight(1f)
            )
            ThemeOption(
                label = stringResource(R.string.theme_light),
                isSelected = selectedTheme == 1,
                onClick = { onThemeSelected(1) },
                modifier = Modifier.weight(1f)
            )
            ThemeOption(
                label = stringResource(R.string.theme_dark),
                isSelected = selectedTheme == 2,
                onClick = { onThemeSelected(2) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ThemeOption(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = label, style = MaterialTheme.typography.labelMedium, maxLines = 1)
        }
    }
}

@Composable
fun LanguageSection(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    val languages = listOf(
        "en" to "English",
        "af" to "Afrikaans",
        "ve" to "Venda",
        "fr" to "Français",
        "it" to "Italiano",
        "ja" to "日本語",
        "de" to "Deutsch",
        "es" to "Español",
        "pt" to "Português",
        "ar" to "العربية",
        "ru" to "Русский",
        "pl" to "Polski",
        "hi" to "हिन्दी",
        "zh" to "中文"
    )

    var expanded by remember { mutableStateOf(false) }
    val currentLanguageName = languages.find { it.first == selectedLanguage }?.second ?: "English"

    Column {
        Text(
            text = stringResource(R.string.language_setting),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        Box {
            OutlinedTextField(
                value = currentLanguageName,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                readOnly = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                languages.forEach { (code, name) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            onLanguageSelected(code)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
