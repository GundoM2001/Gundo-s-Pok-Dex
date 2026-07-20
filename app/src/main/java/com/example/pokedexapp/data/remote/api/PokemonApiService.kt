package com.example.pokedexapp.data.remote.api

import com.example.pokedexapp.domain.model.PokemonDetails
import com.example.pokedexapp.domain.model.PokemonListResponse
import com.example.pokedexapp.domain.model.PokemonSpecies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface PokemonApiService {
    @GET("pokemon")
    suspend fun getAllPokemon(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20
    ): Response<PokemonListResponse>

    @GET
    suspend fun getPokemonPage(@Url url: String): Response<PokemonListResponse>

    @GET
    suspend fun getPokemonDetails(@Url url: String): Response<PokemonDetails>

    @GET
    suspend fun getPokemonSpecies(@Url url: String): Response<PokemonSpecies>
}