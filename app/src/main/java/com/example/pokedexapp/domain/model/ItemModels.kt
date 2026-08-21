package com.example.pokedexapp.domain.model

import com.google.gson.annotations.SerializedName


data class ItemListResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<NamedApiResource>
)

data class ItemDetails(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("cost")
    val cost: Int,
    @SerializedName("attributes")
    val attributes: List<NamedApiResource>,
    @SerializedName("category")
    val category: NamedApiResource,
    @SerializedName("effect_entries")
    val effectEntries: List<VerboseEffect>,
    @SerializedName("sprites")
    val sprites: ItemSprites
)

data class ItemSprites(
    @SerializedName("default")
    val default: String?
)

data class VerboseEffect(
    @SerializedName("effect")
    val effect: String,
    @SerializedName("short_effect")
    val shortEffect: String,
    @SerializedName("language")
    val language: NamedApiResource
)

data class ItemCategoryResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("items")
    val items: List<NamedApiResource>
)

data class ItemAttributeResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("items")
    val items: List<NamedApiResource>
)
