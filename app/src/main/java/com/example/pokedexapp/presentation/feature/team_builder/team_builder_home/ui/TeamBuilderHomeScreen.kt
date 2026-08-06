package com.example.pokedexapp.presentation.feature.team_builder.team_builder_home.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.pokedexapp.R
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
    val teamToDelete by viewModel.teamToDelete.collectAsState()

    TeamBuilderHomeScreenContent(
        teamList = teamList,
        showDialog = showDialog,
        teamToDelete = teamToDelete,
        onAddClick = { viewModel.onShowDialog() },
        onDismissDialog = { viewModel.onDismissDialog() },
        onCreateTeam = { viewModel.createTeam(it) },
        onTeamClick = onTeamClick,
        onEditTeam = { /* Rename logic if needed */ },
        onDeleteTeam = { viewModel.onConfirmDelete(it) },
        onConfirmDelete = { viewModel.deleteTeam() },
        onDismissDelete = { viewModel.onDismissDelete() }
    )
}

@Composable
fun TeamBuilderHomeScreenContent(
    teamList: List<TeamWithPokemon>,
    showDialog: Boolean,
    teamToDelete: TeamEntity?,
    onAddClick: () -> Unit,
    onDismissDialog: () -> Unit,
    onCreateTeam: (String) -> Unit,
    onTeamClick: (Int) -> Unit,
    onEditTeam: (TeamEntity) -> Unit,
    onDeleteTeam: (TeamEntity) -> Unit,
    onConfirmDelete: () -> Unit,
    onDismissDelete: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick, shape = CircleShape, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_team_desc))
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
                        text = stringResource(R.string.no_teams),
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
                            onEditClick = { onEditTeam(teamWithPokemon.team) },
                            onDeleteClick = { onDeleteTeam(teamWithPokemon.team) }
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

    if (teamToDelete != null) {
        DeleteTeamConfirmationDialog(
            onDismiss = onDismissDelete,
            onConfirm = onConfirmDelete
        )
    }
}

@Composable
fun DeleteTeamConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.delete_team_confirm_title)) },
        text = { Text(stringResource(R.string.delete_team_confirm_msg)) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(stringResource(R.string.btn_delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.btn_cancel))
            }
        }
    )
}

@Composable
fun CreateTeamDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var teamName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.create_team_title)) },
        text = {
            Column {
                Text(stringResource(R.string.enter_team_name))
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = teamName,
                    onValueChange = { teamName = it },
                    placeholder = { Text(stringResource(R.string.team_name_placeholder)) },
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
                Text(stringResource(R.string.btn_create))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.btn_cancel))
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
            teamToDelete = null,
            onAddClick = {},
            onDismissDialog = {},
            onCreateTeam = {},
            onTeamClick = {},
            onEditTeam = {},
            onDeleteTeam = {},
            onConfirmDelete = {},
            onDismissDelete = {}
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
            teamToDelete = null,
            onAddClick = {},
            onDismissDialog = {},
            onCreateTeam = {},
            onTeamClick = {},
            onEditTeam = {},
            onDeleteTeam = {},
            onConfirmDelete = {},
            onDismissDelete = {}
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
