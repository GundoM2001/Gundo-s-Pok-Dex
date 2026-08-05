package com.example.pokedexapp.presentation.feature.team_builder.team_detail.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.domain.repository.TeamRepository
import com.example.pokedexapp.presentation.feature.team_builder.team_detail.state.TeamDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TeamDetailViewModel @Inject constructor(
    private val repository: TeamRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val teamId: Int = checkNotNull(savedStateHandle["teamId"])

    private val _state = MutableStateFlow(TeamDetailState())
    val state = _state.asStateFlow()

    // For backward compatibility with the screen if I don't want to update it yet
    val team = _state

    init {
        fetchTeam()
    }

    private fun fetchTeam() {
        _state.update { it.copy(isLoading = true) }
        repository.getTeamWithPokemonByIdFlow(teamId)
            .onEach { result ->
                _state.update { it.copy(team = result, isLoading = false) }
            }
            .catch { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
            .launchIn(viewModelScope)
    }
}
