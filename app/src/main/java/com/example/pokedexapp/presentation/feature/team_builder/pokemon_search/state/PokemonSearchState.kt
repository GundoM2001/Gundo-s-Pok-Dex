package com.example.pokedexapp.presentation.feature.team_builder.pokemon_search.state

import com.example.pokedexapp.domain.model.PokemonResults

data class PokemonSearchState(
    val pokemonList: List<PokemonResults> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
