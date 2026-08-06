package com.example.pokedexapp.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Embedded
import androidx.room.Relation

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "team_pokemon",
    foreignKeys = [
        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["id"],
            childColumns = ["teamId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("teamId")
    ]
)
data class TeamPokemonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val teamId: Int,
    val pokemonId: Int,
    val pokemonName: String,
    val slot: Int,
    val nickname: String? = null,
    val level: Int = 100,
    val nature: String = "Hardy",
    val ability: String? = null,
    
    // Moves
    val move1: String? = null,
    val move2: String? = null,
    val move3: String? = null,
    val move4: String? = null,
    
    // EVs
    val hpEv: Int = 0,
    val atkEv: Int = 0,
    val defEv: Int = 0,
    val spaEv: Int = 0,
    val spdEv: Int = 0,
    val speEv: Int = 0,
    
    // IVs
    val hpIv: Int = 31,
    val atkIv: Int = 31,
    val defIv: Int = 31,
    val spaIv: Int = 31,
    val spdIv: Int = 31,
    val speIv: Int = 31
)

data class TeamWithPokemon(
    @Embedded val team: TeamEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "teamId"
    )
    val pokemon: List<TeamPokemonEntity>
)