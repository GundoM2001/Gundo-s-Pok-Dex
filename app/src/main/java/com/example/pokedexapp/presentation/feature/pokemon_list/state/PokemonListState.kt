package com.example.pokedexapp.presentation.feature.pokemon_list.state

import com.example.pokedexapp.domain.model.PokemonResults

data class PokemonListState(
    val pokemonList: List<PokemonResults>? = null,
    val favouriteIds: Set<Int> = emptySet(),
    val nextUrl: String? = null,
    val previousUrl: String? = null,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isEnriching: Boolean = false,
    val error: String? = null
)
