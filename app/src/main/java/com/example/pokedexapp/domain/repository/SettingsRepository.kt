package com.example.pokedexapp.domain.repository

import com.example.pokedexapp.data.local.entities.UserPreferencesEntity
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getUserPreferences(): Flow<UserPreferencesEntity?>
    suspend fun saveUserPreferences(preferences: UserPreferencesEntity)
}
