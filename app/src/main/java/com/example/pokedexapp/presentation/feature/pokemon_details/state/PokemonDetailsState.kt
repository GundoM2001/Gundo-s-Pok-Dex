package com.example.pokedexapp.presentation.feature.pokemon_details.state

import com.example.pokedexapp.domain.model.*
import com.example.pokedexapp.utils.UiErrorMessage

data class PokemonDetailsState(
    val pokemonDetails: PokemonDetails? = null,
    val pokemonSpecies: PokemonSpecies? = null,
    val pokemonVariants: List<PokemonDetails> = emptyList(),
    val typeAdvantages: Map<String, Double> = emptyMap(),
    val abilityDetails: List<AbilityDetails> = emptyList(),
    val moveDetails: Map<String, MoveDetails> = emptyMap(),
    val machineDetails: Map<String, MachineDetails> = emptyMap(),
    val selectedTabIndex: Int = 0,
    val isLoading: Boolean = false,
    val error: UiErrorMessage? = null
)
