package com.example.pokedexapp.presentation.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pokedexapp.presentation.feature.favourite_pokemon.ui.FavoritePokemonScreen
import com.example.pokedexapp.presentation.feature.pokemon_details.ui.PokemonDetailsScreen
import com.example.pokedexapp.presentation.feature.pokemon_list.ui.PokemonListScreen
import com.example.pokedexapp.presentation.feature.team_builder.team_builder_home.ui.TeamBuilderHomeScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.PokemonList.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { 1000 },
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(400))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -1000 },
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(400))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -1000 },
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(400))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { 1000 },
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(400))
        }
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
            PokemonDetailsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(route = Screen.TeamBuilderHome.route){
            TeamBuilderHomeScreen(
                onTeamClick = { url ->

                }
            )
        }

        composable(route = Screen.Favorites.route) {
            FavoritePokemonScreen(
                onPokemonClick = { url ->
                    navController.navigate(Screen.PokemonDetail.passUrl(url))
                }
            )
        }
    }
}
