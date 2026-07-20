package com.example.pokedexapp.presentation.feature.favourite_pokemon.ui

import androidx.lifecycle.ViewModel
import com.example.pokedexapp.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavouritePokemonViewModel @Inject constructor(private val repository: PokemonRepository) :
    ViewModel() {

    init {
        fetchFavouritePokemon()
    }

    private fun fetchFavouritePokemon() {

    }
}