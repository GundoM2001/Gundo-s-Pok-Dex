package com.example.pokedexapp.domain.model

enum class Nature(val displayName: String, val boostedStat: String? = null, val hinderedStat: String? = null) {
    ADAMANT("Adamant", "attack", "special-attack"),
    BASHFUL("Bashful"),
    BOLD("Bold", "defense", "attack"),
    BRAVE("Brave", "attack", "speed"),
    CALM("Calm", "special-defense", "attack"),
    CAREFUL("Careful", "special-defense", "special-attack"),
    DOCILE("Docile"),
    GENTLE("Gentle", "special-defense", "defense"),
    HARDY("Hardy"),
    HASTY("Hasty", "speed", "defense"),
    IMPISH("Impish", "defense", "special-attack"),
    JOLLY("Jolly", "speed", "special-attack"),
    LAX("Lax", "defense", "special-defense"),
    LONELY("Lonely", "attack", "defense"),
    MILD("Mild", "special-attack", "defense"),
    MODEST("Modest", "special-attack", "attack"),
    NAIVE("Naive", "speed", "special-defense"),
    NAUGHTY("Naughty", "attack", "special-defense"),
    QUIET("Quiet", "special-attack", "speed"),
    QUIRKY("Quirky"),
    RASH("Rash", "special-attack", "special-defense"),
    RELAXED("Relaxed", "defense", "speed"),
    SASSY("Sassy", "special-defense", "speed"),
    SERIOUS("Serious"),
    TIMID("Timid", "speed", "attack");

    fun getMultiplierForStat(statName: String): Double {
        return when {
            statName == boostedStat -> 1.1
            statName == hinderedStat -> 0.9
            else -> 1.0
        }
    }

    companion object {
        fun fromName(name: String): Nature {
            return entries.find { it.displayName.equals(name, ignoreCase = true) } ?: HARDY
        }
    }
}
