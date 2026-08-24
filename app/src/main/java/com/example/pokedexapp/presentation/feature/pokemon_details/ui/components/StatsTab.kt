package com.example.pokedexapp.presentation.feature.pokemon_details.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokedexapp.domain.model.PokemonDetails
import com.example.pokedexapp.presentation.components.PokemonTypeUtils
import com.example.pokedexapp.presentation.components.StatBar
import com.example.pokedexapp.presentation.mock.MockData
import com.example.pokedexapp.utils.StatFormatter

@Composable
fun StatsTab(details: PokemonDetails) {
    val statKeys = listOf("hp", "attack", "defense", "special-attack", "special-defense", "speed")
    val statMap = statKeys.map { key ->
        stringResource(StatFormatter.getStatAbbreviationRes(key)) to details.stats.find { it.stat.name == key }?.baseStat
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        statMap.forEach { (name, value) ->
            StatBar(
                statName = name,
                statValue = value ?: 0,
                color = PokemonTypeUtils.getGradientForType(details.types.firstOrNull()?.type?.name).start
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatsTabPreview() {
    com.example.pokedexapp.presentation.theme.PokeDexAppTheme {
        StatsTab(details = MockData.mockDetails)
    }
}
