package com.example.pokedexapp.presentation.feature.pokemon_list.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit,
) {
    ModalDrawerSheet {
        Text(
            text = "Pokédex",
            modifier = Modifier.padding(16.dp),
        )
        NavigationDrawerItem(
            label = { Text("Home") },
            selected = false,
            onClick = onHomeClick
        )
        NavigationDrawerItem(
            label = { Text("Favourites") },
            selected = false,
            onClick = onFavoritesClick
        )
    }
}