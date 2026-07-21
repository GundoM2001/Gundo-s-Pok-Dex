package com.example.pokedexapp.di

import android.content.Context
import androidx.room.Room
import com.example.pokedexapp.data.AppDatabase
import com.example.pokedexapp.data.local.dao.FavouritePokemonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "favourite_pokemon.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideFavouriteDao(db: AppDatabase): FavouritePokemonDao = db.favouritePokemonDao()
}