package com.example.pokedexapp.presentation.feature.favourite_pokemon.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.domain.model.PokemonResults
import com.example.pokedexapp.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritePokemonViewModel @Inject constructor(private val repository: PokemonRepository) :
    ViewModel() {

    private val _favouritePokemon = MutableStateFlow<List<PokemonResults>>(emptyList())
    val favouritePokemon: StateFlow<List<PokemonResults>> = _favouritePokemon.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchFavouritePokemon()
    }

    private fun fetchFavouritePokemon() {
        viewModelScope.launch {
            _isLoading.value = true
            // Placeholder data
            delay(1000)
            _favouritePokemon.value = listOf(
                PokemonResults(
                    name = "Bulbasaur",
                    url = "https://pokeapi.co/api/v2/pokemon/1/",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                    types = listOf("grass", "poison")
                ),
                PokemonResults(
                    name = "Charmander",
                    url = "https://pokeapi.co/api/v2/pokemon/4/",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png",
                    types = listOf("fire")
                ),
                PokemonResults(
                    name = "Squirtle",
                    url = "https://pokeapi.co/api/v2/pokemon/7/",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/7.png",
                    types = listOf("water")
                ),
                PokemonResults(
                    name = "Pikachu",
                    url = "https://pokeapi.co/api/v2/pokemon/25/",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png",
                    types = listOf("electric")
                )
            )
            _isLoading.value = false
        }
    }
}