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
import com.example.pokedexapp.presentation.feature.team_builder.pokemon_customization.ui.PokemonCustomizationScreen
import com.example.pokedexapp.presentation.feature.team_builder.pokemon_search.ui.PokemonSearchScreen
import com.example.pokedexapp.presentation.feature.team_builder.team_builder_home.ui.TeamBuilderHomeScreen
import com.example.pokedexapp.presentation.feature.team_builder.team_detail.ui.TeamDetailScreen

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
        composable(route = Screen.TeamBuilderHome.route) {
            TeamBuilderHomeScreen(
                onTeamClick = { teamId ->
                    navController.navigate(Screen.TeamDetail.passId(teamId))
                }
            )
        }

        composable(
            route = Screen.TeamDetail.route,
            arguments = listOf(navArgument("teamId") { type = NavType.IntType })
        ) {
            TeamDetailScreen(
                onBackClick = { navController.popBackStack() },
                onAddPokemonClick = { teamId, slot ->
                    navController.navigate(Screen.PokemonSearch.passArgs(teamId, slot))
                },
                onMemberClick = { memberId, pokemonId ->
                    // For editing existing, we need teamId and slot too. 
                    // Let's get them from the current entry or pass them along.
                    // Actually we can pass 0 for teamId/slot if memberId > 0 since we'll use memberId to fetch.
                    navController.navigate(Screen.PokemonCustomization.passArgs(0, 0, pokemonId, memberId))
                }
            )
        }

        composable(
            route = Screen.PokemonSearch.route,
            arguments = listOf(
                navArgument("teamId") { type = NavType.IntType },
                navArgument("slot") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val teamId = backStackEntry.arguments?.getInt("teamId") ?: 0
            val slot = backStackEntry.arguments?.getInt("slot") ?: 0
            PokemonSearchScreen(
                onBackClick = { navController.popBackStack() },
                onPokemonSelected = { tId, s, pId ->
                    navController.navigate(Screen.PokemonCustomization.passArgs(tId, s, pId, 0))
                }
            )
        }

        composable(
            route = Screen.PokemonCustomization.route,
            arguments = listOf(
                navArgument("teamId") { type = NavType.IntType },
                navArgument("slot") { type = NavType.IntType },
                navArgument("pokemonId") { type = NavType.IntType },
                navArgument("memberId") { type = NavType.IntType }
            )
        ) {
            PokemonCustomizationScreen(
                onBackClick = { navController.popBackStack() },
                onSaveSuccess = {
                    navController.popBackStack(Screen.TeamDetail.route, inclusive = false)
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
