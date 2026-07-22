package com.example.pokedexapp.data.repository

import com.example.pokedexapp.data.local.dao.FavouritePokemonDao
import com.example.pokedexapp.data.local.entities.FavouritePokemonEntity
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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val service: PokemonApiService,
    private val dao: FavouritePokemonDao
) : PokemonRepository {

    // In-memory caches
    private val detailsCache = ConcurrentHashMap<String, PokemonDetails>()
    private val speciesCache = ConcurrentHashMap<String, PokemonSpecies>()
    private val typeCache = ConcurrentHashMap<String, TypeDetails>()
    private val abilityCache = ConcurrentHashMap<String, AbilityDetails>()
    private val moveCache = ConcurrentHashMap<String, MoveDetails>()
    private val machineCache = ConcurrentHashMap<String, MachineDetails>()

    // Limit concurrency for batch operations
    private val networkSemaphore = Semaphore(5)

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
        detailsCache[url]?.let { return it }
        return networkSemaphore.withPermit {
            val response = service.getPokemonDetails(url)
            if (response.isSuccessful) {
                response.body()?.also { detailsCache[url] = it }
                    ?: throw Exception("Response body is null")
            } else {
                throw Exception("API Error: ${response.code()} ${response.message()}")
            }
        }
    }

    override suspend fun getPokemonSpecies(url: String): PokemonSpecies {
        speciesCache[url]?.let { return it }
        return networkSemaphore.withPermit {
            val response = service.getPokemonSpecies(url)
            if (response.isSuccessful) {
                response.body()?.also { speciesCache[url] = it }
                    ?: throw Exception("Response body is null")
            } else {
                throw Exception("API Error: ${response.code()} ${response.message()}")
            }
        }
    }

    override suspend fun getTypeDetails(url: String): TypeDetails {
        typeCache[url]?.let { return it }
        return networkSemaphore.withPermit {
            val response = service.getTypeDetails(url)
            if (response.isSuccessful) {
                response.body()?.also { typeCache[url] = it }
                    ?: throw Exception("Response body is null")
            } else {
                throw Exception("API Error: ${response.code()} ${response.message()}")
            }
        }
    }

    override suspend fun getAbilityDetails(url: String): AbilityDetails {
        abilityCache[url]?.let { return it }
        return networkSemaphore.withPermit {
            val response = service.getAbilityDetails(url)
            if (response.isSuccessful) {
                response.body()?.also { abilityCache[url] = it }
                    ?: throw Exception("Response body is null")
            } else {
                throw Exception("API Error: ${response.code()} ${response.message()}")
            }
        }
    }

    override suspend fun getMoveDetails(url: String): MoveDetails {
        moveCache[url]?.let { return it }
        return networkSemaphore.withPermit {
            val response = service.getMoveDetails(url)
            if (response.isSuccessful) {
                response.body()?.also { moveCache[url] = it }
                    ?: throw Exception("Response body is null")
            } else {
                throw Exception("API Error: ${response.code()} ${response.message()}")
            }
        }
    }

    override suspend fun getMachineDetails(url: String): MachineDetails {
        machineCache[url]?.let { return it }
        return networkSemaphore.withPermit {
            val response = service.getMachineDetails(url)
            if (response.isSuccessful) {
                response.body()?.also { machineCache[url] = it }
                    ?: throw Exception("Response body is null")
            } else {
                throw Exception("API Error: ${response.code()} ${response.message()}")
            }
        }
    }

    override suspend fun addFavourite(pokemon: PokemonResults) {
        dao.insertFavourite(
            FavouritePokemonEntity(
                id = pokemon.id,
                name = pokemon.name,
                url = pokemon.url,
                imageUrl = pokemon.imageUrl,
                types = pokemon.types
            )
        )
    }

    override suspend fun removeFavourite(pokemonId: Int) {
        dao.deleteFavouriteById(pokemonId)
    }

    override fun getFavouritePokemon(): Flow<List<PokemonResults>> {
        return dao.getFavourite().map { favourites ->
            favourites.map { entity ->
                PokemonResults(
                    name = entity.name,
                    url = entity.url,
                    imageUrl = entity.imageUrl,
                    types = entity.types
                )
            }
        }
    }

    override fun getFavouritePokemonIds(): Flow<Set<Int>> {
        return dao.getFavourite().map { favourites ->
            favourites.map { it.id }.toSet()
        }
    }
}
