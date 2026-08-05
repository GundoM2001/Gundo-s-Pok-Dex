package com.example.pokedexapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey val id: Int = 0, // Singleton record
    val languageCode: String = "", // Empty string means System Default
    val themeMode: Int = 0 // 0: System, 1: Light, 2: Dark
)
