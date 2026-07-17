package com.example.pokedexapp.presentation.feature.pokemon_list.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.domain.model.PokemonListResponse
import com.example.pokedexapp.domain.model.PokemonResults
import com.example.pokedexapp.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(private val repository: PokemonRepository) :
    ViewModel() {
    private val _pokemonList = MutableStateFlow<List<PokemonResults>?>(null)
    val pokemonList = _pokemonList.asStateFlow()

    private val _nextUrl = MutableStateFlow<String?>(null)
    val nextUrl = _nextUrl.asStateFlow()

    private val _previousUrl = MutableStateFlow<String?>(null)
    val previousUrl = _previousUrl.asStateFlow()

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading = _loading.asStateFlow()

    init {
        fetchPokemonData()
    }

    fun fetchPokemonData(url: String? = null) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.getAllPokemon(url)
                _pokemonList.value = response.results
                _nextUrl.value = response.next
                _previousUrl.value = response.previous
            } catch (e: Exception) {
                Log.e("VM", "Error fetching data", e)
            } finally {
                _loading.value = false
            }
        }
    }
}