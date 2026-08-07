package com.example.pokedexapp.presentation.feature.team_builder.pokemon_search.state

import com.example.pokedexapp.domain.model.PokemonResults
import com.example.pokedexapp.utils.UiErrorMessage

data class PokemonSearchState(
    val pokemonList: List<PokemonResults> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: UiErrorMessage? = null
)
