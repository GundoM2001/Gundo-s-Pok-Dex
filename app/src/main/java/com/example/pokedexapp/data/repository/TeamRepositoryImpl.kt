package com.example.pokedexapp.data.repository

import com.example.pokedexapp.data.local.dao.TeamDao
import com.example.pokedexapp.data.local.entities.TeamEntity
import com.example.pokedexapp.data.local.entities.TeamPokemonEntity
import com.example.pokedexapp.data.local.entities.TeamWithPokemon
import com.example.pokedexapp.domain.repository.TeamRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TeamRepositoryImpl @Inject constructor(
    private val dao: TeamDao
) : TeamRepository {

    override fun getTeams() = dao.getTeams()

    override fun getTeamsWithPokemon() = dao.getTeamsWithPokemon()

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

    override suspend fun addPokemonToTeam(teamId: Int, pokemonId: Int, pokemonName: String) {
        val size = dao.getTeamSize(teamId)

        if (size >= 6) {
            throw IllegalStateException("Team is already full")
        }

        dao.addPokemon(
            TeamPokemonEntity(
                teamId = teamId,
                pokemonId = pokemonId,
                pokemonName = pokemonName,
                slot = size
            )
        )
    }

    override suspend fun removePokemonFromTeam(teamPokemon: TeamPokemonEntity) {
        dao.removePokemon(teamPokemon)
    }

    override fun getTeamMember(id: Int): Flow<TeamPokemonEntity?> {
        return dao.getTeamMember(id)
    }

    override suspend fun updateTeamMember(member: TeamPokemonEntity) {
        dao.updateTeamMember(member)
    }

    override suspend fun getTeamWithPokemonById(teamId: Int): TeamWithPokemon? {
        return dao.getTeamWithPokemonById(teamId)
    }

    override fun getTeamWithPokemonByIdFlow(teamId: Int): Flow<TeamWithPokemon?> {
        return dao.getTeamWithPokemonByIdFlow(teamId)
    }

    override suspend fun addPokemonToTeamWithDetails(member: TeamPokemonEntity) {
        dao.addPokemon(member)
    }
}
