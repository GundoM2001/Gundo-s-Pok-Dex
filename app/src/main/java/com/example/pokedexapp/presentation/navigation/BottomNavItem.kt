package com.example.pokedexapp.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.pokedexapp.R

sealed class BottomNavItem(
    val route: String,
    @StringRes val titleRes: Int,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = Screen.PokemonList.route,
        titleRes = R.string.nav_home,
        icon = Icons.Default.Home
    )

    object TeamBuilder : BottomNavItem(
        route = Screen.TeamBuilderHome.route,
        titleRes = R.string.nav_team_builder,
        icon = Icons.Default.CatchingPokemon
    )

    object Favorites : BottomNavItem(
        route = Screen.Favorites.route,
        titleRes = R.string.nav_favorites,
        icon = Icons.Default.Favorite
    )

    object Settings : BottomNavItem(
        route = Screen.Settings.route,
        titleRes = R.string.nav_settings,
        icon = Icons.Default.Settings
    )
}
