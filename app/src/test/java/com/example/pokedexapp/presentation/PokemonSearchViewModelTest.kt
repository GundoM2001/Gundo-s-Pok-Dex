package com.example.pokedexapp.presentation

import com.example.pokedexapp.presentation.feature.team_builder.pokemon_search.ui.PokemonSearchViewModel
import com.example.pokedexapp.presentation.feature.team_builder.pokemon_search.state.PokemonSearchState

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.pokedexapp.domain.model.PokemonResults
import com.example.pokedexapp.domain.repository.PokemonRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonSearchViewModelTest {

    private val repository: PokemonRepository = mockk()
    private val savedStateHandle: SavedStateHandle = SavedStateHandle()
    private lateinit var viewModel: PokemonSearchViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val mockMasterList = listOf(
        PokemonResults("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
        PokemonResults("ivysaur", "https://pokeapi.co/api/v2/pokemon/2/"),
        PokemonResults("charmander", "https://pokeapi.co/api/v2/pokemon/4/")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle["teamId"] = 1
        savedStateHandle["slot"] = 1
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization should fetch master list and show first 50`() = runTest {
        coEvery { repository.getFullPokemonList() } returns mockMasterList
        coEvery { repository.enrichPokemonList(any()) } returns mockMasterList

        viewModel = PokemonSearchViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(mockMasterList, state.pokemonList)
            assertEquals(false, state.isLoading)
        }
    }

    @Test
    fun `onSearchQueryChanged should filter list`() = runTest {
        coEvery { repository.getFullPokemonList() } returns mockMasterList
        coEvery { repository.enrichPokemonList(any()) } answers { it.invocation.args[0] as List<PokemonResults> }
        viewModel = PokemonSearchViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        viewModel.onSearchQueryChanged("char")
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(1, state.pokemonList.size)
            assertEquals("charmander", state.pokemonList[0].name)
        }
    }

    @Test
    fun `search by ID should work`() = runTest {
        coEvery { repository.getFullPokemonList() } returns mockMasterList
        coEvery { repository.enrichPokemonList(any()) } answers { it.invocation.args[0] as List<PokemonResults> }
        viewModel = PokemonSearchViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        viewModel.onSearchQueryChanged("2")
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(1, state.pokemonList.size)
            assertEquals("ivysaur", state.pokemonList[0].name)
        }
    }
}
