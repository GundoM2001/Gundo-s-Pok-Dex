package com.example.pokedexapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pokedexapp.presentation.feature.favourite_pokemon.ui.FavoritePokemonScreen
import com.example.pokedexapp.presentation.feature.pokemon_details.ui.PokemonDetailsScreen
import com.example.pokedexapp.presentation.feature.pokemon_list.ui.PokemonListScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.PokemonList.route
    ) {
        composable(route = Screen.PokemonList.route) {
            PokemonListScreen(
                onPokemonClick = { url ->
                    navController.navigate(Screen.PokemonDetail.passUrl(url))
                },
                onFavoritesClick = { navController.navigate(Screen.Favorites.route) },
            )
        }
        composable(
            route = Screen.PokemonDetail.route,
            arguments = listOf(
                navArgument("pokemonUrl") {
                    type = NavType.StringType
                }
            )
        ) {
            PokemonDetailsScreen()
        }
        composable(route = Screen.Favorites.route) {
            FavoritePokemonScreen(
                onHomeClick = { navController.navigate(Screen.PokemonList.route) },
                onPokemonClick = { url ->
                    navController.navigate(Screen.PokemonDetail.passUrl(url))
                },
            )
        }
    }
}
