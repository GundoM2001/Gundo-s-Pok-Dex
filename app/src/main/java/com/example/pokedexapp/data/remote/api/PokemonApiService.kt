package com.example.pokedexapp.data.remote.api

import com.example.pokedexapp.domain.model.PokemonDetails
import com.example.pokedexapp.domain.model.PokemonListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface PokemonApiService {
    @GET("pokemon")
    suspend fun getAllPokemon(): Response<PokemonListResponse>

    @GET
    suspend fun getPokemonPage(@Url url: String): Response<PokemonListResponse>

    @GET
    suspend fun getPokemonDetails(@Url url: String): Response<PokemonDetails>
}