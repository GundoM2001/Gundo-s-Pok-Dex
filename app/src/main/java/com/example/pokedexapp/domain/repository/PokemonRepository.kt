package com.example.pokedexapp.domain.repository

import com.example.pokedexapp.domain.model.AbilityDetails
import com.example.pokedexapp.domain.model.PokemonDetails
import com.example.pokedexapp.domain.model.PokemonListResponse
import com.example.pokedexapp.domain.model.PokemonResults
import com.example.pokedexapp.domain.model.PokemonSpecies
import com.example.pokedexapp.domain.model.TypeDetails

interface PokemonRepository {
    suspend fun getAllPokemon(url: String? = null): PokemonListResponse
    suspend fun getFullPokemonList(): List<PokemonResults>
    suspend fun enrichPokemonList(list: List<PokemonResults>): List<PokemonResults>
    suspend fun getPokemonDetails(url: String): PokemonDetails
    suspend fun getPokemonSpecies(url: String): PokemonSpecies
    suspend fun getTypeDetails(url: String): TypeDetails
    suspend fun getAbilityDetails(url: String): AbilityDetails
    suspend fun addFavourite(pokemon: PokemonResults)
}
