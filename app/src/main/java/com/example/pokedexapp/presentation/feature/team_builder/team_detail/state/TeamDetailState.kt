package com.example.pokedexapp.presentation.feature.team_builder.team_detail.state

import com.example.pokedexapp.data.local.entities.TeamWithPokemon
import com.example.pokedexapp.utils.UiErrorMessage

data class TeamDetailState(
    val team: TeamWithPokemon? = null,
    val isLoading: Boolean = false,
    val error: UiErrorMessage? = null
)
