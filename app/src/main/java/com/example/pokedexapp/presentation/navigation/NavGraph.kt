package com.example.pokedexapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pokedexapp.presentation.feature.pokemon_list.PokemonListScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.PokemonList.route
    ) {
        composable(route = Screen.PokemonList.route) {
            PokemonListScreen()
        }
        composable(route = Screen.PokemonDetail.route) {
            // TODO: Add Pokemon Detail Screen
        }
    }
}
