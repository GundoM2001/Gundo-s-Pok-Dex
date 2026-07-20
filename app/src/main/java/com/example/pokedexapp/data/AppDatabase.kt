package com.example.pokedexapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokedexapp.data.local.dao.FavouritePokemonDao
import com.example.pokedexapp.data.local.entities.FavouritePokemonEntity
import com.example.pokedexapp.data.local.converters.PokemonConverters

@Database(entities = [FavouritePokemonEntity::class], version = 1)
@TypeConverters(PokemonConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouritePokemonDao(): FavouritePokemonDao
}