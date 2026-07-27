package com.example.pokedexapp.presentation.feature.team_builder.team_builder_home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun TeamBuilderHomeScreen(
    viewModel: TeamBuilderHomeViewModel = hiltViewModel(),
    onTeamClick: (String) -> Unit = {}
) {
    val teamList by viewModel.teams.collectAsState()

    TeamBuilderHomeScreenContent()
}

@Composable
fun TeamBuilderHomeScreenContent(){

}