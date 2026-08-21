package com.example.pokedexapp.presentation.feature.team_builder.pokemon_customization.state

import com.example.pokedexapp.data.local.entities.TeamPokemonEntity
import com.example.pokedexapp.domain.model.ItemDetails
import com.example.pokedexapp.domain.model.NamedApiResource
import com.example.pokedexapp.domain.model.PokemonDetails
import com.example.pokedexapp.utils.UiErrorMessage

data class PokemonCustomizationState(
    val pokemonDetails: PokemonDetails? = null,
    val member: TeamPokemonEntity? = null,
    val nickname: String? = null,
    val level: Int = 100,
    val selectedNature: String = "Hardy",
    val selectedAbility: String? = null,
    
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
    val speIv: Int = 31,
    
    val move1: String? = null,
    val move2: String? = null,
    val move3: String? = null,
    val move4: String? = null,
    
    val moveDetails: Map<String, com.example.pokedexapp.domain.model.MoveDetails> = emptyMap(),
    val moveSearchQuery: String = "",

    val heldItem: String? = null,
    val itemDetails: Map<String, ItemDetails> = emptyMap(),
    val itemSearchQuery: String = "",
    val availableItems: List<NamedApiResource> = emptyList(),
    val isItemsLoading: Boolean = false,
    
    val varieties: List<PokemonDetails> = emptyList(),
    
    val isLoading: Boolean = false,
    val error: UiErrorMessage? = null
)
