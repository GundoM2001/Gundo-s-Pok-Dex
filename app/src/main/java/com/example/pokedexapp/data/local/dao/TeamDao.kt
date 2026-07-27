package com.example.pokedexapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pokedexapp.data.local.entities.TeamEntity
import com.example.pokedexapp.data.local.entities.TeamPokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {
    // Team Queries

    @Query("SELECT * FROM teams")
    fun getTeams(): Flow<List<TeamEntity>>

    @Insert
    suspend fun createTeam(team: TeamEntity): Long

    @Delete
    suspend fun deleteTeam(team: TeamEntity)

    //Team Pokemon Queries

    @Insert
    suspend fun addPokemon(teamPokemon: TeamPokemonEntity)

    @Delete
    suspend fun removePokemon(teamPokemon: TeamPokemonEntity)

    @Query("""
        SELECT * FROM team_pokemon
        WHERE teamId = :teamId
        ORDER BY slot
    """)
    fun getPokemonForTeam(teamId: Int):Flow <List<TeamPokemonEntity>>

    @Query("""
    SELECT COUNT(*)
    FROM team_pokemon
    WHERE teamId = :teamId
""")
    suspend fun getTeamSize(teamId: Int): Int

}