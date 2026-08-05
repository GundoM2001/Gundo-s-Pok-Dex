package com.example.pokedexapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokedexapp.data.local.dao.FavouritePokemonDao
import com.example.pokedexapp.data.local.entities.FavouritePokemonEntity
import com.example.pokedexapp.data.local.converters.PokemonConverters
import com.example.pokedexapp.data.local.dao.TeamDao
import com.example.pokedexapp.data.local.entities.TeamEntity
import com.example.pokedexapp.data.local.entities.TeamPokemonEntity

@Database(
    entities = [FavouritePokemonEntity::class, TeamEntity::class, TeamPokemonEntity::class],
    version = 7,
    exportSchema = true
)
@TypeConverters(PokemonConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouritePokemonDao(): FavouritePokemonDao

    abstract fun teamDao(): TeamDao

}