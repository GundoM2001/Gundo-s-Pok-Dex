package com.example.pokedexapp.utils

import androidx.annotation.StringRes
import com.example.pokedexapp.R

object StatFormatter {
    @StringRes
    fun getStatAbbreviationRes(statName: String?): Int {
        return when (statName?.lowercase()) {
            "hp" -> R.string.stat_hp
            "attack" -> R.string.stat_atk
            "defense" -> R.string.stat_def
            "special-attack" -> R.string.stat_spa
            "special-defense" -> R.string.stat_spd
            "speed" -> R.string.stat_spe
            else -> R.string.none_label
        }
    }
}
