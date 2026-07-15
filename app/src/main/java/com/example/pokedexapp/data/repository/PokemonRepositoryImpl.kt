package com.example.pokedexapp.data.repository

import com.example.pokedexapp.data.remote.api.PokemonApiService
import com.example.pokedexapp.domain.model.PokemonDetails
import com.example.pokedexapp.domain.model.PokemonListResponse
import com.example.pokedexapp.domain.repository.PokemonRepository
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val service: PokemonApiService
) : PokemonRepository {

    override suspend fun getAllPokemon(url: String?): PokemonListResponse {
        val response = if (url != null) {
            service.getPokemonPage(url)
        } else {
            service.getAllPokemon()
        }

        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Response body is null")
        } else {
            throw Exception("API Error: ${response.code()} ${response.message()}")
        }
    }

    override suspend fun getPokemonDetails(url: String): PokemonDetails {
        val response = service.getPokemonDetails(url)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Response body is null")
        } else {
            throw Exception("API Error: ${response.code()} ${response.message()}")
        }
    }
}
