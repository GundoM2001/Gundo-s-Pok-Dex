package com.example.pokedexapp.presentation.feature.pokemon_list.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.domain.model.PokemonListResponse
import com.example.pokedexapp.domain.model.PokemonResults
import com.example.pokedexapp.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class PokemonListViewModel @Inject constructor(private val repository: PokemonRepository) :
    ViewModel() {
    private val _pokemonList = MutableStateFlow<List<PokemonResults>?>(null)
    val pokemonList = _pokemonList.asStateFlow()

    private val _masterPokemonList = MutableStateFlow<List<PokemonResults>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _nextUrl = MutableStateFlow<String?>(null)
    val nextUrl = _nextUrl.asStateFlow()

    private val _previousUrl = MutableStateFlow<String?>(null)
    val previousUrl = _previousUrl.asStateFlow()

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading = _loading.asStateFlow()

    private val _isEnriching = MutableStateFlow<Boolean>(false)
    val isEnriching = _isEnriching.asStateFlow()

    val filteredPokemonList: StateFlow<List<PokemonResults>?> = combine(
        _pokemonList,
        _searchQuery
    ) { paginatedList, query ->
        query to paginatedList
    }.flatMapLatest { (query, paginatedList) ->
        flow {
            if (query.isBlank()) {
                _isEnriching.value = false
                emit(paginatedList)
            } else {
                delay(300) // Debounce enrichment
                _isEnriching.value = true
                val filtered = _masterPokemonList.value.filter { pokemon ->
                    val pokemonId = pokemon.url.trimEnd('/').split('/').lastOrNull() ?: ""
                    pokemon.name.contains(query, ignoreCase = true) || pokemonId == query
                }.take(20) // Limit enrichment to first 20 matches for speed

                val enriched = repository.enrichPokemonList(filtered)
                emit(enriched)
                _isEnriching.value = false
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val favouriteIds = repository
        .getFavouritePokemonIds()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    init {
        fetchPokemonData()
        fetchMasterList()
    }

    private fun fetchMasterList() {
        viewModelScope.launch {
            try {
                _masterPokemonList.value = repository.getFullPokemonList()
            } catch (e: Exception) {
                Log.e("VM", "Error fetching master list", e)
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
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

    fun toggleFavourite(pokemon: PokemonResults) {
        viewModelScope.launch {
            if (favouriteIds.value.contains(pokemon.id)) {
                repository.removeFavourite(pokemon.id)
            } else {
                repository.addFavourite(pokemon)
            }
        }
    }
}