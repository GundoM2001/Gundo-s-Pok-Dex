package com.example.pokedexapp.presentation.navigation

sealed class Screen(val route: String) {
    object PokemonList : Screen("pokemon_list_screen")
    object PokemonDetail : Screen("pokemon_detail_screen/{pokemonUrl}") {
        fun passUrl(url: String): String {
            return "pokemon_detail_screen/${java.net.URLEncoder.encode(url, "UTF-8")}"
        }
    }
    object TeamBuilderHome : Screen("team_builder_home_screen")
    object TeamDetail : Screen("team_detail_screen/{teamId}") {
        fun passId(teamId: Int): String = "team_detail_screen/$teamId"
    }
    object PokemonSearch : Screen("pokemon_search_screen/{teamId}/{slot}") {
        fun passArgs(teamId: Int, slot: Int): String = "pokemon_search_screen/$teamId/$slot"
    }
    object PokemonCustomization : Screen("pokemon_customization_screen/{teamId}/{slot}/{pokemonId}/{memberId}") {
        fun passArgs(teamId: Int, slot: Int, pokemonId: Int, memberId: Int): String = 
            "pokemon_customization_screen/$teamId/$slot/$pokemonId/$memberId"
    }
    object Favorites : Screen("favorites_screen")
    object Settings : Screen("settings_screen")
}
