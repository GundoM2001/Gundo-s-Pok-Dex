package com.example.pokedexapp.presentation.feature.pokemon_list.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.pokedexapp.R
import com.example.pokedexapp.domain.model.PokemonResults
import com.example.pokedexapp.presentation.components.PokedexBackground
import com.example.pokedexapp.presentation.components.PokemonItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel = hiltViewModel(),
    onPokemonClick: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val pokemonList by viewModel.filteredPokemonList.collectAsState()
    val nextUrl = state.nextUrl
    val previousUrl = state.previousUrl
    val isLoading = state.isLoading
    val isEnriching = state.isEnriching
    val searchQuery = state.searchQuery
    val favouriteIds = state.favouriteIds

    PokemonListContent(
        pokemonList = pokemonList,
        favouriteIds = favouriteIds,
        nextUrl = nextUrl,
        previousUrl = previousUrl,
        isLoading = isLoading,
        isEnriching = isEnriching,
        searchQuery = searchQuery,
        onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
        onPageRequest = { viewModel.fetchPokemonData(it) },
        onPokemonClick = onPokemonClick,
        onToggleFavourite = { pokemon ->
            viewModel.toggleFavourite(pokemon)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListContent(
    pokemonList: List<PokemonResults>?,
    favouriteIds: Set<Int>,
    nextUrl: String?,
    previousUrl: String?,
    isLoading: Boolean,
    isEnriching: Boolean,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onPageRequest: (String) -> Unit,
    onPokemonClick: (String) -> Unit,
    onToggleFavourite: (PokemonResults) -> Unit
) {
    var isSearchExpanded by remember { mutableStateOf(false) }

    PokedexBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        if (isSearchExpanded) {
                            TextField(
                                value = searchQuery,
                                onValueChange = onSearchQueryChanged,
                                placeholder = {
                                    Text(
                                        stringResource(R.string.search_placeholder),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                ),
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.pokedex_title),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    navigationIcon = {
                        if (isSearchExpanded) {
                            IconButton(onClick = {
                                isSearchExpanded = false
                                onSearchQueryChanged("")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.close_search_desc)
                                )
                            }
                        }
                    },
                    actions = {
                        if (!isSearchExpanded) {
                            IconButton(onClick = { isSearchExpanded = true }) {
                                Icon(imageVector = Icons.Default.Search, contentDescription = stringResource(R.string.search_desc))
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            bottomBar = {
                if (searchQuery.isBlank()) {
                    PaginationPager(
                        nextUrl = nextUrl,
                        previousUrl = previousUrl,
                        onPageRequest = onPageRequest
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading || isEnriching) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(pokemonList ?: emptyList()) { pokemon ->
                            PokemonItem(
                                pokemon = pokemon,
                                isFavorite = favouriteIds.contains(pokemon.id),
                                onFavouriteClick = {
                                    onToggleFavourite(pokemon)
                                },
                                onClick = {
                                    onPokemonClick(pokemon.url)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaginationPager(
    nextUrl: String?,
    previousUrl: String?,
    onPageRequest: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { previousUrl?.let { onPageRequest(it) } },
            enabled = previousUrl != null,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(stringResource(R.string.pagination_previous), style = MaterialTheme.typography.labelSmall)
        }

        Button(
            onClick = { nextUrl?.let { onPageRequest(it) } },
            enabled = nextUrl != null,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(stringResource(R.string.pagination_next), style = MaterialTheme.typography.labelSmall)
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonListContentPreview() {
    com.example.pokedexapp.presentation.theme.PokeDexAppTheme {
        PokemonListContent(
            pokemonList = listOf(
                PokemonResults(
                    name = "Bulbasaur",
                    url = "https://pokeapi.co/api/v2/pokemon/1/"
                ),
                PokemonResults(
                    name = "Ivysaur",
                    url = "https://pokeapi.co/api/v2/pokemon/2/"
                ),
                PokemonResults(
                    name = "Venusaur",
                    url = "https://pokeapi.co/api/v2/pokemon/3/"
                )
            ),
            favouriteIds = setOf(1, 3),
            nextUrl = "next",
            previousUrl = null,
            isLoading = false,
            isEnriching = false,
            searchQuery = "",
            onSearchQueryChanged = {},
            onPageRequest = {},
            onPokemonClick = {},
            onToggleFavourite = {}
        )
    }
}
