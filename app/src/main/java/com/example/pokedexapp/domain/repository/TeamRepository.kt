package com.example.pokedexapp.domain.repository

import com.example.pokedexapp.data.local.entities.TeamEntity
import com.example.pokedexapp.data.local.entities.TeamPokemonEntity
import kotlinx.coroutines.flow.Flow

interface TeamRepository {

    fun getTeams(): Flow<List<TeamEntity>>

    fun getPokemonForTeam(teamId: Int): Flow<List<TeamPokemonEntity>>

    suspend fun createTeam(name: String)

    suspend fun deleteTeam(team: TeamEntity)

    suspend fun addPokemonToTeam(teamId: Int, pokemonId: Int)

    suspend fun removePokemonFromTeam(teamPokemon: TeamPokemonEntity)
}