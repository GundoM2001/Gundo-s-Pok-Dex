package com.example.pokedexapp.presentation.feature.pokemon_details.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.domain.model.AbilityDetails
import com.example.pokedexapp.domain.model.PokemonDetails
import com.example.pokedexapp.domain.model.PokemonSpecies
import com.example.pokedexapp.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        _selectedTabIndex.value = index
    }

    fun onVariantChanged(details: PokemonDetails) {
        _pokemonDetails.value = details
        fetchTypeAdvantages(details)
        fetchAbilityDetails(details)
    }

    private fun fetchAbilityDetails(details: PokemonDetails) {
        viewModelScope.launch {
            try {
                val abilities = coroutineScope {
                    details.abilities.map { ability ->
                        async { pokemonRepository.getAbilityDetails(ability.ability.url) }
                    }.awaitAll()
                }
                _abilityDetails.value = abilities
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
                _typeAdvantages.value = advantages
            } catch (e: Exception) {
                Log.e("VM", "Error calculating type advantages", e)
            }
        }
    }

    private fun fetchPokemonDetails(url: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

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

                _pokemonDetails.value = initialDetails
                _pokemonSpecies.value = species
                _pokemonVariants.value = variantDetails
                
                fetchTypeAdvantages(initialDetails)
                fetchAbilityDetails(initialDetails)

                Log.d("DETAILS", "fetchPokemonDetails: Variants loaded: ${variantDetails.map { "${it.name} (ID: ${it.id})" }}")

            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            } finally {
                _loading.value = false
            }
        }
    }
}
