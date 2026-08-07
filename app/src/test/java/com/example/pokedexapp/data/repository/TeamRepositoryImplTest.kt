package com.example.pokedexapp.data.repository

import app.cash.turbine.test
import com.example.pokedexapp.data.local.dao.TeamDao
import com.example.pokedexapp.data.local.entities.TeamEntity
import com.example.pokedexapp.data.local.entities.TeamPokemonEntity
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TeamRepositoryImplTest {

    private lateinit var repository: TeamRepositoryImpl
    private val dao: TeamDao = mockk()

    @Before
    fun setup() {
        repository = TeamRepositoryImpl(dao)
    }

    @Test
    fun `createTeam calls dao createTeam`() = runTest {
        coEvery { dao.createTeam(any()) } returns 1L

        repository.createTeam("My Team")

        coVerify {
            dao.createTeam(match { it.name == "My Team" })
        }
    }

    @Test
    fun `deleteTeam calls dao deleteTeam`() = runTest {
        val team = TeamEntity(1, "My Team")
        coEvery { dao.deleteTeam(team) } just Runs

        repository.deleteTeam(team)

        coVerify { dao.deleteTeam(team) }
    }

    @Test
    fun `addPokemonToTeam adds pokemon when team not full`() = runTest {
        coEvery { dao.getTeamSize(1) } returns 3
        coEvery { dao.addPokemon(any()) } just Runs

        repository.addPokemonToTeam(1, 25, "Pikachu")

        coVerify {
            dao.addPokemon(match {
                it.teamId == 1 && it.pokemonId == 25 && it.pokemonName == "Pikachu" && it.slot == 3
            })
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `addPokemonToTeam throws exception when team is full`() = runTest {
        coEvery { dao.getTeamSize(1) } returns 6

        repository.addPokemonToTeam(1, 25, "Pikachu")
    }

    @Test
    fun `getPokemonForTeam returns flow from dao`() = runTest {
        val members = listOf(
            TeamPokemonEntity(1, 1, 25, "Pikachu", 0)
        )
        every { dao.getPokemonForTeam(1) } returns flowOf(members)

        repository.getPokemonForTeam(1).test {
            assertEquals(members, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getTeamMember returns flow from dao`() = runTest {
        val member = TeamPokemonEntity(1, 1, 25, "Pikachu", 0)
        every { dao.getTeamMember(1) } returns flowOf(member)

        repository.getTeamMember(1).test {
            assertEquals(member, awaitItem())
            awaitComplete()
        }
    }
}
