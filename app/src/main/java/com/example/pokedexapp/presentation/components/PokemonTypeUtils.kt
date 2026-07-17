package com.example.pokedexapp.presentation.components

import androidx.compose.ui.graphics.Color

data class TypeGradient(
    val start: Color,
    val end: Color
)

object PokemonTypeUtils {
    private val typeColors = mapOf(
        "fire" to TypeGradient(Color(0xFFFF9A76), Color(0xFFF15A29)),
        "water" to TypeGradient(Color(0xFF8FD3FF), Color(0xFF3B82F6)),
        "grass" to TypeGradient(Color(0xFFA8E6A3), Color(0xFF4CAF50)),
        "electric" to TypeGradient(Color(0xFFFFE66D), Color(0xFFFFC107)),
        "ice" to TypeGradient(Color(0xFFC8F5FF), Color(0xFF4DD0E1)),
        "fighting" to TypeGradient(Color(0xFFE57373), Color(0xFFC62828)),
        "poison" to TypeGradient(Color(0xFFCE93D8), Color(0xFF8E24AA)),
        "ground" to TypeGradient(Color(0xFFD7B98E), Color(0xFF8D6E63)),
        "flying" to TypeGradient(Color(0xFFB3E5FC), Color(0xFF64B5F6)),
        "psychic" to TypeGradient(Color(0xFFFFB6C1), Color(0xFFEC407A)),
        "bug" to TypeGradient(Color(0xFFDCE775), Color(0xFF8BC34A)),
        "rock" to TypeGradient(Color(0xFFD7CCC8), Color(0xFF8D6E63)),
        "ghost" to TypeGradient(Color(0xFFB39DDB), Color(0xFF5E35B1)),
        "dragon" to TypeGradient(Color(0xFF9FA8DA), Color(0xFF3949AB)),
        "dark" to TypeGradient(Color(0xFFB0BEC5), Color(0xFF455A64)),
        "steel" to TypeGradient(Color(0xFFCFD8DC), Color(0xFF78909C)),
        "fairy" to TypeGradient(Color(0xFFFFD1E8), Color(0xFFF48FB1)),
        "normal" to TypeGradient(Color(0xFFECEFF1), Color(0xFFB0BEC5))
    )

    fun getGradientForType(type: String?): TypeGradient {
        return typeColors[type?.lowercase()] ?: TypeGradient(Color(0xFFECEFF1), Color(0xFFB0BEC5))
    }
}
