package com.example.pokedexapp.domain.repository

import com.example.pokedexapp.domain.model.PokemonResults
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {
    suspend fun addFavourite(pokemon: PokemonResults)
    suspend fun removeFavourite(pokemonId: Int)
    fun getFavouritePokemon(): Flow<List<PokemonResults>>
    fun getFavouritePokemonIds(): Flow<Set<Int>>
}
