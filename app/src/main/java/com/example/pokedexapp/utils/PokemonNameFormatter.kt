package com.example.pokedexapp.utils

import java.util.Locale

object PokemonNameFormatter {

    fun format(name: String): String {
        return when {
            name.contains("-mega") -> formatMega(name)
            name.contains("-gmax") -> formatGmax(name)
            name.contains("-alola") -> "Alolan ${formatBase(name, "-alola")}"
            name.contains("-galar") -> "Galarian ${formatBase(name, "-galar")}"
            name.contains("-hisui") -> "Hisuian ${formatBase(name, "-hisui")}"
            name.contains("-paldea") -> "Paldean ${formatBase(name, "-paldea")}"
            else -> name.toTitleCase()
        }
    }

    private fun formatMega(name: String): String {
        // e.g., charizard-mega-x -> Mega Charizard X
        val parts = name.split("-")
        val megaIndex = parts.indexOf("mega")
        val baseName = parts.subList(0, megaIndex).joinToString(" ") { it.toTitleCase() }
        val suffix = if (megaIndex < parts.lastIndex) {
            " " + parts.subList(megaIndex + 1, parts.size).joinToString(" ") { it.toTitleCase() }
        } else ""
        return "Mega $baseName$suffix"
    }

    private fun formatGmax(name: String): String {
        // e.g., pikachu-gmax -> Pikachu (Gigantamax)
        val baseName = formatBase(name, "-gmax")
        return "$baseName (Gigantamax)"
    }

    private fun formatBase(name: String, suffix: String): String {
        return name.replace(suffix, "").toTitleCase()
    }

    private fun String.toTitleCase(): String {
        return replace("-", " ")
            .split(" ")
            .joinToString(" ") { word ->
                word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            }
    }
}
