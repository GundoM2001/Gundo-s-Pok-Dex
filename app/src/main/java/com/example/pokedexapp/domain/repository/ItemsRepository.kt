package com.example.pokedexapp.domain.repository

import com.example.pokedexapp.domain.model.ItemDetails
import com.example.pokedexapp.domain.model.ItemListResponse

interface ItemsRepository {
    suspend fun getAllItems(url: String? = null): ItemListResponse
    suspend fun getItemDetails(idOrName: String): ItemDetails
    suspend fun getItemCategory(category: String): com.example.pokedexapp.domain.model.ItemCategoryResponse
    suspend fun getItemAttribute(attribute: String): com.example.pokedexapp.domain.model.ItemAttributeResponse
}
