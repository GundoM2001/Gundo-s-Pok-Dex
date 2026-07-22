package com.example.pokedexapp.domain.model

import com.google.gson.annotations.SerializedName

// --- Response Models ---

/**
 * Main response for the paginated list of Pokémon.
 */
data class PokemonListResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<PokemonResults>
)

/**
 * Basic representation of a Pokémon in a list or search result.
 */
data class PokemonResults(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String,
    val imageUrl: String? = null,
    val types: List<String>? = null
) {
    val id: Int
        get() = url
            .trimEnd('/')
            .substringAfterLast('/')
            .toIntOrNull() ?: 0
}

// --- Core Detail Model ---

/**
 * Comprehensive details of a specific Pokémon.
 */
data class PokemonDetails(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("base_experience")
    val baseExperience: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("is_default")
    val isDefault: Boolean,
    @SerializedName("order")
    val order: Int,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("abilities")
    val abilities: List<Ability>,
    @SerializedName("forms")
    val forms: List<Form>,
    @SerializedName("game_indices")
    val gameIndices: List<GameIndex>,
    @SerializedName("held_items")
    val heldItems: List<HeldItem>,
    @SerializedName("location_area_encounters")
    val locationAreaEncounters: String,
    @SerializedName("moves")
    val moves: List<Move>,
    @SerializedName("species")
    val species: NamedApiResource,
    @SerializedName("sprites")
    val sprites: Sprites,
    @SerializedName("cries")
    val cries: Cries,
    @SerializedName("stats")
    val stats: List<Stat>,
    @SerializedName("types")
    val types: List<Type>,
    @SerializedName("past_abilities")
    val pastAbilities: List<PastAbility>,
    @SerializedName("past_stats")
    val pastStats: List<PastStat>,
    @SerializedName("past_types")
    val pastTypes: List<PastType>
)

// --- Nested Data Models ---

data class Ability(
    @SerializedName("ability")
    val ability: NamedApiResource,
    @SerializedName("is_hidden")
    val isHidden: Boolean,
    @SerializedName("slot")
    val slot: Int
)

data class Form(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class GameIndex(
    @SerializedName("game_index")
    val gameIndex: Int,
    @SerializedName("version")
    val version: NamedApiResource
)

data class HeldItem(
    @SerializedName("item")
    val item: NamedApiResource,
    @SerializedName("version_details")
    val versionDetails: List<HeldItemVersionDetail>
)

data class HeldItemVersionDetail(
    @SerializedName("rarity")
    val rarity: Int,
    @SerializedName("version")
    val version: NamedApiResource
)

data class Move(
    @SerializedName("move")
    val move: NamedApiResource,
    @SerializedName("version_group_details")
    val versionGroupDetails: List<VersionGroupDetail>
)

data class VersionGroupDetail(
    @SerializedName("level_learned_at")
    val levelLearnedAt: Int,
    @SerializedName("move_learn_method")
    val moveLearnMethod: NamedApiResource,
    @SerializedName("version_group")
    val versionGroup: NamedApiResource
)

data class Cries(
    @SerializedName("latest")
    val latest: String,
    @SerializedName("legacy")
    val legacy: String
)

data class Stat(
    @SerializedName("base_stat")
    val baseStat: Int,
    @SerializedName("effort")
    val effort: Int,
    @SerializedName("stat")
    val stat: NamedApiResource
)

data class Type(
    @SerializedName("slot")
    val slot: Int,
    @SerializedName("type")
    val type: NamedApiResource
)

// --- Historical Data Models ---

data class PastAbility(
    @SerializedName("abilities")
    val abilities: List<Ability>,
    @SerializedName("generation")
    val generation: NamedApiResource
)

data class PastStat(
    @SerializedName("generation")
    val generation: NamedApiResource,
    @SerializedName("stats")
    val stats: List<Stat>
)

data class PastType(
    @SerializedName("generation")
    val generation: NamedApiResource,
    @SerializedName("types")
    val types: List<Type>
)

// --- Sprite Models ---

/**
 * Root sprite container including variants for different categories and game versions.
 */
data class Sprites(
    @SerializedName("back_default")
    val backDefault: String?,
    @SerializedName("back_female")
    val backFemale: String?,
    @SerializedName("back_shiny")
    val backShiny: String?,
    @SerializedName("back_shiny_female")
    val backShiny_female: String?,
    @SerializedName("front_default")
    val frontDefault: String?,
    @SerializedName("front_female")
    val frontFemale: String?,
    @SerializedName("front_shiny")
    val frontShiny: String?,
    @SerializedName("front_shiny_female")
    val frontShiny_female: String?,
    @SerializedName("other")
    val other: OtherSprites?,
    @SerializedName("versions")
    val versions: Versions?
)

/**
 * High-resolution or themed sprite variants.
 */
data class OtherSprites(
    @SerializedName("dream_world")
    val dreamWorld: SpriteVariant?,
    @SerializedName("home")
    val home: SpriteVariant?,
    @SerializedName("official-artwork")
    val officialArtwork: SpriteVariant?,
    @SerializedName("showdown")
    val showdown: SpriteVariant?
)

/**
 * Standard set of front/back and shiny/default sprites.
 */
data class SpriteVariant(
    @SerializedName("front_default")
    val frontDefault: String?,
    @SerializedName("front_female")
    val frontFemale: String?,
    @SerializedName("front_shiny")
    val frontShiny: String?,
    @SerializedName("front_shiny_female")
    val frontShiny_female: String?,
    @SerializedName("back_default")
    val backDefault: String?,
    @SerializedName("back_female")
    val backFemale: String?,
    @SerializedName("back_shiny")
    val backShiny: String?,
    @SerializedName("back_shiny_female")
    val backShiny_female: String?,
    @SerializedName("back_gray")
    val backGray: String? = null,
    @SerializedName("back_transparent")
    val backTransparent: String? = null,
    @SerializedName("front_gray")
    val frontGray: String? = null,
    @SerializedName("front_transparent")
    val frontTransparent: String? = null,
    @SerializedName("back_shiny_transparent")
    val backShinyTransparent: String? = null,
    @SerializedName("front_shiny_transparent")
    val frontShinyTransparent: String? = null,
    @SerializedName("animated")
    val animated: SpriteVariant? = null
)

/**
 * Sprites categorized by Game Generation.
 */
data class Versions(
    @SerializedName("generation-i")
    val generationI: GenerationI?,
    @SerializedName("generation-ii")
    val generationII: GenerationII?,
    @SerializedName("generation-iii")
    val generationIII: GenerationIII?,
    @SerializedName("generation-iv")
    val generationIV: GenerationIV?,
    @SerializedName("generation-v")
    val generationV: GenerationV?,
    @SerializedName("generation-vi")
    val generationVI: Map<String, SpriteVariant>?,
    @SerializedName("generation-vii")
    val generationVII: GenerationVII?,
    @SerializedName("generation-viii")
    val generationVIII: GenerationVIII?,
    @SerializedName("generation-ix")
    val generationIX: GenerationIX?
)

data class GenerationI(
    @SerializedName("red-blue")
    val redBlue: SpriteVariant?,
    @SerializedName("yellow")
    val yellow: SpriteVariant?
)

data class GenerationII(
    @SerializedName("crystal")
    val crystal: SpriteVariant?,
    @SerializedName("gold")
    val gold: SpriteVariant?,
    @SerializedName("silver")
    val silver: SpriteVariant?
)

data class GenerationIII(
    @SerializedName("emerald")
    val emerald: SpriteVariant?,
    @SerializedName("firered-leafgreen")
    val fireredLeafgreen: SpriteVariant?,
    @SerializedName("ruby-sapphire")
    val rubySapphire: SpriteVariant?
)

data class GenerationIV(
    @SerializedName("diamond-pearl")
    val diamondPearl: SpriteVariant?,
    @SerializedName("heartgold-soulsilver")
    val heartgoldSoulsilver: SpriteVariant?,
    @SerializedName("platinum")
    val platinum: SpriteVariant?
)

data class GenerationV(
    @SerializedName("black-white")
    val blackWhite: SpriteVariant?
)

data class GenerationVII(
    @SerializedName("icons")
    val icons: SpriteVariant?,
    @SerializedName("ultra-sun-ultra-moon")
    val ultraSunUltraMoon: SpriteVariant?
)

data class GenerationVIII(
    @SerializedName("brilliant-diamond-shining-pearl")
    val brilliantDiamondShiningPearl: SpriteVariant?,
    @SerializedName("icons")
    val icons: SpriteVariant?
)

data class GenerationIX(
    @SerializedName("scarlet-violet")
    val scarletViolet: SpriteVariant?
)

// --- Utility Models ---

/**
 * Common pattern used in PokéAPI for resources that consist of a name and a URL.
 */
data class NamedApiResource(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

//Pokemon Species and Variants

data class PokemonSpecies(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntry>,
    @SerializedName("genera")
    val genera: List<Genus>,
    @SerializedName("varieties")
    val varieties: List<PokemonVariety>
)

data class FlavorTextEntry(
    @SerializedName("flavor_text")
    val flavorText: String,
    @SerializedName("language")
    val language: NamedApiResource,
    @SerializedName("version")
    val version: NamedApiResource
)

data class Genus(
    @SerializedName("genus")
    val genus: String,
    @SerializedName("language")
    val language: NamedApiResource
)

data class PokemonVariety(
    @SerializedName("is_default")
    val isDefault: Boolean,
    @SerializedName("pokemon")
    val pokemon: NamedApiResource
)

// --- Type Details ---

data class TypeDetails(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("damage_relations")
    val damageRelations: DamageRelations
)

data class DamageRelations(
    @SerializedName("no_damage_to")
    val noDamageTo: List<NamedApiResource>,
    @SerializedName("half_damage_to")
    val halfDamageTo: List<NamedApiResource>,
    @SerializedName("double_damage_to")
    val doubleDamageTo: List<NamedApiResource>,
    @SerializedName("no_damage_from")
    val noDamageFrom: List<NamedApiResource>,
    @SerializedName("half_damage_from")
    val halfDamageFrom: List<NamedApiResource>,
    @SerializedName("double_damage_from")
    val doubleDamageFrom: List<NamedApiResource>
)

// --- Ability Details ---

data class AbilityDetails(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("effect_entries")
    val effectEntries: List<EffectEntry>
)

// --- Move Details ---

data class MoveDetails(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("accuracy")
    val accuracy: Int?,
    @SerializedName("power")
    val power: Int?,
    @SerializedName("pp")
    val pp: Int?,
    @SerializedName("priority")
    val priority: Int,
    @SerializedName("type")
    val type: NamedApiResource,
    @SerializedName("damage_class")
    val damageClass: NamedApiResource?,
    @SerializedName("effect_entries")
    val effectEntries: List<EffectEntry>,
    @SerializedName("flavor_text_entries")
    val flavorTextEntries: List<MoveFlavorText>,
    @SerializedName("machines")
    val machines: List<MachineVersionDetail>
)

data class MachineVersionDetail(
    @SerializedName("machine")
    val machine: NamedApiResource,
    @SerializedName("version_group")
    val versionGroup: NamedApiResource
)

// --- Machine Details ---

data class MachineDetails(
    @SerializedName("id")
    val id: Int,
    @SerializedName("item")
    val item: NamedApiResource,
    @SerializedName("move")
    val move: NamedApiResource,
    @SerializedName("version_group")
    val versionGroup: NamedApiResource
)

data class MoveFlavorText(
    @SerializedName("flavor_text")
    val flavorText: String,
    @SerializedName("language")
    val language: NamedApiResource,
    @SerializedName("version_group")
    val versionGroup: NamedApiResource
)

data class EffectEntry(
    @SerializedName("effect")
    val effect: String,
    @SerializedName("short_effect")
    val shortEffect: String,
    @SerializedName("language")
    val language: NamedApiResource
)
