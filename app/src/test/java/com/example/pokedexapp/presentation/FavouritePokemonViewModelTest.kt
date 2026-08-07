package com.example.pokedexapp.presentation

import app.cash.turbine.test
import com.example.pokedexapp.domain.model.PokemonResults
import com.example.pokedexapp.domain.repository.FavouriteRepository
import com.example.pokedexapp.presentation.feature.favourite_pokemon.ui.FavouritePokemonViewModel
import com.example.pokedexapp.utils.ErrorHandler
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavouritePokemonViewModelTest {

    private val repository: FavouriteRepository = mockk()
    private lateinit var viewModel: FavouritePokemonViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial load should populate list when repository returns data`() = runTest {
        val pokemonList = listOf(PokemonResults(name = "Pikachu", url = "https://pokeapi.co/api/v2/pokemon/25/"))
        every { repository.getFavouritePokemon() } returns flowOf(pokemonList)

        viewModel = FavouritePokemonViewModel(repository)
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(pokemonList, state.favouritePokemon)
            assertFalse(state.isLoading)
            assertEquals(null, state.error)
        }
    }

    @Test
    fun `initial load should set error state when repository fails`() = runTest {
        val exception = Exception("Network error")
        every { repository.getFavouritePokemon() } returns flow { throw exception }

        viewModel = FavouritePokemonViewModel(repository)
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertNotNull(state.error)
            assertEquals(ErrorHandler.mapException(exception), state.error)
        }
    }
}
