package com.example.pokedexapp.presentation.feature.pokemon_details.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.domain.model.AbilityDetails
import com.example.pokedexapp.domain.model.MachineDetails
import com.example.pokedexapp.domain.model.MoveDetails
import com.example.pokedexapp.domain.model.PokemonDetails
import com.example.pokedexapp.domain.model.PokemonSpecies
import com.example.pokedexapp.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _pokemonDetails = MutableStateFlow<PokemonDetails?>(null)
    val pokemonDetails = _pokemonDetails.asStateFlow()

    private val _pokemonSpecies = MutableStateFlow<PokemonSpecies?>(null)
    val pokemonSpecies = _pokemonSpecies.asStateFlow()

    private val _pokemonVariants = MutableStateFlow<List<PokemonDetails>>(emptyList())
    val pokemonVariants = _pokemonVariants.asStateFlow()

    private val _typeAdvantages = MutableStateFlow<Map<String, Double>>(emptyMap())
    val typeAdvantages = _typeAdvantages.asStateFlow()

    private val _abilityDetails = MutableStateFlow<List<AbilityDetails>>(emptyList())
    val abilityDetails = _abilityDetails.asStateFlow()

    private val _moveDetails = MutableStateFlow<Map<String, MoveDetails>>(emptyMap())
    val moveDetails = _moveDetails.asStateFlow()

    private val _machineDetails = MutableStateFlow<Map<String, MachineDetails>>(emptyMap())
    val machineDetails = _machineDetails.asStateFlow()

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex = _selectedTabIndex.asStateFlow()

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        savedStateHandle.get<String>("pokemonUrl")?.let { url ->
            fetchPokemonDetails(url)
        }
    }

    fun onTabSelected(index: Int) {
        _selectedTabIndex.update { index }
        if (index == 1) {
            _pokemonDetails.value?.let { 
                viewModelScope.launch {
                    fetchMoveDetails(it, essentialOnly = false) 
                }
            }
        }
    }

    fun onVariantChanged(details: PokemonDetails) {
        _pokemonDetails.update { details }
        fetchTypeAdvantages(details)
        fetchAbilityDetails(details)
        if (_selectedTabIndex.value == 1) {
            viewModelScope.launch {
                fetchMoveDetails(details, essentialOnly = false)
            }
        }
    }

    private suspend fun fetchMoveDetails(details: PokemonDetails, essentialOnly: Boolean) {
        try {
            val methods = if (essentialOnly) {
                listOf("level-up")
            } else {
                listOf("level-up", "machine", "tutor", "evolution")
            }

            val movesToFetch = details.moves.filter { move ->
                move.versionGroupDetails.any { it.moveLearnMethod.name in methods }
            }

            coroutineScope {
                movesToFetch.map { moveEntry ->
                    async {
                        if (!_moveDetails.value.containsKey(moveEntry.move.name)) {
                            try {
                                val moveInfo = pokemonRepository.getMoveDetails(moveEntry.move.url)
                                _moveDetails.update { it + (moveEntry.move.name to moveInfo) }

                                // Also fetch machine details if applicable
                                moveInfo.machines.forEach { machineVer ->
                                    if (!_machineDetails.value.containsKey(machineVer.machine.url)) {
                                        try {
                                            val machine = pokemonRepository.getMachineDetails(machineVer.machine.url)
                                            _machineDetails.update { it + (machineVer.machine.url to machine) }
                                        } catch (e: Exception) {
                                            Log.e("VM", "Error fetching machine details", e)
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e("VM", "Error fetching move details for ${moveEntry.move.name}", e)
                            }
                        }
                    }
                }.awaitAll()
            }
        } catch (e: Exception) {
            Log.e("VM", "Error in fetchMoveDetails", e)
        }
    }

    private fun fetchAbilityDetails(details: PokemonDetails) {
        viewModelScope.launch {
            try {
                val abilities = coroutineScope {
                    details.abilities.map { ability ->
                        async { pokemonRepository.getAbilityDetails(ability.ability.url) }
                    }.awaitAll()
                }
                _abilityDetails.update { abilities }
            } catch (e: Exception) {
                Log.e("VM", "Error fetching ability details", e)
            }
        }
    }

    private fun fetchTypeAdvantages(details: PokemonDetails) {
        viewModelScope.launch {
            try {
                val typeDetailsList = coroutineScope {
                    details.types.map { type ->
                        async { pokemonRepository.getTypeDetails(type.type.url) }
                    }.awaitAll()
                }

                val advantages = mutableMapOf<String, Double>()
                // Initialize all types with 1.0
                val allTypes = listOf(
                    "normal", "fire", "water", "electric", "grass", "ice", "fighting",
                    "poison", "ground", "flying", "psychic", "bug", "rock", "ghost",
                    "dragon", "dark", "steel", "fairy"
                )
                allTypes.forEach { advantages[it] = 1.0 }

                typeDetailsList.forEach { typeDetail ->
                    typeDetail.damageRelations.doubleDamageFrom.forEach { 
                        advantages[it.name] = (advantages[it.name] ?: 1.0) * 2.0 
                    }
                    typeDetail.damageRelations.halfDamageFrom.forEach { 
                        advantages[it.name] = (advantages[it.name] ?: 1.0) * 0.5 
                    }
                    typeDetail.damageRelations.noDamageFrom.forEach { 
                        advantages[it.name] = (advantages[it.name] ?: 1.0) * 0.0 
                    }
                }
                _typeAdvantages.update { advantages }
            } catch (e: Exception) {
                Log.e("VM", "Error calculating type advantages", e)
            }
        }
    }

    private fun fetchPokemonDetails(url: String) {
        viewModelScope.launch {
            try {
                _loading.update { true }
                _error.update { null }

                val initialDetails = pokemonRepository.getPokemonDetails(url)
                
                val species = pokemonRepository.getPokemonSpecies(initialDetails.species.url)
                
                val variantDetails = coroutineScope {
                    val filteredVarieties = species.varieties.filter { !it.pokemon.name.contains("totem") }
                    
                    filteredVarieties
                        .filter { it.pokemon.url != url }
                        .map { variety ->
                            async {
                                pokemonRepository.getPokemonDetails(variety.pokemon.url)
                            }
                        }
                        .awaitAll()
                        .let { details ->
                            if (!initialDetails.name.contains("totem")) {
                                details.plus(initialDetails)
                            } else {
                                details
                            }
                        }
                        .sortedBy { details ->
                            species.varieties.indexOfFirst { it.pokemon.name == details.name }
                        }
                }

                _pokemonDetails.update { initialDetails }
                _pokemonSpecies.update { species }
                _pokemonVariants.update { variantDetails }
                
                fetchTypeAdvantages(initialDetails)
                fetchAbilityDetails(initialDetails)

                // Wait for essential moves (Level Up) before showing the screen
                fetchMoveDetails(initialDetails, essentialOnly = true)

                // Load the rest of the moves in the background
                viewModelScope.launch {
                    fetchMoveDetails(initialDetails, essentialOnly = false)
                }

                Log.d("DETAILS", "fetchPokemonDetails: Variants and essential moves loaded.")

            } catch (e: Exception) {
                _error.update { e.message ?: "An unknown error occurred" }
            } finally {
                _loading.update { false }
            }
        }
    }
}
