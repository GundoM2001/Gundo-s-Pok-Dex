package com.example.pokedexapp.presentation.feature.favourite_pokemon.state

import com.example.pokedexapp.domain.model.PokemonResults
import com.example.pokedexapp.utils.UiErrorMessage

data class FavouritePokemonState(
    val favouritePokemon: List<PokemonResults> = emptyList(),
    val isLoading: Boolean = false,
    val error: UiErrorMessage? = null
)
