package com.example.pokedexapp.di

import com.example.pokedexapp.data.remote.api.PokemonApiService
import com.example.pokedexapp.data.repository.PokemonRepositoryImpl
import com.example.pokedexapp.domain.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePokemonRepository(api: PokemonApiService): PokemonRepository =
        PokemonRepositoryImpl(api)

}