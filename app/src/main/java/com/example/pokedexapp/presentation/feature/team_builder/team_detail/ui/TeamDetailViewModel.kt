package com.example.pokedexapp.presentation.feature.team_builder.team_detail.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.data.local.entities.TeamPokemonEntity
import com.example.pokedexapp.domain.repository.TeamRepository
import com.example.pokedexapp.presentation.feature.team_builder.team_detail.state.TeamDetailState
import com.example.pokedexapp.utils.ErrorHandler
import com.example.pokedexapp.utils.UiErrorMessage
import com.example.pokedexapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamDetailViewModel @Inject constructor(
    private val repository: TeamRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val teamId: Int = savedStateHandle.get<Int>("teamId") ?: 0

    private val _state = MutableStateFlow(TeamDetailState())
    val state = _state.asStateFlow()

    // For backward compatibility with the screen if I don't want to update it yet
    val team = _state

    init {
        if (teamId != 0) {
            fetchTeam()
        } else {
            _state.update { it.copy(error = UiErrorMessage(R.string.error_unknown_title, R.string.unknown_error)) }
        }
    }

    private fun fetchTeam() {
        _state.update { it.copy(isLoading = true) }
        repository.getTeamWithPokemonByIdFlow(teamId)
            .onEach { result ->
                _state.update { it.copy(team = result, isLoading = false, error = null) }
            }
            .catch { e ->
                _state.update { it.copy(isLoading = false, error = ErrorHandler.mapException(e)) }
            }
            .launchIn(viewModelScope)
    }

    fun onRetry() {
        fetchTeam()
    }

    fun removePokemon(member: TeamPokemonEntity) {
        viewModelScope.launch {
            repository.removePokemonFromTeam(member)
        }
    }
}
