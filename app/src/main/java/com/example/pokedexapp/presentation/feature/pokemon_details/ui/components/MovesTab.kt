package com.example.pokedexapp.presentation.feature.pokemon_details.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pokedexapp.domain.model.PokemonDetails

@Composable
fun MovesTab(details: PokemonDetails) {
    val groupedMoves = remember(details.moves) {
        val levelUp = details.moves.filter { move ->
            move.versionGroupDetails.any { it.moveLearnMethod.name == "level-up" }
        }.sortedBy { move ->
            move.versionGroupDetails.find { it.moveLearnMethod.name == "level-up" }?.levelLearnedAt ?: 0
        }

        val machine = details.moves.filter { move ->
            move.versionGroupDetails.any { it.moveLearnMethod.name == "machine" }
        }.sortedBy { it.move.name }

        listOf(
            "Level Up" to levelUp,
            "TMs / HMs" to machine
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        groupedMoves.forEach { (header, moves) ->
            if (moves.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = header,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    moves.forEach { moveEntry ->
                        val methodKey = if (header == "Level Up") "level-up" else "machine"
                        val learnDetail = moveEntry.versionGroupDetails.find { it.moveLearnMethod.name == methodKey }
                        
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = moveEntry.move.name.replace("-", " ").uppercase(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                val level = learnDetail?.levelLearnedAt ?: 0
                                if (level > 0) {
                                    Text(
                                        text = "Lvl $level",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                } else if (header.contains("TM")) {
                                    Text(
                                        text = "TM",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
