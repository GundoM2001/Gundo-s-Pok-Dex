package com.example.pokedexapp.presentation

import com.example.pokedexapp.presentation.feature.team_builder.team_detail.ui.TeamDetailViewModel
import com.example.pokedexapp.presentation.feature.team_builder.team_detail.state.TeamDetailState

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.pokedexapp.data.local.entities.TeamEntity
import com.example.pokedexapp.data.local.entities.TeamPokemonEntity
import com.example.pokedexapp.data.local.entities.TeamWithPokemon
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
class TeamDetailViewModelTest {

    private val repository: TeamRepository = mockk()
    private val savedStateHandle: SavedStateHandle = SavedStateHandle()
    private lateinit var viewModel: TeamDetailViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val teamId = 1
    private val mockTeamWithPokemon = TeamWithPokemon(
        team = TeamEntity(teamId, "My Team"),
        pokemon = listOf(
            TeamPokemonEntity(id = 1, teamId = teamId, pokemonId = 1, pokemonName = "bulbasaur", slot = 1)
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle["teamId"] = teamId
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization should fetch team details`() = runTest {
        every { repository.getTeamWithPokemonByIdFlow(teamId) } returns flowOf(mockTeamWithPokemon)

        viewModel = TeamDetailViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(mockTeamWithPokemon, state.team)
            assertEquals(false, state.isLoading)
        }
    }

    @Test
    fun `removePokemon should call repository`() = runTest {
        every { repository.getTeamWithPokemonByIdFlow(teamId) } returns flowOf(mockTeamWithPokemon)
        coEvery { repository.removePokemonFromTeam(any()) } just Runs
        viewModel = TeamDetailViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        val memberToRemove = mockTeamWithPokemon.pokemon[0]

        viewModel.removePokemon(memberToRemove)
        advanceUntilIdle()

        coVerify { repository.removePokemonFromTeam(memberToRemove) }
    }

    @Test
    fun `initialization with invalid id should show error`() = runTest {
        savedStateHandle["teamId"] = 0

        viewModel = TeamDetailViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(true, state.error != null)
        }
    }
}
