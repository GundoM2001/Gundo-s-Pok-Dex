package com.example.pokedexapp.presentation.feature.favourite_pokemon.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.pokedexapp.domain.model.PokemonResults
import com.example.pokedexapp.presentation.components.PokedexBackground
import com.example.pokedexapp.presentation.components.PokemonItem
import com.example.pokedexapp.presentation.feature.pokemon_list.components.AppDrawer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritePokemonScreen(
    viewModel: FavouritePokemonViewModel = hiltViewModel(),
    onPokemonClick: (String) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onFavoritesClick: () -> Unit = {}
) {
    val favouritePokemon by viewModel.favouritePokemon.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    FavoritePokemonContent(
        favouritePokemon = favouritePokemon,
        isLoading = isLoading,
        onPokemonClick = onPokemonClick,
        onMenuClick = {
            scope.launch {
                drawerState.open()
            }
        },
        onHomeClick = onHomeClick,
        onFavoritesClick = onFavoritesClick,
        drawerState = drawerState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritePokemonContent(
    favouritePokemon: List<PokemonResults>,
    isLoading: Boolean,
    onPokemonClick: (String) -> Unit,
    onMenuClick: () -> Unit,
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                onHomeClick = onHomeClick,
                onFavoritesClick = onFavoritesClick
            )
        }
    ) {
        PokedexBackground {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = "Favorites",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onMenuClick) {
                                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                            }
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
                            text = "No favorites yet",
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
                                    onClick = { onPokemonClick(pokemon.url) }
                                )
                            }
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
        onPokemonClick = {},
        onMenuClick = {},
        onHomeClick = {},
        onFavoritesClick = {}
    )
}
