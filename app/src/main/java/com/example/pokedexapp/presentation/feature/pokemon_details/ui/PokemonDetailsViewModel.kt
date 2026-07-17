package com.example.pokedexapp.presentation.feature.pokemon_details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.domain.model.PokemonDetails
import com.example.pokedexapp.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        savedStateHandle.get<String>("pokemonUrl")?.let { url ->
            fetchPokemonDetails(url)
        }
    }

    private fun fetchPokemonDetails(url: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                val details = pokemonRepository.getPokemonDetails(url)
                _pokemonDetails.value = details
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            } finally {
                _loading.value = false
            }
        }
    }
}
