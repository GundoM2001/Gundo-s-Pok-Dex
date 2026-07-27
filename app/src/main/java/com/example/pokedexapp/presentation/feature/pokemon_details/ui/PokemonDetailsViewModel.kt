package com.example.pokedexapp.presentation.feature.pokemon_details.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.domain.model.*
import com.example.pokedexapp.domain.repository.PokemonRepository
import com.example.pokedexapp.presentation.feature.pokemon_details.state.PokemonDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(PokemonDetailsState())
    val state = _state.asStateFlow()

    init {
        savedStateHandle.get<String>("pokemonUrl")?.let { url ->
            fetchPokemonDetails(url)
        }
    }

    fun onTabSelected(index: Int) {
        _state.update { it.copy(selectedTabIndex = index) }
        if (index == 1) {
            _state.value.pokemonDetails?.let { 
                viewModelScope.launch {
                    fetchMoveDetails(it, essentialOnly = false) 
                }
            }
        }
    }

    fun onVariantChanged(details: PokemonDetails) {
        _state.update { it.copy(pokemonDetails = details) }
        fetchTypeAdvantages(details)
        fetchAbilityDetails(details)
        if (_state.value.selectedTabIndex == 1) {
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
                        if (!_state.value.moveDetails.containsKey(moveEntry.move.name)) {
                            try {
                                val moveInfo = pokemonRepository.getMoveDetails(moveEntry.move.url)
                                _state.update { s ->
                                    s.copy(moveDetails = s.moveDetails + (moveEntry.move.name to moveInfo))
                                }

                                // Also fetch machine details if applicable
                                moveInfo.machines.forEach { machineVer ->
                                    if (!_state.value.machineDetails.containsKey(machineVer.machine.url)) {
                                        try {
                                            val machine = pokemonRepository.getMachineDetails(machineVer.machine.url)
                                            _state.update { s ->
                                                s.copy(machineDetails = s.machineDetails + (machineVer.machine.url to machine))
                                            }
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
                _state.update { it.copy(abilityDetails = abilities) }
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
                _state.update { it.copy(typeAdvantages = advantages) }
            } catch (e: Exception) {
                Log.e("VM", "Error calculating type advantages", e)
            }
        }
    }

    private fun fetchPokemonDetails(url: String) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

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

                _state.update {
                    it.copy(
                        pokemonDetails = initialDetails,
                        pokemonSpecies = species,
                        pokemonVariants = variantDetails
                    )
                }
                
                fetchTypeAdvantages(initialDetails)
                fetchAbilityDetails(initialDetails)
                fetchMoveDetails(initialDetails, essentialOnly = true)

                viewModelScope.launch {
                    fetchMoveDetails(initialDetails, essentialOnly = false)
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "An unknown error occurred") }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    // Backward compatibility
    val pokemonDetails = state.map { it.pokemonDetails }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val pokemonSpecies = state.map { it.pokemonSpecies }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val pokemonVariants = state.map { it.pokemonVariants }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val typeAdvantages = state.map { it.typeAdvantages }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
    val abilityDetails = state.map { it.abilityDetails }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val moveDetails = state.map { it.moveDetails }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
    val machineDetails = state.map { it.machineDetails }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
    val selectedTabIndex = state.map { it.selectedTabIndex }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val loading = state.map { it.isLoading }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    val error = state.map { it.error }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
