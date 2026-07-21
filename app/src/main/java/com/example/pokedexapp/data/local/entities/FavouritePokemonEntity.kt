package com.example.pokedexapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_pokemon")
data class FavouritePokemonEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val url: String,
    val imageUrl: String?,
    val types: List<String>?
)