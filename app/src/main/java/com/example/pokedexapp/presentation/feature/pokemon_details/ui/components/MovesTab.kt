package com.example.pokedexapp.presentation.feature.pokemon_details.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedexapp.domain.model.MachineDetails
import com.example.pokedexapp.domain.model.MoveDetails
import com.example.pokedexapp.domain.model.PokemonDetails
import com.example.pokedexapp.presentation.components.PokemonTypeBadge
import com.example.pokedexapp.presentation.mock.MockData

@Composable
fun MovesTab(
    details: PokemonDetails,
    moveDetails: Map<String, MoveDetails> = emptyMap(),
    machineDetails: Map<String, MachineDetails> = emptyMap()
) {
    val groupedMoves = remember(details.moves, machineDetails) {
        val levelUp = details.moves.filter { move ->
            move.versionGroupDetails.any { it.moveLearnMethod.name == "level-up" }
        }.sortedBy { move ->
            move.versionGroupDetails.find { it.moveLearnMethod.name == "level-up" }?.levelLearnedAt ?: 0
        }

        val machine = details.moves.filter { move ->
            move.versionGroupDetails.any { it.moveLearnMethod.name == "machine" }
        }.sortedBy { moveEntry ->
            // Try to find the TM/HM number for sorting
            val mDetails = moveDetails[moveEntry.move.name]
            val machineUrl = mDetails?.machines?.firstOrNull()?.machine?.url
            val numberStr = machineDetails[machineUrl]?.item?.name?.filter { it.isDigit() }
            numberStr?.toIntOrNull() ?: Int.MAX_VALUE
        }

        val tutor = details.moves.filter { move ->
            move.versionGroupDetails.any { it.moveLearnMethod.name == "tutor" }
        }.sortedBy { it.move.name }

        val evolution = details.moves.filter { move ->
            move.versionGroupDetails.any { it.moveLearnMethod.name == "evolution" }
        }.sortedBy { it.move.name }

        listOf(
            "Level Up" to levelUp,
            "Evolution" to evolution,
            "TMs / HMs" to machine,
            "Move Tutor" to tutor
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
                        val methodKey = when {
                            header == "Level Up" -> "level-up"
                            header == "Evolution" -> "evolution"
                            header.contains("TM") -> "machine"
                            else -> "tutor"
                        }
                        val learnDetail = moveEntry.versionGroupDetails.find { it.moveLearnMethod.name == methodKey }
                        val details = moveDetails[moveEntry.move.name]
                        
                        // Extract machine number if applicable
                        val machineUrl = details?.machines?.firstOrNull()?.machine?.url
                        val machineNumber = machineDetails[machineUrl]?.item?.name?.uppercase()
                        var isExpanded by remember { mutableStateOf(false) }
                        val rotation by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isExpanded = !isExpanded },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = moveEntry.move.name.replace("-", " ").uppercase(),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        details?.let {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                modifier = Modifier.padding(top = 4.dp)
                                            ) {
                                                PokemonTypeBadge(
                                                    type = it.type.name,
                                                    modifier = Modifier.height(24.dp)
                                                )
                                                it.damageClass?.let { damageClass ->
                                                    Text(
                                                        text = damageClass.name.uppercase(),
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Column(
                                        horizontalAlignment = Alignment.End,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    ) {
                                        val level = learnDetail?.levelLearnedAt ?: 0
                                        if (level > 0) {
                                            Text(
                                                text = "Lvl $level",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        } else if (machineNumber != null) {
                                            Text(
                                                text = machineNumber,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        } else if (header.contains("Tutor")) {
                                            Text(
                                                text = "TUTOR",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        } else if (header == "Evolution") {
                                            Text(
                                                text = "EVO",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        if (details != null) {
                                            Row(
                                                modifier = Modifier.padding(top = 4.dp),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                if (details.power != null) {
                                                    MoveStatItem(label = "PWR", value = details.power.toString())
                                                }
                                                if (details.pp != null) {
                                                    MoveStatItem(label = "PP", value = details.pp.toString())
                                                }
                                                if (details.accuracy != null) {
                                                    MoveStatItem(label = "ACC", value = "${details.accuracy}%")
                                                }
                                            }
                                        }
                                    }

                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .rotate(rotation)
                                    )
                                }

                                AnimatedVisibility(visible = isExpanded) {
                                    Column(modifier = Modifier.padding(top = 12.dp)) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(bottom = 8.dp),
                                            thickness = 0.5.dp,
                                            color = MaterialTheme.colorScheme.outlineVariant
                                        )
                                        // Priority: Flavor text (cleaner description) -> Short effect -> No description
                                        val effect = details?.flavorTextEntries
                                            ?.firstOrNull { it.language.name == "en" }
                                            ?.flavorText?.replace("\n", " ")
                                            ?: details?.effectEntries
                                                ?.firstOrNull { it.language.name == "en" }
                                                ?.shortEffect
                                        
                                        Text(
                                            text = effect ?: "No description available",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 16.sp,
                                            textAlign = TextAlign.Start
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
}

@Composable
private fun MoveStatItem(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovesTabPreview() {
    com.example.pokedexapp.presentation.theme.PokeDexAppTheme {
        MovesTab(
            details = MockData.mockDetails,
            moveDetails = MockData.mockMoveDetails
        )
    }
}
