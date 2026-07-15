package com.example.pokedexapp.domain.model

import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("name")
    val results: List<PokemonResults>
)

data class PokemonResults(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)