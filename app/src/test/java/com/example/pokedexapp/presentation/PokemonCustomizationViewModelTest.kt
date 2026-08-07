package com.example.pokedexapp.presentation

import com.example.pokedexapp.presentation.feature.team_builder.pokemon_customization.ui.PokemonCustomizationViewModel
import com.example.pokedexapp.presentation.feature.team_builder.pokemon_customization.state.PokemonCustomizationState

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.pokedexapp.data.local.entities.TeamPokemonEntity
import com.example.pokedexapp.domain.model.*
import com.example.pokedexapp.domain.repository.PokemonRepository
import com.example.pokedexapp.domain.repository.TeamRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonCustomizationViewModelTest {

    private val pokemonRepository: PokemonRepository = mockk()
    private val teamRepository: TeamRepository = mockk()
    private val savedStateHandle: SavedStateHandle = SavedStateHandle()
    private lateinit var viewModel: PokemonCustomizationViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val pokemonId = 1
    private val teamId = 1
    private val slot = 1
    
    private val mockPokemon: PokemonDetails = mockk(relaxed = true) {
        coEvery { id } returns 1
        coEvery { name } returns "bulbasaur"
        coEvery { species } returns NamedApiResource("bulbasaur", "species_url")
        coEvery { abilities } returns listOf(Ability(NamedApiResource("overgrow", "url"), false, 1))
        coEvery { moves } returns emptyList()
    }

    private val mockSpecies: PokemonSpecies = mockk(relaxed = true) {
        coEvery { varieties } returns listOf(PokemonVariety(true, NamedApiResource("bulbasaur", "url")))
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle["pokemonId"] = pokemonId
        savedStateHandle["teamId"] = teamId
        savedStateHandle["slot"] = slot
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization should load pokemon data`() = runTest {
        coEvery { pokemonRepository.getPokemonDetails(any()) } returns mockPokemon
        coEvery { pokemonRepository.getPokemonSpecies(any()) } returns mockSpecies

        viewModel = PokemonCustomizationViewModel(pokemonRepository, teamRepository, savedStateHandle)
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(mockPokemon, state.pokemonDetails)
            assertEquals("bulbasaur", state.pokemonDetails?.name)
            assertEquals(false, state.isLoading)
        }
    }

    @Test
    fun `loadData should load existing member when memberId is provided`() = runTest {
        val memberId = 123
        savedStateHandle["memberId"] = memberId
        val existingMember = TeamPokemonEntity(id = memberId, teamId = teamId, pokemonId = pokemonId, pokemonName = "Bulby", slot = slot, nickname = "Bulby")
        
        coEvery { pokemonRepository.getPokemonDetails(any()) } returns mockPokemon
        coEvery { pokemonRepository.getPokemonSpecies(any()) } returns mockSpecies
        every { teamRepository.getTeamMember(memberId) } returns flowOf(existingMember)

        viewModel = PokemonCustomizationViewModel(pokemonRepository, teamRepository, savedStateHandle)
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertEquals("Bulby", state.nickname)
            assertEquals(existingMember, state.member)
        }
    }

    @Test
    fun `onNicknameChanged should update state`() = runTest {
        coEvery { pokemonRepository.getPokemonDetails(any()) } returns mockPokemon
        coEvery { pokemonRepository.getPokemonSpecies(any()) } returns mockSpecies
        viewModel = PokemonCustomizationViewModel(pokemonRepository, teamRepository, savedStateHandle)
        advanceUntilIdle()

        viewModel.onNicknameChanged("New Nickname")

        assertEquals("New Nickname", viewModel.state.value.nickname)
    }

    @Test
    fun `onLevelChanged should update state if valid`() = runTest {
        coEvery { pokemonRepository.getPokemonDetails(any()) } returns mockPokemon
        coEvery { pokemonRepository.getPokemonSpecies(any()) } returns mockSpecies
        viewModel = PokemonCustomizationViewModel(pokemonRepository, teamRepository, savedStateHandle)
        advanceUntilIdle()

        viewModel.onLevelChanged(50)
        assertEquals(50, viewModel.state.value.level)

        viewModel.onLevelChanged(101) 
        assertEquals(50, viewModel.state.value.level)
    }

    @Test
    fun `onMoveSelected should handle unique move logic`() = runTest {
        coEvery { pokemonRepository.getPokemonDetails(any()) } returns mockPokemon
        coEvery { pokemonRepository.getPokemonSpecies(any()) } returns mockSpecies
        viewModel = PokemonCustomizationViewModel(pokemonRepository, teamRepository, savedStateHandle)
        advanceUntilIdle()

        viewModel.onMoveSelected(1, "Tackle")
        viewModel.onMoveSelected(2, "Tackle") 

        assertEquals(null, viewModel.state.value.move1)
        assertEquals("Tackle", viewModel.state.value.move2)
    }

    @Test
    fun `save should call repository update when editing existing member`() = runTest {
        val memberId = 123
        savedStateHandle["memberId"] = memberId
        val existingMember = TeamPokemonEntity(id = memberId, teamId = teamId, pokemonId = pokemonId, pokemonName = "bulbasaur", slot = slot)
        
        coEvery { pokemonRepository.getPokemonDetails(any()) } returns mockPokemon
        coEvery { pokemonRepository.getPokemonSpecies(any()) } returns mockSpecies
        every { teamRepository.getTeamMember(memberId) } returns flowOf(existingMember)
        coEvery { teamRepository.updateTeamMember(any()) } just Runs

        viewModel = PokemonCustomizationViewModel(pokemonRepository, teamRepository, savedStateHandle)
        advanceUntilIdle()

        var successCalled = false
        viewModel.save { successCalled = true }
        advanceUntilIdle()

        coVerify { teamRepository.updateTeamMember(any()) }
        assertEquals(true, successCalled)
    }
}
