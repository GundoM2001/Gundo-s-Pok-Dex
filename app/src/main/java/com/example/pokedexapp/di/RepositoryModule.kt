package com.example.pokedexapp.di

import com.example.pokedexapp.data.local.dao.FavouritePokemonDao
import com.example.pokedexapp.data.local.dao.TeamDao
import com.example.pokedexapp.data.remote.api.PokemonApiService
import com.example.pokedexapp.data.repository.FavouriteRepositoryImpl
import com.example.pokedexapp.data.repository.PokemonRepositoryImpl
import com.example.pokedexapp.data.repository.TeamRepositoryImpl
import com.example.pokedexapp.domain.repository.FavouriteRepository
import com.example.pokedexapp.domain.repository.PokemonRepository
import com.example.pokedexapp.domain.repository.TeamRepository
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

    @Provides
    @Singleton
    fun provideFavouriteRepository(dao: FavouritePokemonDao): FavouriteRepository =
        FavouriteRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideTeamRepository(dao: TeamDao): TeamRepository =
        TeamRepositoryImpl(dao)

}
