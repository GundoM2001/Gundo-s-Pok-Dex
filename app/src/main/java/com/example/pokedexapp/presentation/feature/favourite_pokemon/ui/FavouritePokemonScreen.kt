package com.example.pokedexapp.presentation.feature.favourite_pokemon.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.pokedexapp.R
import com.example.pokedexapp.domain.model.PokemonResults
import com.example.pokedexapp.presentation.components.PokedexBackground
import com.example.pokedexapp.presentation.components.PokemonItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritePokemonScreen(
    viewModel: FavouritePokemonViewModel = hiltViewModel(),
    onPokemonClick: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val favouritePokemon = state.favouritePokemon
    val isLoading = state.isLoading

    FavoritePokemonContent(
        favouritePokemon = favouritePokemon,
        isLoading = isLoading,
        onPokemonClick = onPokemonClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritePokemonContent(
    favouritePokemon: List<PokemonResults>,
    isLoading: Boolean,
    onPokemonClick: (String) -> Unit
) {
    PokedexBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.nav_favorites),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                } else if (favouritePokemon.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_favorites),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(favouritePokemon) { pokemon ->
                            PokemonItem(
                                pokemon = pokemon,
                                isFavorite = true,
                                onClick = { onPokemonClick(pokemon.url) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritePokemonContentPreview() {
    FavoritePokemonContent(
        favouritePokemon = listOf(
            PokemonResults(name = "Bulbasaur", url = ""),
            PokemonResults(name = "Ivysaur", url = ""),
            PokemonResults(name = "Venusaur", url = "")
        ),
        isLoading = false,
        onPokemonClick = {}
    )
}
