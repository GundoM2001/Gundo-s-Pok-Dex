package com.example.pokedexapp.presentation.feature.pokemon_list.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.domain.model.PokemonResults
import com.example.pokedexapp.domain.repository.FavouriteRepository
import com.example.pokedexapp.domain.repository.PokemonRepository
import com.example.pokedexapp.presentation.feature.pokemon_list.state.PokemonListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository,
    private val favouriteRepository: FavouriteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PokemonListState())
    val state = _state.asStateFlow()

    private val _masterPokemonList = MutableStateFlow<List<PokemonResults>>(emptyList())
    private val _paginatedList = MutableStateFlow<List<PokemonResults>?>(null)

    val filteredPokemonList: StateFlow<List<PokemonResults>?> = combine(
        _paginatedList,
        state.map { it.searchQuery }.distinctUntilChanged()
    ) { paginatedList, query ->
        query to paginatedList
    }.flatMapLatest { (query, paginatedList) ->
        flow {
            if (query.isBlank()) {
                _state.update { it.copy(isEnriching = false) }
                emit(paginatedList)
            } else {
                delay(300)
                _state.update { it.copy(isEnriching = true) }
                val filtered = _masterPokemonList.value.filter { pokemon ->
                    val pokemonId = pokemon.url.trimEnd('/').split('/').lastOrNull() ?: ""
                    pokemon.name.contains(query, ignoreCase = true) || pokemonId == query
                }.take(20)

                val enriched = repository.enrichPokemonList(filtered)
                emit(enriched)
                _state.update { it.copy(isEnriching = false) }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        fetchPokemonData()
        fetchMasterList()
        
        favouriteRepository.getFavouritePokemonIds()
            .onEach { ids ->
                _state.update { it.copy(favouriteIds = ids) }
            }
            .launchIn(viewModelScope)
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
        _state.update { it.copy(searchQuery = query) }
    }

    fun fetchPokemonData(url: String? = null) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                val response = repository.getAllPokemon(url)
                _paginatedList.value = response.results
                _state.update {
                    it.copy(
                        nextUrl = response.next,
                        previousUrl = response.previous,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("VM", "Error fetching data", e)
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun toggleFavourite(pokemon: PokemonResults) {
        viewModelScope.launch {
            if (_state.value.favouriteIds.contains(pokemon.id)) {
                favouriteRepository.removeFavourite(pokemon.id)
            } else {
                favouriteRepository.addFavourite(pokemon)
            }
        }
    }

    // For backward compatibility with Screen
    val searchQuery = state.map { it.searchQuery }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")
    val nextUrl = state.map { it.nextUrl }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val previousUrl = state.map { it.previousUrl }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val loading = state.map { it.isLoading }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    val isEnriching = state.map { it.isEnriching }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    val favouriteIds = state.map { it.favouriteIds }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())
}
