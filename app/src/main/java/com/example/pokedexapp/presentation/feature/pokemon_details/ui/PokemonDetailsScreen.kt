package com.example.pokedexapp.presentation.feature.pokemon_details.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun PokemonDetailsScreen(
    viewModel: PokemonDetailsViewModel = hiltViewModel()
) {
    val pokemonDetails by viewModel.pokemonDetails.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else if (error != null) {
            Text(text = "Error: $error", color = MaterialTheme.colorScheme.error)

        } else {
            pokemonDetails?.let { details ->
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Name: ${details.name.uppercase()}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "ID: #${details.id}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
