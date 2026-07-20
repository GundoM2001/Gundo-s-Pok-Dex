package com.example.pokedexapp.presentation.mock

import com.example.pokedexapp.domain.model.*

object MockData {
    val mockDetails = PokemonDetails(
        id = 25,
        name = "pikachu",
        baseExperience = 112,
        height = 4,
        isDefault = true,
        order = 35,
        weight = 60,
        abilities = listOf(
            Ability(NamedApiResource("static", "https://pokeapi.co/api/v2/ability/9/"), false, 1),
            Ability(NamedApiResource("lightning-rod", "https://pokeapi.co/api/v2/ability/31/"), true, 3)
        ),
        forms = emptyList(),
        gameIndices = emptyList(),
        heldItems = emptyList(),
        locationAreaEncounters = "",
        moves = listOf(
            Move(NamedApiResource("thunder-shock", ""), listOf(VersionGroupDetail(1, NamedApiResource("level-up", ""), NamedApiResource("red-blue", "")))),
            Move(NamedApiResource("quick-attack", ""), listOf(VersionGroupDetail(10, NamedApiResource("level-up", ""), NamedApiResource("red-blue", "")))),
            Move(NamedApiResource("thunderbolt", ""), listOf(VersionGroupDetail(0, NamedApiResource("machine", ""), NamedApiResource("red-blue", ""))))
        ),
        species = NamedApiResource("pikachu", "https://pokeapi.co/api/v2/pokemon-species/25/"),
        sprites = Sprites(
            null, null, null, null, null, null, null, null,
            OtherSprites(
                null, null,
                SpriteVariant(
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png",
                    null, null, null, null, null, null, null
                ),
                null
            ),
            null
        ),
        cries = Cries("", ""),
        stats = listOf(
            Stat(35, 0, NamedApiResource("hp", "")),
            Stat(55, 0, NamedApiResource("attack", "")),
            Stat(40, 0, NamedApiResource("defense", "")),
            Stat(50, 0, NamedApiResource("special-attack", "")),
            Stat(50, 0, NamedApiResource("special-defense", "")),
            Stat(90, 0, NamedApiResource("speed", ""))
        ),
        types = listOf(
            Type(1, NamedApiResource("electric", ""))
        ),
        pastAbilities = emptyList(),
        pastStats = emptyList(),
        pastTypes = emptyList()
    )

    val mockSpecies = PokemonSpecies(
        id = 25,
        name = "pikachu",
        flavorTextEntries = listOf(
            FlavorTextEntry(
                "When several of these POKéMON gather, their electricity could build and cause lightning storms.",
                NamedApiResource("en", ""),
                NamedApiResource("red", "")
            ),
            FlavorTextEntry(
                "It keeps its tail raised to monitor its surroundings. If you pull its tail, it will try to bite you.",
                NamedApiResource("en", ""),
                NamedApiResource("yellow", "")
            )
        ),
        genera = listOf(
            Genus("Mouse Pokémon", NamedApiResource("en", ""))
        ),
        varieties = emptyList()
    )

    val mockAbilityDetails = listOf(
        AbilityDetails(
            id = 9,
            name = "static",
            effectEntries = listOf(
                EffectEntry(
                    "The Pokémon is charged with static electricity, so contact with it may cause paralysis.",
                    "May cause paralysis if touched.",
                    NamedApiResource("en", "")
                )
            )
        ),
        AbilityDetails(
            id = 31,
            name = "lightning-rod",
            effectEntries = listOf(
                EffectEntry(
                    "The Pokémon draws in all Electric-type moves. Instead of being hit by Electric-type moves, it boosts its Sp. Atk.",
                    "Draws in Electric moves to up Sp. Atk.",
                    NamedApiResource("en", "")
                )
            )
        )
    )

    val mockTypeAdvantages = mapOf(
        "ground" to 2.0,
        "flying" to 0.5,
        "steel" to 0.5,
        "electric" to 0.5
    )
}
