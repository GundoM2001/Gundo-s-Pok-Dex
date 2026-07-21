package com.example.pokedexapp.presentation.feature.pokemon_list.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit,
) {
    ModalDrawerSheet {
        Text(
            text = "Pokédex",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
        )
        HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
        NavigationDrawerItem(
            label = { Text("Home", style = MaterialTheme.typography.labelLarge) },
            selected = false,
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
            onClick = onHomeClick,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        NavigationDrawerItem(
            label = { Text("Favourites", style = MaterialTheme.typography.labelLarge) },
            selected = false,
            icon = { Icon(imageVector = Icons.Default.Favorite, contentDescription = null) },
            onClick = onFavoritesClick,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            label = { Text("Settings", style = MaterialTheme.typography.labelLarge) },
            selected = false,
            icon = { Icon(imageVector = Icons.Default.Settings, contentDescription = null) },
            onClick = onFavoritesClick,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppDrawerPreview() {
    MaterialTheme {
        AppDrawer(
            onHomeClick = {},
            onFavoritesClick = {}
        )
    }
}
