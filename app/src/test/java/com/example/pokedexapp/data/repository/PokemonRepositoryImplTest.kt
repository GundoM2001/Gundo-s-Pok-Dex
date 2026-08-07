package com.example.pokedexapp.data.repository

import com.example.pokedexapp.data.remote.api.PokemonApiService
import com.example.pokedexapp.domain.model.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class PokemonRepositoryImplTest {

    private lateinit var repository: PokemonRepositoryImpl
    private val service: PokemonApiService = mockk()

    @Before
    fun setup() {
        repository = PokemonRepositoryImpl(service)
    }

    @Test
    fun `getAllPokemon returns enriched list on success`() = runTest {
        val pokemonResults = listOf(
            PokemonResults("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/")
        )
        val listResponse = PokemonListResponse(1, null, null, pokemonResults)
        val details = mockk<PokemonDetails>(relaxed = true) {
            every { sprites.other?.officialArtwork?.frontDefault } returns "image_url"
            every { types } returns listOf(Type(1, NamedApiResource("grass", "")))
        }

        coEvery { service.getAllPokemon() } returns Response.success(listResponse)
        coEvery { service.getPokemonDetails(any()) } returns Response.success(details)

        val result = repository.getAllPokemon(null)

        assertEquals(1, result.results.size)
        assertEquals("image_url", result.results[0].imageUrl)
        assertEquals(listOf("grass"), result.results[0].types)
    }

    @Test(expected = HttpException::class)
    fun `getAllPokemon throws HttpException on 404 error`() = runTest {
        coEvery { service.getAllPokemon() } returns Response.error(404, "".toResponseBody())

        repository.getAllPokemon(null)
    }

    @Test(expected = HttpException::class)
    fun `getAllPokemon throws HttpException on 500 error`() = runTest {
        coEvery { service.getAllPokemon() } returns Response.error(500, "".toResponseBody())

        repository.getAllPokemon(null)
    }

    @Test
    fun `getFullPokemonList returns filtered list on success`() = runTest {
        val pokemonResults = listOf(
            PokemonResults("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
            PokemonResults("mega-bulbasaur", "https://pokeapi.co/api/v2/pokemon/10001/")
        )
        val listResponse = PokemonListResponse(2, null, null, pokemonResults)

        coEvery { service.getAllPokemon(limit = 1500) } returns Response.success(listResponse)

        val result = repository.getFullPokemonList()

        assertEquals(1, result.size)
        assertEquals("bulbasaur", result[0].name)
    }

    @Test(expected = HttpException::class)
    fun `getFullPokemonList throws HttpException on error`() = runTest {
        coEvery { service.getAllPokemon(limit = 1500) } returns Response.error(500, "".toResponseBody())

        repository.getFullPokemonList()
    }

    @Test
    fun `getPokemonDetails returns details and caches them`() = runTest {
        val url = "https://pokeapi.co/api/v2/pokemon/1/"
        val details = mockk<PokemonDetails>(relaxed = true)

        coEvery { service.getPokemonDetails(url) } returns Response.success(details)

        val result1 = repository.getPokemonDetails(url)
        val result2 = repository.getPokemonDetails(url)

        assertEquals(details, result1)
        assertEquals(details, result2)
        coVerify(exactly = 1) { service.getPokemonDetails(url) }
    }

    @Test(expected = HttpException::class)
    fun `getPokemonDetails throws HttpException on error`() = runTest {
        val url = "https://pokeapi.co/api/v2/pokemon/1/"
        coEvery { service.getPokemonDetails(url) } returns Response.error(404, "".toResponseBody())

        repository.getPokemonDetails(url)
    }
}
