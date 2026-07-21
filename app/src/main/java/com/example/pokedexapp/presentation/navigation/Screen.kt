package com.example.pokedexapp.presentation.navigation

sealed class Screen(val route: String) {
    object PokemonList : Screen("pokemon_list_screen")
    object PokemonDetail : Screen("pokemon_detail_screen/{pokemonUrl}") {
        fun passUrl(url: String): String {
            return "pokemon_detail_screen/${java.net.URLEncoder.encode(url, "UTF-8")}"
        }
    }
    object Favorites : Screen("favorites_screen")
}
