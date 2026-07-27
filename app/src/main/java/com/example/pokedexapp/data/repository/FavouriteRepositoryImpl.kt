package com.example.pokedexapp.data.repository

import com.example.pokedexapp.data.local.dao.FavouritePokemonDao
import com.example.pokedexapp.data.local.entities.FavouritePokemonEntity
import com.example.pokedexapp.domain.model.PokemonResults
import com.example.pokedexapp.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val dao: FavouritePokemonDao
) : FavouriteRepository {

    override suspend fun addFavourite(pokemon: PokemonResults) {
        dao.insertFavourite(
            FavouritePokemonEntity(
                id = pokemon.id,
                name = pokemon.name,
                url = pokemon.url,
                imageUrl = pokemon.imageUrl,
                types = pokemon.types
            )
        )
    }

    override suspend fun removeFavourite(pokemonId: Int) {
        dao.deleteFavouriteById(pokemonId)
    }

    override fun getFavouritePokemon(): Flow<List<PokemonResults>> {
        return dao.getFavourite().map { favourites ->
            favourites.map { entity ->
                PokemonResults(
                    name = entity.name,
                    url = entity.url,
                    imageUrl = entity.imageUrl,
                    types = entity.types
                )
            }
        }
    }

    override fun getFavouritePokemonIds(): Flow<Set<Int>> {
        return dao.getFavourite().map { favourites ->
            favourites.map { it.id }.toSet()
        }
    }
}
