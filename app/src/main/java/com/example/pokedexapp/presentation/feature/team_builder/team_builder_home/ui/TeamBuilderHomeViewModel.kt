package com.example.pokedexapp.presentation.feature.team_builder.team_builder_home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.domain.repository.TeamRepository
import com.example.pokedexapp.presentation.feature.team_builder.team_builder_home.state.TeamBuilderHomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamBuilderHomeViewModel @Inject constructor(
    private val repository: TeamRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TeamBuilderHomeState())
    val state = _state.asStateFlow()

    init {
        repository.getTeamsWithPokemon()
            .onEach { teams ->
                _state.update { it.copy(teams = teams) }
            }
            .launchIn(viewModelScope)
    }

    val teams = state.map { it.teams }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val showCreateDialog = state.map { it.showCreateDialog }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun onShowDialog() {
        _state.update { it.copy(showCreateDialog = true) }
    }

    fun onDismissDialog() {
        _state.update { it.copy(showCreateDialog = false) }
    }

    fun createTeam(name: String) {
        viewModelScope.launch {
            repository.createTeam(name)
            onDismissDialog()
        }
    }
}
