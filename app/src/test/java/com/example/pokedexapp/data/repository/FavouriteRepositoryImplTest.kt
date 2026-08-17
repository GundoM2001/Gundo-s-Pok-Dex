package com.example.pokedexapp.data.repository

import app.cash.turbine.test
import com.example.pokedexapp.data.local.dao.FavouritePokemonDao
import com.example.pokedexapp.data.local.entities.FavouritePokemonEntity
import com.example.pokedexapp.domain.model.PokemonResults
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FavouriteRepositoryImplTest {

    private lateinit var repository: FavouriteRepositoryImpl
    private val dao: FavouritePokemonDao = mockk()

    @Before
    fun setup() {
        repository = FavouriteRepositoryImpl(dao)
    }

    @Test
    fun `addFavourite calls dao insert`() = runTest {
        val pokemon = PokemonResults("bulbasaur", "url", "image", listOf("grass"))
        coEvery { dao.insertFavourite(any()) } just Runs

        repository.addFavourite(pokemon)

        coVerify {
            dao.insertFavourite(match {
                it.name == "bulbasaur" && it.url == "url" && it.imageUrl == "image" && it.types == listOf("grass")
            })
        }
    }

    @Test
    fun `removeFavourite calls dao delete`() = runTest {
        coEvery { dao.deleteFavouriteById(1) } just Runs

        repository.removeFavourite(1)

        coVerify { dao.deleteFavouriteById(1) }
    }

    @Test
    fun `getFavouritePokemon maps entities to domain models`() = runTest {
        val entities = listOf(
            FavouritePokemonEntity(1, "bulbasaur", "url", "image", listOf("grass"))
        )
        every { dao.getFavourite() } returns flowOf(entities)

        repository.getFavouritePokemon().test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("bulbasaur", result[0].name)
            assertEquals("url", result[0].url)
            assertEquals("image", result[0].imageUrl)
            assertEquals(listOf("grass"), result[0].types)
            awaitComplete()
        }
    }

    @Test
    fun `getFavouritePokemonIds returns set of ids`() = runTest {
        val entities = listOf(
            FavouritePokemonEntity(1, "bulbasaur", "url", "image", listOf("grass")),
            FavouritePokemonEntity(4, "charmander", "url2", "image2", listOf("fire"))
        )
        every { dao.getFavourite() } returns flowOf(entities)

        repository.getFavouritePokemonIds().test {
            val result = awaitItem()
            assertEquals(setOf(1, 4), result)
            awaitComplete()
        }
    }
}
