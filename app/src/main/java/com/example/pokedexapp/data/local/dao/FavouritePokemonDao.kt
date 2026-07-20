package com.example.pokedexapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokedexapp.data.local.entities.FavouritePokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritePokemonDao {

    @Query("SELECT * FROM favorite_pokemon")
    fun getFavourite(): Flow<List<FavouritePokemonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(pokemon: FavouritePokemonEntity)

    @Delete
    suspend fun deleteFavourite(pokemon: FavouritePokemonEntity)

    @Query("SELECT EXISTS (SELECT 1 FROM favorite_pokemon WHERE id = :pokemonId)")
    fun isFavourite(pokemonId: Int): Flow<Boolean>

}