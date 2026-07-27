package com.example.pokedexapp.presentation.feature.team_builder.team_builder_home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.domain.repository.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamBuilderHomeViewModel @Inject constructor(private val repository: TeamRepository):
    ViewModel(){

    val teams = repository.getTeams()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private fun createTeam(name: String){
        viewModelScope.launch {
            repository.createTeam(name)
        }
    }

}