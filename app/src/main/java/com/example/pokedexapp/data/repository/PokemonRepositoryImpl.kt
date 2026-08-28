package com.example.pokedexapp.data.repository

import com.example.pokedexapp.data.remote.api.PokemonApiService
import com.example.pokedexapp.domain.model.AbilityDetails
import com.example.pokedexapp.domain.model.MachineDetails
import com.example.pokedexapp.domain.model.MoveDetails
import com.example.pokedexapp.domain.model.PokemonDetails
import com.example.pokedexapp.domain.model.PokemonListResponse
import com.example.pokedexapp.domain.model.PokemonResults
import com.example.pokedexapp.domain.model.PokemonSpecies
import com.example.pokedexapp.domain.model.TypeDetails
import com.example.pokedexapp.domain.repository.PokemonRepository
import android.util.LruCache
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import retrofit2.HttpException
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val service: PokemonApiService
) : PokemonRepository {

    // In-memory caches with limits to balance memory usage
    private val detailsCache = LruCache<String, PokemonDetails>(100)
    private val speciesCache = LruCache<String, PokemonSpecies>(50)
    private val typeCache = LruCache<String, TypeDetails>(20)
    private val abilityCache = LruCache<String, AbilityDetails>(50)
    private val moveCache = LruCache<String, MoveDetails>(100)
    private val machineCache = LruCache<String, MachineDetails>(50)

    // Limit concurrency for batch operations
    private val networkSemaphore = Semaphore(5)

    override suspend fun getAllPokemon(url: String?): PokemonListResponse {
        val response = if (url != null) {
            service.getPokemonPage(url)
        } else {
            service.getAllPokemon()
        }

            if (response.isSuccessful) {
                val listResponse = response.body() ?: throw com.google.gson.JsonSyntaxException("Response body is null")
                val filteredResults = listResponse.results.filter { isBaseForm(it.url) }
            val updatedResults = enrichPokemonList(filteredResults)
            return listResponse.copy(results = updatedResults)
        } else {
            throw HttpException(response)
        }
    }

    override suspend fun getFullPokemonList(): List<PokemonResults> {
        val response = service.getAllPokemon(limit = 1500)
        if (response.isSuccessful) {
            val results = response.body()?.results ?: emptyList()
            return results.filter { isBaseForm(it.url) }
        } else {
            throw HttpException(response)
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
                        val details = getPokemonDetails(pokemon.url)
                        pokemon.copy(
                            imageUrl = details.sprites.other?.officialArtwork?.frontDefault,
                            types = details.types.map { it.type.name }
                        )
                    } catch (e: Exception) {
                        pokemon
                    }
                }
            }.awaitAll()
        }
    }

    override suspend fun getPokemonDetails(url: String): PokemonDetails {
        detailsCache.get(url)?.let { return it }
        return networkSemaphore.withPermit {
            val response = service.getPokemonDetails(url)
            if (response.isSuccessful) {
                response.body()?.also { detailsCache.put(url, it) }
                    ?: throw com.google.gson.JsonSyntaxException("Response body is null")
            } else {
                throw HttpException(response)
            }
        }
    }

    override suspend fun getPokemonSpecies(url: String): PokemonSpecies {
        speciesCache.get(url)?.let { return it }
        return networkSemaphore.withPermit {
            val response = service.getPokemonSpecies(url)
            if (response.isSuccessful) {
                response.body()?.also { speciesCache.put(url, it) }
                    ?: throw Exception("Response body is null")
            } else {
                throw HttpException(response)
            }
        }
    }

    override suspend fun getTypeDetails(url: String): TypeDetails {
        typeCache.get(url)?.let { return it }
        return networkSemaphore.withPermit {
            val response = service.getTypeDetails(url)
            if (response.isSuccessful) {
                response.body()?.also { typeCache.put(url, it) }
                    ?: throw Exception("Response body is null")
            } else {
                throw HttpException(response)
            }
        }
    }

    override suspend fun getAbilityDetails(url: String): AbilityDetails {
        abilityCache.get(url)?.let { return it }
        return networkSemaphore.withPermit {
            val response = service.getAbilityDetails(url)
            if (response.isSuccessful) {
                response.body()?.also { abilityCache.put(url, it) }
                    ?: throw Exception("Response body is null")
            } else {
                throw HttpException(response)
            }
        }
    }

    override suspend fun getMoveDetails(url: String): MoveDetails {
        moveCache.get(url)?.let { return it }
        return networkSemaphore.withPermit {
            val response = service.getMoveDetails(url)
            if (response.isSuccessful) {
                response.body()?.also { moveCache.put(url, it) }
                    ?: throw Exception("Response body is null")
            } else {
                throw HttpException(response)
            }
        }
    }

    override suspend fun getMachineDetails(url: String): MachineDetails {
        machineCache.get(url)?.let { return it }
        return networkSemaphore.withPermit {
            val response = service.getMachineDetails(url)
            if (response.isSuccessful) {
                response.body()?.also { machineCache.put(url, it) }
                    ?: throw Exception("Response body is null")
            } else {
                throw HttpException(response)
            }
        }
    }
}
