package com.example.pokedexapp.domain.repository

import com.example.pokedexapp.domain.model.PokemonListResponse

interface PokemonRepository {
    suspend fun getAllPokemon(url: String? = null): PokemonListResponse
}
