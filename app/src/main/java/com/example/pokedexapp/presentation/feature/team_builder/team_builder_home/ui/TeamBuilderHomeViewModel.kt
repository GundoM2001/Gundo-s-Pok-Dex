package com.example.pokedexapp.presentation.feature.team_builder.team_builder_home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.data.local.entities.TeamEntity
import com.example.pokedexapp.domain.repository.TeamRepository
import com.example.pokedexapp.presentation.feature.team_builder.team_builder_home.state.TeamBuilderHomeState
import com.example.pokedexapp.utils.ErrorHandler
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
                _state.update { it.copy(teams = teams, isLoading = false, error = null) }
            }
            .catch { e ->
                _state.update { it.copy(isLoading = false, error = ErrorHandler.mapException(e)) }
            }
            .launchIn(viewModelScope)
    }

    fun onRetry() {
        // Handled by observation, but could force refresh if needed
    }

    val teams = state.map { it.teams }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val showCreateDialog = state.map { it.showCreateDialog }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val teamToDelete = state.map { it.teamToDelete }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

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

    fun onConfirmDelete(team: TeamEntity) {
        _state.update { it.copy(teamToDelete = team) }
    }

    fun onDismissDelete() {
        _state.update { it.copy(teamToDelete = null) }
    }

    fun deleteTeam() {
        viewModelScope.launch {
            _state.value.teamToDelete?.let {
                repository.deleteTeam(it)
            }
            onDismissDelete()
        }
    }
}
