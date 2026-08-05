package com.example.pokedexapp.domain.repository

import com.example.pokedexapp.data.local.entities.TeamEntity
import com.example.pokedexapp.data.local.entities.TeamPokemonEntity
import com.example.pokedexapp.data.local.entities.TeamWithPokemon
import kotlinx.coroutines.flow.Flow

interface TeamRepository {

    fun getTeams(): Flow<List<TeamEntity>>

    fun getTeamsWithPokemon(): Flow<List<TeamWithPokemon>>

    fun getPokemonForTeam(teamId: Int): Flow<List<TeamPokemonEntity>>

    suspend fun createTeam(name: String)

    suspend fun deleteTeam(team: TeamEntity)

    suspend fun addPokemonToTeam(teamId: Int, pokemonId: Int, pokemonName: String)

    suspend fun removePokemonFromTeam(teamPokemon: TeamPokemonEntity)

    fun getTeamMember(id: Int): Flow<TeamPokemonEntity?>

    suspend fun updateTeamMember(member: TeamPokemonEntity)

    suspend fun getTeamWithPokemonById(teamId: Int): TeamWithPokemon?

    fun getTeamWithPokemonByIdFlow(teamId: Int): Flow<TeamWithPokemon?>

    suspend fun addPokemonToTeamWithDetails(member: TeamPokemonEntity)
}
