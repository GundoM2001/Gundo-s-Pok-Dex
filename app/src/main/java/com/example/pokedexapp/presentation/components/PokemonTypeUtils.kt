package com.example.pokedexapp.presentation.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

data class TypeGradient(
    val start: Color,
    val end: Color
)

object PokemonTypeUtils {
    private val typeColors = mapOf(
        "fire" to TypeGradient(Color(0xFFFF9C54), Color(0xFFF08030)),
        "water" to TypeGradient(Color(0xFF6890F0), Color(0xFF445E9C)),
        "grass" to TypeGradient(Color(0xFF7AC74C), Color(0xFF4E8234)),
        "electric" to TypeGradient(Color(0xFFF7D02C), Color(0xFFA1871F)),
        "ice" to TypeGradient(Color(0xFF98D8D8), Color(0xFF638D8D)),
        "fighting" to TypeGradient(Color(0xFFC22E28), Color(0xFF7D1E1A)),
        "poison" to TypeGradient(Color(0xFFA33EA1), Color(0xFF682868)),
        "ground" to TypeGradient(Color(0xFFE2BF65), Color(0xFF927D44)),
        "flying" to TypeGradient(Color(0xFFA98FF3), Color(0xFF6D5E9C)),
        "psychic" to TypeGradient(Color(0xFFF95587), Color(0xFFA13959)),
        "bug" to TypeGradient(Color(0xFFA6B91A), Color(0xFF6D7811)),
        "rock" to TypeGradient(Color(0xFFB6A136), Color(0xFF786923)),
        "ghost" to TypeGradient(Color(0xFF735797), Color(0xFF493863)),
        "dragon" to TypeGradient(Color(0xFF6F35FC), Color(0xFF4924A1)),
        "dark" to TypeGradient(Color(0xFF705746), Color(0xFF49392F)),
        "steel" to TypeGradient(Color(0xFFB7B7CE), Color(0xFF787887)),
        "fairy" to TypeGradient(Color(0xFFD685AD), Color(0xFF9B597A)),
        "normal" to TypeGradient(Color(0xFFA8A77A), Color(0xFF6D6D4E))
    )

    fun getGradientForType(type: String?): TypeGradient {
        return typeColors[type?.lowercase()] ?: TypeGradient(Color(0xFFA8A77A), Color(0xFF6D6D4E))
    }

    fun getContrastColor(backgroundColor: Color): Color {
        return if (backgroundColor.luminance() > 0.45) Color.Black else Color.White
    }
}
