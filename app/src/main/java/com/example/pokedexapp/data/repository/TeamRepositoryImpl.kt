package com.example.pokedexapp.data.repository

import com.example.pokedexapp.data.local.dao.TeamDao
import com.example.pokedexapp.data.local.entities.TeamEntity
import com.example.pokedexapp.data.local.entities.TeamPokemonEntity
import com.example.pokedexapp.domain.repository.TeamRepository
import javax.inject.Inject

class TeamRepositoryImpl @Inject constructor(
    private val dao: TeamDao
) : TeamRepository {

    override fun getTeams() = dao.getTeams()

    override fun getPokemonForTeam(teamId: Int) = dao.getPokemonForTeam(teamId)

    override suspend fun createTeam(name: String) {
        dao.createTeam(
            TeamEntity(
                name = name
            )
        )
    }

    override suspend fun deleteTeam(team: TeamEntity) {
        dao.deleteTeam(team)
    }

    override suspend fun addPokemonToTeam(teamId: Int, pokemonId: Int) {

        val size = dao.getTeamSize(teamId)

        if (size >= 6) {
            throw IllegalStateException("Team is already full")
        }

        dao.addPokemon(
            TeamPokemonEntity(
                teamId = teamId,
                pokemonId = pokemonId,
                slot = size
            )
        )
    }

    override suspend fun removePokemonFromTeam(teamPokemon: TeamPokemonEntity) {
        dao.removePokemon(teamPokemon)
    }
}
