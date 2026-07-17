package com.example.pokedexapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pokedexapp.presentation.feature.pokemon_details.ui.PokemonDetailsScreen
import com.example.pokedexapp.presentation.feature.pokemon_list.ui.PokemonListScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.PokemonList.route
    ) {
        composable(route = Screen.PokemonList.route) {
            PokemonListScreen(
                onPokemonClick = { url ->
                    navController.navigate(Screen.PokemonDetail.passUrl(url))
                }
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
    }
}
