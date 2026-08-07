package com.example.pokedexapp.presentation.feature.team_builder.team_builder_home.state

import com.example.pokedexapp.data.local.entities.TeamEntity
import com.example.pokedexapp.data.local.entities.TeamWithPokemon
import com.example.pokedexapp.utils.UiErrorMessage

data class TeamBuilderHomeState(
    val teams: List<TeamWithPokemon> = emptyList(),
    val showCreateDialog: Boolean = false,
    val teamToDelete: TeamEntity? = null,
    val isLoading: Boolean = false,
    val error: UiErrorMessage? = null
)
