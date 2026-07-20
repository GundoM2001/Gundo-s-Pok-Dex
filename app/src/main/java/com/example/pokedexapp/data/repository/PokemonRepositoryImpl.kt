package com.example.pokedexapp.data.repository

import com.example.pokedexapp.data.local.dao.FavouritePokemonDao
import com.example.pokedexapp.data.remote.api.PokemonApiService
import com.example.pokedexapp.domain.model.AbilityDetails
import com.example.pokedexapp.domain.model.PokemonDetails
import com.example.pokedexapp.domain.model.PokemonListResponse
import com.example.pokedexapp.domain.model.PokemonResults
import com.example.pokedexapp.domain.model.PokemonSpecies
import com.example.pokedexapp.domain.model.TypeDetails
import com.example.pokedexapp.domain.repository.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val service: PokemonApiService,
    private val dao : FavouritePokemonDao
) : PokemonRepository {

    override suspend fun getAllPokemon(url: String?): PokemonListResponse {
        val response = if (url != null) {
            service.getPokemonPage(url)
        } else {
            service.getAllPokemon()
        }

        if (response.isSuccessful) {
            val listResponse = response.body() ?: throw Exception("Response body is null")
            val filteredResults = listResponse.results.filter { isBaseForm(it.url) }
            val updatedResults = enrichPokemonList(filteredResults)
            return listResponse.copy(results = updatedResults)
        } else {
            throw Exception("API Error: ${response.code()} ${response.message()}")
        }
    }

    override suspend fun getFullPokemonList(): List<PokemonResults> {
        val response = service.getAllPokemon(limit = 1500)
        if (response.isSuccessful) {
            val results = response.body()?.results ?: emptyList()
            return results.filter { isBaseForm(it.url) }
        } else {
            throw Exception("API Error: ${response.code()} ${response.message()}")
        }
    }

    private fun isBaseForm(url: String): Boolean {
        return try {
            val id = url.trimEnd('/').split('/').last().toInt()
            id < 10000
        } catch (e: Exception) {
            true // Default to true if parsing fails
        }
    }

    override suspend fun enrichPokemonList(list: List<PokemonResults>): List<PokemonResults> {
        return coroutineScope {
            list.map { pokemon ->
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
    }

    override suspend fun getPokemonDetails(url: String): PokemonDetails {
        val response = service.getPokemonDetails(url)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Response body is null")
        } else {
            throw Exception("API Error: ${response.code()} ${response.message()}")
        }
    }

    override suspend fun getPokemonSpecies(url: String): PokemonSpecies {
        val response = service.getPokemonSpecies(url)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Response body is null")
        } else {
            throw Exception("API Error: ${response.code()} ${response.message()}")
        }
    }

    override suspend fun getTypeDetails(url: String): TypeDetails {
        val response = service.getTypeDetails(url)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Response body is null")
        } else {
            throw Exception("API Error: ${response.code()} ${response.message()}")
        }
    }

    override suspend fun getAbilityDetails(url: String): AbilityDetails {
        val response = service.getAbilityDetails(url)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Response body is null")
        } else {
            throw Exception("API Error: ${response.code()} ${response.message()}")
        }
    }

    override suspend fun addFavourite(pokemon: PokemonResults) {

    }
}
