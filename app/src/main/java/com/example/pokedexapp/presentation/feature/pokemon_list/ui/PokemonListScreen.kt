package com.example.pokedexapp.presentation.feature.pokemon_list.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun PokemonListScreen(viewModel: PokemonListViewModel = hiltViewModel()) {
    val pokemonList by viewModel.pokemonList.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(pokemonList ?: emptyList()) { pokemon ->
                Text(text = pokemon.name)
            }
        }
    }
}
