package com.example.pokedexapp.presentation

import android.util.Log
import com.example.pokedexapp.presentation.feature.pokemon_details.ui.PokemonDetailsViewModel
import com.example.pokedexapp.presentation.feature.pokemon_details.state.PokemonDetailsState

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.pokedexapp.domain.model.*
import com.example.pokedexapp.domain.repository.PokemonRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonDetailsViewModelTest {

    private val repository: PokemonRepository = mockk()
    private val savedStateHandle: SavedStateHandle = SavedStateHandle()
    private lateinit var viewModel: PokemonDetailsViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val pokemonUrl = "https://pokeapi.co/api/v2/pokemon/1/"
    
    private val mockPokemon: PokemonDetails = mockk(relaxed = true) {
        every { id } returns 1
        every { name } returns "bulbasaur"
        every { species } returns NamedApiResource("bulbasaur", "species_url")
        every { types } returns listOf(Type(1, NamedApiResource("grass", "url")))
        every { abilities } returns emptyList()
        every { moves } returns emptyList()
    }

    private val mockSpecies: PokemonSpecies = mockk(relaxed = true) {
        every { varieties } returns listOf(PokemonVariety(true, NamedApiResource("bulbasaur", pokemonUrl)))
    }

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        Dispatchers.setMain(testDispatcher)
        savedStateHandle["pokemonUrl"] = pokemonUrl
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization should fetch pokemon details successfully`() = runTest {
        coEvery { repository.getPokemonDetails(pokemonUrl) } returns mockPokemon
        coEvery { repository.getPokemonSpecies(any()) } returns mockSpecies
        coEvery { repository.getTypeDetails(any()) } returns mockk(relaxed = true)

        viewModel = PokemonDetailsViewModel(repository, savedStateHandle)

        viewModel.state.test {
            advanceUntilIdle()
            val state = expectMostRecentItem()
            assertEquals(mockPokemon, state.pokemonDetails)
            assertEquals(mockSpecies, state.pokemonSpecies)
            assertEquals(false, state.isLoading)
        }
    }

    @Test
    fun `initialization should set error state when fetching fails`() = runTest {
        coEvery { repository.getPokemonDetails(pokemonUrl) } throws Exception("Network error")

        viewModel = PokemonDetailsViewModel(repository, savedStateHandle)

        viewModel.state.test {
            advanceUntilIdle()
            val state = expectMostRecentItem()
            assertNotNull(state.error)
            assertEquals(false, state.isLoading)
        }
    }

    @Test
    fun `onTabSelected should update selected tab index`() = runTest {
        coEvery { repository.getPokemonDetails(pokemonUrl) } returns mockPokemon
        coEvery { repository.getPokemonSpecies(any()) } returns mockSpecies
        coEvery { repository.getTypeDetails(any()) } returns mockk(relaxed = true)
        viewModel = PokemonDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        viewModel.selectedTabIndex.test {
            assertEquals(0, awaitItem())

            viewModel.onTabSelected(1)
            advanceUntilIdle()

            assertEquals(1, awaitItem())
        }
    }

    @Test
    fun `onVariantChanged should update pokemon details and fetch new data`() = runTest {
        coEvery { repository.getPokemonDetails(pokemonUrl) } returns mockPokemon
        coEvery { repository.getPokemonSpecies(any()) } returns mockSpecies
        coEvery { repository.getTypeDetails(any()) } returns mockk(relaxed = true)
        viewModel = PokemonDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        val newVariant: PokemonDetails = mockk(relaxed = true) {
            every { name } returns "bulbasaur-mega"
            every { types } returns listOf(Type(1, NamedApiResource("grass", "url")))
            every { abilities } returns emptyList()
            every { moves } returns emptyList()
        }
        coEvery { repository.getTypeDetails(any()) } returns mockk(relaxed = true)

        viewModel.pokemonDetails.test {
            assertEquals(null, awaitItem())
            assertEquals(mockPokemon, awaitItem())

            viewModel.onVariantChanged(newVariant)
            advanceUntilIdle()

            assertEquals(newVariant, awaitItem())
        }
    }
}
