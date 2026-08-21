package com.example.pokedexapp.data.repository

import com.example.pokedexapp.data.remote.api.ItemsApiService
import com.example.pokedexapp.domain.model.ItemAttributeResponse
import com.example.pokedexapp.domain.model.ItemCategoryResponse
import com.example.pokedexapp.domain.model.ItemDetails
import com.example.pokedexapp.domain.model.ItemListResponse
import com.example.pokedexapp.domain.repository.ItemsRepository
import retrofit2.HttpException
import javax.inject.Inject

class ItemsRepositoryImpl @Inject constructor(
    private val service: ItemsApiService
) : ItemsRepository {
    override suspend fun getAllItems(url: String?): ItemListResponse {
        val response = if (url != null) {
            service.getItemPage(url)
        } else {
            service.getAllItems()
        }

        if (response.isSuccessful) {
            val listResponse = response.body()
                ?: throw com.google.gson.JsonSyntaxException("Response body is null")
            return listResponse
        } else {
            throw HttpException(response)
        }
    }

    override suspend fun getItemDetails(idOrName: String): ItemDetails {
        val response = service.getItemDetails(idOrName)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Response body is null")
        } else {
            throw HttpException(response)
        }
    }

    override suspend fun getItemCategory(category: String): ItemCategoryResponse {
        val response = service.getItemCategory(category)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Response body is null")
        } else {
            throw HttpException(response)
        }
    }

    override suspend fun getItemAttribute(attribute: String): ItemAttributeResponse {
        val response = service.getItemAttribute(attribute)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Response body is null")
        } else {
            throw HttpException(response)
        }
    }
}
