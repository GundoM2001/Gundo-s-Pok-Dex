package com.example.pokedexapp.presentation.feature.favourite_pokemon.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.domain.model.PokemonResults
import com.example.pokedexapp.domain.repository.FavouriteRepository
import com.example.pokedexapp.presentation.feature.favourite_pokemon.state.FavouritePokemonState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritePokemonViewModel @Inject constructor(
    private val repository: FavouriteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FavouritePokemonState())
    val state = _state.asStateFlow()

    init {
        repository.getFavouritePokemon()
            .onEach { pokemon ->
                _state.update { it.copy(favouritePokemon = pokemon) }
            }
            .launchIn(viewModelScope)
    }

    val favouritePokemon = state.map { it.favouritePokemon }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isLoading = state.map { it.isLoading }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
}
