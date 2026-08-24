package com.example.pokedexapp.utils

object MegaEvolutionUtils {

    fun isMega(pokemonName: String?): Boolean {
        if (pokemonName == null) return false
        return pokemonName.contains("-mega") || 
               pokemonName.contains("-primal") || 
               pokemonName.contains("-ultra") ||
               pokemonName.contains("-crowned") ||
               pokemonName.contains("-wellspring") ||
               pokemonName.contains("-hearthflame") ||
               pokemonName.contains("-cornerstone")
    }

    fun getRequiredMove(pokemonName: String?): String? {
        return when (pokemonName) {
            "rayquaza-mega" -> "dragon-ascent"
            "necrozma-ultra" -> "photon-geyser"
            else -> null
        }
    }

    fun getRequiredItem(pokemonName: String?): String? {
        if (pokemonName == null) return null
        
        return when {
            pokemonName == "charizard-mega-x" -> "charizardite-x"
            pokemonName == "charizard-mega-y" -> "charizardite-y"
            pokemonName == "mewtwo-mega-x" -> "mewtwonite-x"
            pokemonName == "mewtwo-mega-y" -> "mewtwonite-y"
            pokemonName == "kyogre-primal" -> "blue-orb"
            pokemonName == "groudon-primal" -> "red-orb"
            pokemonName == "necrozma-ultra" -> "ultranecrozium-z--held"
            pokemonName == "zacian-crowned" -> "rusted-sword"
            pokemonName == "zamazenta-crowned" -> "rusted-shield"
            pokemonName.startsWith("silvally-") && pokemonName != "silvally" -> {
                val type = pokemonName.removePrefix("silvally-")
                "$type-memory"
            }
            pokemonName.startsWith("arceus-") && pokemonName != "arceus" -> {
                val type = pokemonName.removePrefix("arceus-")
                when(type) {
                    "bug" -> "insect-plate"
                    "dark" -> "dread-plate"
                    "dragon" -> "draco-plate"
                    "electric" -> "zap-plate"
                    "fairy" -> "pixie-plate"
                    "fighting" -> "fist-plate"
                    "fire" -> "flame-plate"
                    "flying" -> "sky-plate"
                    "ghost" -> "spooky-plate"
                    "grass" -> "meadow-plate"
                    "ground" -> "earth-plate"
                    "ice" -> "icicle-plate"
                    "poison" -> "toxic-plate"
                    "psychic" -> "mind-plate"
                    "rock" -> "stone-plate"
                    "steel" -> "iron-plate"
                    "water" -> "splash-plate"
                    else -> null
                }
            }
            pokemonName.startsWith("ogerpon-") -> {
                when {
                    pokemonName.contains("-wellspring") -> "wellspring-mask"
                    pokemonName.contains("-hearthflame") -> "hearthflame-mask"
                    pokemonName.contains("-cornerstone") -> "cornerstone-mask"
                    else -> null
                }
            }
            // Remember, Mega Raquayza needs to know Dragon Ascent, not hold a mega stone
            pokemonName == "rayquaza-mega" -> null 
            pokemonName.contains("-mega") -> {
                val base = pokemonName.removeSuffix("-mega")
                when (base) {
                    "venusaur" -> "venusaurite"
                    "blastoise" -> "blastoisinite"
                    "alakazam" -> "alakazite"
                    "gengar" -> "gengarite"
                    "kangaskhan" -> "kangaskhanite"
                    "pinsir" -> "pinsirite"
                    "gyarados" -> "gyaradosite"
                    "aerodactyl" -> "aerodactylite"
                    "ampharos" -> "ampharosite"
                    "scizor" -> "scizorite"
                    "heracross" -> "heracronite"
                    "houndoom" -> "houndoominite"
                    "tyranitar" -> "tyranitarite"
                    "blaziken" -> "blazikenite"
                    "gardevoir" -> "gardevoirite"
                    "mawile" -> "mawilite"
                    "aggron" -> "aggronite"
                    "medicham" -> "medichamite"
                    "manectric" -> "manectite"
                    "banette" -> "banettite"
                    "absol" -> "absolite"
                    "garchomp" -> "garchompite"
                    "lucario" -> "lucarionite"
                    "abomasnow" -> "abomasnowite"
                    "beedrill" -> "beedrillite"
                    "pidgeot" -> "pidgeotite"
                    "slowbro" -> "slowbronite"
                    "steelix" -> "steelixite"
                    "sceptile" -> "sceptilite"
                    "swampert" -> "swampertite"
                    "sableye" -> "sablenite"
                    "sharpedo" -> "sharpedonite"
                    "camerupt" -> "cameruptite"
                    "altaria" -> "altarianite"
                    "glalie" -> "glalitite"
                    "salamence" -> "salamencite"
                    "metagross" -> "metagrossite"
                    "latias" -> "latiasite"
                    "latios" -> "latiosite"
                    "lopunny" -> "lopunnite"
                    "gallade" -> "galladite"
                    "audino" -> "audinite"
                    "diancie" -> "diancite"
                    else -> "${base}ite" // General pattern
                }
            }
            else -> null
        }
    }
}
