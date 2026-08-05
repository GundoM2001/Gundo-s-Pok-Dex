package com.example.pokedexapp.presentation.feature.team_builder.pokemon_search.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.domain.model.PokemonResults
import com.example.pokedexapp.domain.repository.PokemonRepository
import com.example.pokedexapp.presentation.feature.team_builder.pokemon_search.state.PokemonSearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonSearchViewModel @Inject constructor(
    private val repository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val teamId: Int = checkNotNull(savedStateHandle["teamId"])
    val slot: Int = checkNotNull(savedStateHandle["slot"])

    private val _masterPokemonList = MutableStateFlow<List<PokemonResults>>(emptyList())
    
    private val _state = MutableStateFlow(PokemonSearchState())
    val state = _state.asStateFlow()

    init {
        fetchMasterList()
        setupSearchLogic()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setupSearchLogic() {
        combine(_masterPokemonList, state.map { it.searchQuery }.distinctUntilChanged()) { list, query ->
            if (query.isBlank()) {
                list.take(50)
            } else {
                list.filter { 
                    val id = it.url.trimEnd('/').split('/').lastOrNull() ?: ""
                    it.name.contains(query, ignoreCase = true) || id == query
                }.take(50)
            }
        }.flatMapLatest { filtered ->
            flow {
                emit(filtered)
                if (filtered.isNotEmpty()) {
                    val enriched = repository.enrichPokemonList(filtered)
                    emit(enriched)
                }
            }
        }.onEach { list ->
            _state.update { it.copy(pokemonList = list) }
        }.launchIn(viewModelScope)
    }

    private fun fetchMasterList() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                _masterPokemonList.value = repository.getFullPokemonList()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }
}
