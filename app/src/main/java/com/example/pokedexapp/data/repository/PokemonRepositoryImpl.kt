package com.example.pokedexapp.data.repository

import com.example.pokedexapp.data.remote.api.PokemonApiService
import com.example.pokedexapp.domain.model.PokemonDetails
import com.example.pokedexapp.domain.model.PokemonListResponse
import com.example.pokedexapp.domain.repository.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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
            val listResponse = response.body() ?: throw Exception("Response body is null")

            val updatedResults = coroutineScope {
                listResponse.results.map { pokemon ->
                    async {
                        try {
                            val detailsResponse = service.getPokemonDetails(pokemon.url)
                            if (detailsResponse.isSuccessful) {
                                val details = detailsResponse.body()
                                pokemon.copy(
                                    imageUrl = details?.sprites?.other?.officialArtwork?.frontDefault,
                                    types = details?.types?.map { it.type.name }
                                )
                            } else {
                                pokemon
                            }
                        } catch (e: Exception) {
                            pokemon
                        }
                    }
                }.awaitAll()
            }

            return listResponse.copy(results = updatedResults)
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
