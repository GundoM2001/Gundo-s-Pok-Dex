package com.example.pokedexapp.data.repository

import com.example.pokedexapp.data.local.dao.UserPreferencesDao
import com.example.pokedexapp.data.local.entities.UserPreferencesEntity
import com.example.pokedexapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dao: UserPreferencesDao
) : SettingsRepository {
    override fun getUserPreferences(): Flow<UserPreferencesEntity?> {
        return dao.getUserPreferences()
    }

    override suspend fun saveUserPreferences(preferences: UserPreferencesEntity) {
        dao.saveUserPreferences(preferences)
    }
}
