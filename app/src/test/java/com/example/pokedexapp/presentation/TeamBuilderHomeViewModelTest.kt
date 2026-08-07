package com.example.pokedexapp.presentation

import com.example.pokedexapp.presentation.feature.team_builder.team_builder_home.ui.TeamBuilderHomeViewModel
import com.example.pokedexapp.presentation.feature.team_builder.team_builder_home.state.TeamBuilderHomeState

import app.cash.turbine.test
import com.example.pokedexapp.data.local.entities.TeamEntity
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
class TeamBuilderHomeViewModelTest {

    private val repository: TeamRepository = mockk()
    private lateinit var viewModel: TeamBuilderHomeViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val mockTeams = listOf(
        TeamWithPokemon(TeamEntity(1, "Team 1"), emptyList()),
        TeamWithPokemon(TeamEntity(2, "Team 2"), emptyList())
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { repository.getTeamsWithPokemon() } returns flowOf(mockTeams)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization should observe teams from repository`() = runTest {
        viewModel = TeamBuilderHomeViewModel(repository)
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(mockTeams, state.teams)
            assertEquals(false, state.isLoading)
        }
    }

    @Test
    fun `createTeam should call repository and dismiss dialog`() = runTest {
        coEvery { repository.createTeam(any()) } just Runs
        viewModel = TeamBuilderHomeViewModel(repository)
        advanceUntilIdle()

        viewModel.showCreateDialog.test {
            assertEquals(false, awaitItem())

            viewModel.onShowDialog()
            assertEquals(true, awaitItem())
            
            viewModel.createTeam("New Team")
            
            assertEquals(false, awaitItem())
            coVerify { repository.createTeam("New Team") }
        }
    }

    @Test
    fun `onConfirmDelete should set teamToDelete`() = runTest {
        viewModel = TeamBuilderHomeViewModel(repository)
        val teamToDelete = mockTeams[0].team

        viewModel.teamToDelete.test {
            assertEquals(null, awaitItem())

            viewModel.onConfirmDelete(teamToDelete)

            assertEquals(teamToDelete, awaitItem())
        }
    }

    @Test
    fun `deleteTeam should call repository and clear teamToDelete`() = runTest {
        coEvery { repository.deleteTeam(any()) } just Runs
        viewModel = TeamBuilderHomeViewModel(repository)
        val teamToDelete = mockTeams[0].team

        viewModel.teamToDelete.test {
            assertEquals(null, awaitItem())
            
            viewModel.onConfirmDelete(teamToDelete)
            assertEquals(teamToDelete, awaitItem())

            viewModel.deleteTeam()

            assertEquals(null, awaitItem())
            coVerify { repository.deleteTeam(teamToDelete) }
        }
    }
}
