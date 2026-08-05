package com.example.pokedexapp.presentation.feature.team_builder.team_builder_home.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.pokedexapp.data.local.entities.TeamEntity
import com.example.pokedexapp.data.local.entities.TeamPokemonEntity
import com.example.pokedexapp.data.local.entities.TeamWithPokemon
import com.example.pokedexapp.presentation.feature.team_builder.team_builder_home.ui.components.TeamCard
import com.example.pokedexapp.presentation.theme.PokeDexAppTheme

@Composable
fun TeamBuilderHomeScreen(
    viewModel: TeamBuilderHomeViewModel = hiltViewModel(),
    onTeamClick: (Int) -> Unit = {}
) {
    val teamList by viewModel.teams.collectAsState()
    val showDialog by viewModel.showCreateDialog.collectAsState()

    TeamBuilderHomeScreenContent(
        teamList = teamList,
        showDialog = showDialog,
        onAddClick = { viewModel.onShowDialog() },
        onDismissDialog = { viewModel.onDismissDialog() },
        onCreateTeam = { viewModel.createTeam(it) },
        onTeamClick = onTeamClick,
        onEditTeam = { /* Rename logic if needed */ }
    )
}

@Composable
fun TeamBuilderHomeScreenContent(
    teamList: List<TeamWithPokemon>,
    showDialog: Boolean,
    onAddClick: () -> Unit,
    onDismissDialog: () -> Unit,
    onCreateTeam: (String) -> Unit,
    onTeamClick: (Int) -> Unit,
    onEditTeam: (TeamEntity) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Team")
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (teamList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No teams created yet.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(teamList) { teamWithPokemon ->
                        TeamCard(
                            teamWithPokemon = teamWithPokemon,
                            onClick = { onTeamClick(teamWithPokemon.team.id) },
                            onEditClick = { onEditTeam(teamWithPokemon.team) }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        CreateTeamDialog(
            onDismiss = onDismissDialog,
            onConfirm = onCreateTeam
        )
    }
}

@Composable
fun CreateTeamDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var teamName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Team") },
        text = {
            Column {
                Text("Enter a name for your team:")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = teamName,
                    onValueChange = { teamName = it },
                    placeholder = { Text("Team Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (teamName.isNotBlank()) onConfirm(teamName) },
                enabled = teamName.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TeamBuilderHomeScreenContentPreview() {
    val sampleTeams = listOf(
        TeamWithPokemon(
            team = TeamEntity(id = 1, name = "Kanto Team"),
            pokemon = listOf(
                TeamPokemonEntity(teamId = 1, pokemonId = 1, pokemonName = "bulbasaur", slot = 0),
                TeamPokemonEntity(teamId = 1, pokemonId = 4, pokemonName = "charmander", slot = 1),
                TeamPokemonEntity(teamId = 1, pokemonId = 7, pokemonName = "squirtle", slot = 2)
            )
        ),
        TeamWithPokemon(
            team = TeamEntity(id = 2, name = "My Favourites"),
            pokemon = listOf(
                TeamPokemonEntity(teamId = 2, pokemonId = 25, pokemonName = "pikachu", slot = 0),
                TeamPokemonEntity(teamId = 2, pokemonId = 133, pokemonName = "eevee", slot = 1)
            )
        ),
        TeamWithPokemon(
            team = TeamEntity(id = 3, name = "Empty Team"),
            pokemon = emptyList()
        )
    )

    PokeDexAppTheme {
        TeamBuilderHomeScreenContent(
            teamList = sampleTeams,
            showDialog = false,
            onAddClick = {},
            onDismissDialog = {},
            onCreateTeam = {},
            onTeamClick = {},
            onEditTeam = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TeamBuilderHomeScreenContentEmptyPreview() {
    PokeDexAppTheme {
        TeamBuilderHomeScreenContent(
            teamList = emptyList(),
            showDialog = false,
            onAddClick = {},
            onDismissDialog = {},
            onCreateTeam = {},
            onTeamClick = {},
            onEditTeam = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreateTeamDialogPreview() {
    PokeDexAppTheme {
        CreateTeamDialog(
            onDismiss = {},
            onConfirm = {}
        )
    }
}
