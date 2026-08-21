package com.example.pokedexapp.data.remote.api

import com.example.pokedexapp.domain.model.ItemAttributeResponse
import com.example.pokedexapp.domain.model.ItemCategoryResponse
import com.example.pokedexapp.domain.model.ItemDetails
import com.example.pokedexapp.domain.model.ItemListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ItemsApiService {

    @GET("item")
    suspend fun getAllItems(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20
    ): Response<ItemListResponse>

    @GET("item/{id_or_name}")
    suspend fun getItemDetails(
        @Path("id_or_name") idOrName: String
    ): Response<ItemDetails>

    @GET("item-category/{id_or_name}")
    suspend fun getItemCategory(
        @Path("id_or_name") idOrName: String
    ): Response<ItemCategoryResponse>

    @GET("item-attribute/{id_or_name}")
    suspend fun getItemAttribute(
        @Path("id_or_name") idOrName: String
    ): Response<ItemAttributeResponse>
}
