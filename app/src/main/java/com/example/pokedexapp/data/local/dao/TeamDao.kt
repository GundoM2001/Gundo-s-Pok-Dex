package com.example.pokedexapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.pokedexapp.data.local.entities.TeamEntity
import com.example.pokedexapp.data.local.entities.TeamPokemonEntity
import com.example.pokedexapp.data.local.entities.TeamWithPokemon
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {
    // Team Queries

    @Query("SELECT * FROM teams")
    fun getTeams(): Flow<List<TeamEntity>>

    @Transaction
    @Query("SELECT * FROM teams")
    fun getTeamsWithPokemon(): Flow<List<TeamWithPokemon>>

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
    fun getPokemonForTeam(teamId: Int): Flow<List<TeamPokemonEntity>>

    @Query("""
        SELECT COUNT(*)
        FROM team_pokemon
        WHERE teamId = :teamId
    """)
    suspend fun getTeamSize(teamId: Int): Int

    @Query("SELECT * FROM team_pokemon WHERE id = :id")
    fun getTeamMember(id: Int): Flow<TeamPokemonEntity?>

    @Update
    suspend fun updateTeamMember(member: TeamPokemonEntity)

    @Transaction
    @Query("SELECT * FROM teams WHERE id = :teamId")
    suspend fun getTeamWithPokemonById(teamId: Int): TeamWithPokemon?

    @Transaction
    @Query("SELECT * FROM teams WHERE id = :teamId")
    fun getTeamWithPokemonByIdFlow(teamId: Int): Flow<TeamWithPokemon?>
}
