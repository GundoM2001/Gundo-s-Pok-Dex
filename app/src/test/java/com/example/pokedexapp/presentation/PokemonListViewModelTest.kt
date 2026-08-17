package com.example.pokedexapp.presentation

import android.util.Log
import app.cash.turbine.test
import com.example.pokedexapp.domain.model.PokemonListResponse
import com.example.pokedexapp.domain.model.PokemonResults
import com.example.pokedexapp.domain.repository.FavouriteRepository
import com.example.pokedexapp.domain.repository.PokemonRepository
import com.example.pokedexapp.presentation.feature.pokemon_list.ui.PokemonListViewModel
import com.example.pokedexapp.utils.ErrorHandler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonListViewModelTest {

    private val repository: PokemonRepository = mockk()
    private val favouriteRepository: FavouriteRepository = mockk()
    private lateinit var viewModel: PokemonListViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val pokemonResults = listOf(
        PokemonResults(name = "Bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/"),
        PokemonResults(name = "Ivysaur", url = "https://pokeapi.co/api/v2/pokemon/2/")
    )

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        Dispatchers.setMain(testDispatcher)
        
        coEvery { repository.getAllPokemon(any()) } returns PokemonListResponse(2, null, null, pokemonResults)
        coEvery { repository.getFullPokemonList() } returns pokemonResults
        every { favouriteRepository.getFavouritePokemonIds() } returns flowOf(emptySet())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchPokemonData success should update paginated list`() = runTest {
        val response = PokemonListResponse(2, "next", "prev", pokemonResults)
        coEvery { repository.getAllPokemon(null) } returns response

        viewModel = PokemonListViewModel(repository, favouriteRepository)
        
        viewModel.filteredPokemonList.test {
            advanceUntilIdle()
            assertEquals(null, awaitItem()) 
            assertEquals(pokemonResults, awaitItem()) 
            
            val state = viewModel.state.value
            assertEquals("next", state.nextUrl)
            assertEquals("prev", state.previousUrl)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `fetchPokemonData error should set error state`() = runTest {
        val exception = Exception("API error")
        coEvery { repository.getAllPokemon(null) } throws exception

        viewModel = PokemonListViewModel(repository, favouriteRepository)
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(ErrorHandler.mapException(exception), state.error)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `onSearchQueryChanged should trigger filteredPokemonList enrichment after delay`() = runTest {
        val query = "Bulb"
        val enriched = listOf(pokemonResults[0].copy(imageUrl = "some_url"))
        
        coEvery { repository.enrichPokemonList(any()) } returns enriched
        
        viewModel = PokemonListViewModel(repository, favouriteRepository)

        viewModel.filteredPokemonList.test {
            advanceUntilIdle()
            assertEquals(null, awaitItem()) 
            assertEquals(pokemonResults, awaitItem()) 

            viewModel.onSearchQueryChanged(query)
            
            advanceTimeBy(300)
            advanceUntilIdle()
            
            coVerify { repository.enrichPokemonList(any()) }
            assertEquals(enriched, awaitItem())
        }
    }

    @Test
    fun `toggleFavourite should add favourite if not already present`() = runTest {
        val pokemon = pokemonResults[0]
        every { favouriteRepository.getFavouritePokemonIds() } returns flowOf(emptySet())
        coEvery { favouriteRepository.addFavourite(pokemon) } returns Unit
        
        viewModel = PokemonListViewModel(repository, favouriteRepository)
        advanceUntilIdle()

        viewModel.toggleFavourite(pokemon)
        advanceUntilIdle()

        coVerify { favouriteRepository.addFavourite(pokemon) }
    }

    @Test
    fun `toggleFavourite should remove favourite if already present`() = runTest {
        val pokemon = pokemonResults[0]
        every { favouriteRepository.getFavouritePokemonIds() } returns flowOf(setOf(pokemon.id))
        coEvery { favouriteRepository.removeFavourite(pokemon.id) } returns Unit
        
        viewModel = PokemonListViewModel(repository, favouriteRepository)
        advanceUntilIdle() 

        viewModel.toggleFavourite(pokemon)
        advanceUntilIdle()

        coVerify { favouriteRepository.removeFavourite(pokemon.id) }
    }
}
