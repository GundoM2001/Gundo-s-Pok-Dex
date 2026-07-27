package com.example.pokedexapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = Screen.PokemonList.route,
        title = "Home",
        icon = Icons.Default.Home
    )

    object TeamBuilder : BottomNavItem(
        route = Screen.TeamBuilderHome.route,
        title = "Team Builder",
        icon = Icons.Default.Favorite
    )

    object Favorites : BottomNavItem(
        route = Screen.Favorites.route,
        title = "Favorites",
        icon = Icons.Default.Favorite
    )
}
