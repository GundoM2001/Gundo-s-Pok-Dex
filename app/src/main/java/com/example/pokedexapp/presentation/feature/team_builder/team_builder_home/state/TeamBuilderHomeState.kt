package com.example.pokedexapp.presentation.feature.team_builder.team_builder_home.state

import com.example.pokedexapp.data.local.entities.TeamWithPokemon

data class TeamBuilderHomeState(
    val teams: List<TeamWithPokemon> = emptyList(),
    val showCreateDialog: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
