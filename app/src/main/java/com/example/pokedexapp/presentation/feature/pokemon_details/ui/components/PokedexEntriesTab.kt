package com.example.pokedexapp.presentation.feature.pokemon_details.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pokedexapp.domain.model.PokemonSpecies
import com.example.pokedexapp.presentation.mock.MockData
import com.example.pokedexapp.utils.PokemonVersionUtils

@Composable
fun PokedexEntriesTab(species: PokemonSpecies?) {
    val entries = remember(species) {
        species?.flavorTextEntries
            ?.filter { it.language.name == "en" }
            ?.map { it.copy(flavorText = it.flavorText.replace("\n", " ").replace("\u000c", " ")) }
            ?.distinctBy { it.flavorText }
            ?.groupBy { PokemonVersionUtils.getGeneration(it.version.name) }
            ?: emptyMap()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        entries.forEach { (gen, versionEntries) ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = gen,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                versionEntries.forEach { entry ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = PokemonVersionUtils.formatVersionName(entry.version.name),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = entry.flavorText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokedexEntriesTabPreview() {
    com.example.pokedexapp.presentation.theme.PokeDexAppTheme {
        PokedexEntriesTab(species = MockData.mockSpecies)
    }
}
