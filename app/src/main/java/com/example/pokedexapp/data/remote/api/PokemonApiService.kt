package com.example.pokedexapp.data.remote.api

import com.example.pokedexapp.domain.model.AbilityDetails
import com.example.pokedexapp.domain.model.MachineDetails
import com.example.pokedexapp.domain.model.MoveDetails
import com.example.pokedexapp.domain.model.PokemonDetails
import com.example.pokedexapp.domain.model.PokemonListResponse
import com.example.pokedexapp.domain.model.PokemonSpecies
import com.example.pokedexapp.domain.model.TypeDetails
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

    @GET
    suspend fun getTypeDetails(@Url url: String): Response<TypeDetails>

    @GET
    suspend fun getAbilityDetails(@Url url: String): Response<AbilityDetails>

    @GET
    suspend fun getMoveDetails(@Url url: String): Response<MoveDetails>

    @GET
    suspend fun getMachineDetails(@Url url: String): Response<MachineDetails>
}
