package com.example.pokedexapp.utils

import java.util.Locale

object PokemonVersionUtils {

    private val versionToGeneration = mapOf(
        "red" to "Gen I", "blue" to "Gen I", "yellow" to "Gen I",
        "gold" to "Gen II", "silver" to "Gen II", "crystal" to "Gen II",
        "ruby" to "Gen III", "sapphire" to "Gen III", "emerald" to "Gen III", "firered" to "Gen III", "leafgreen" to "Gen III",
        "diamond" to "Gen IV", "pearl" to "Gen IV", "platinum" to "Gen IV", "heartgold" to "Gen IV", "soulsilver" to "Gen IV",
        "black" to "Gen V", "white" to "Gen V", "black-2" to "Gen V", "white-2" to "Gen V",
        "x" to "Gen VI", "y" to "Gen VI", "omega-ruby" to "Gen VI", "alpha-sapphire" to "Gen VI",
        "sun" to "Gen VII", "moon" to "Gen VII", "ultra-sun" to "Gen VII", "ultra-moon" to "Gen VII", "lets-go-pikachu" to "Gen VII", "lets-go-eevee" to "Gen VII",
        "sword" to "Gen VIII", "shield" to "Gen VIII", "brilliant-diamond" to "Gen VIII", "shining-pearl" to "Gen VIII", "legends-arceus" to "Gen VIII",
        "scarlet" to "Gen IX", "violet" to "Gen IX"
    )

    fun getGeneration(version: String): String {
        return versionToGeneration[version.lowercase()] ?: "Other"
    }

    fun formatVersionName(version: String): String {
        return version.replace("-", " ")
            .split(" ")
            .joinToString(" ") { it.replaceFirstChar { char -> if (characterIsLowerCase(char)) char.titlecase(Locale.getDefault()) else char.toString() } }
    }

    private fun characterIsLowerCase(c: Char): Boolean = c in 'a'..'z'
}
