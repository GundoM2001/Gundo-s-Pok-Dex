package com.example.pokedexapp.utils

object PokemonImageUtils {
    /**
     * Generates the official artwork URL for a given Pokémon ID.
     */
    fun getOfficialArtworkUrl(pokemonId: Int): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokemonId.png"
    }
}
