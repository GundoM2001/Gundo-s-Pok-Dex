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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedexapp.R
import com.example.pokedexapp.domain.model.MachineDetails
import com.example.pokedexapp.domain.model.MoveDetails
import com.example.pokedexapp.domain.model.PokemonDetails
import com.example.pokedexapp.presentation.components.PokemonTypeBadge
import com.example.pokedexapp.presentation.mock.MockData
import com.example.pokedexapp.utils.PokemonNameFormatter

@Composable
fun MovesTab(
    details: PokemonDetails,
    moveDetails: Map<String, MoveDetails> = emptyMap(),
    machineDetails: Map<String, MachineDetails> = emptyMap()
) {
    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }
    
    val levelUpHeader = stringResource(R.string.move_learn_level_up)
    val evolutionHeader = stringResource(R.string.move_learn_evolution)
    val machineHeader = stringResource(R.string.move_learn_machine)
    val tutorHeader = stringResource(R.string.move_learn_tutor)
    val eggHeader = "Egg Moves"
    val otherHeader = "Other Moves"

    val groupedMoves = remember(details.moves, moveDetails, machineDetails, levelUpHeader, evolutionHeader, machineHeader, tutorHeader, eggHeader, otherHeader) {
        val levelUp = details.moves.filter { move ->
            move.versionGroupDetails.any { it.moveLearnMethod.name == "level-up" }
        }.sortedBy { move ->
            move.versionGroupDetails.find { it.moveLearnMethod.name == "level-up" }?.levelLearnedAt ?: 0
        }

        val machine = details.moves.filter { move ->
            move.versionGroupDetails.any { it.moveLearnMethod.name == "machine" }
        }.sortedBy { moveEntry ->
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

        val egg = details.moves.filter { move ->
            move.versionGroupDetails.any { it.moveLearnMethod.name == "egg" }
        }.sortedBy { it.move.name }

        val other = details.moves.filter { move ->
            move.versionGroupDetails.any { 
                it.moveLearnMethod.name !in listOf("level-up", "machine", "tutor", "evolution", "egg") 
            }
        }.sortedBy { it.move.name }

        listOf(
            levelUpHeader to levelUp,
            evolutionHeader to evolution,
            machineHeader to machine,
            tutorHeader to tutor,
            eggHeader to egg,
            otherHeader to other
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
                        val levelUpHeader = stringResource(R.string.move_learn_level_up)
                        val evolutionHeader = stringResource(R.string.move_learn_evolution)
                        val machineHeader = stringResource(R.string.move_learn_machine)

                        val methodKey = when {
                            header == levelUpHeader -> "level-up"
                            header == evolutionHeader -> "evolution"
                            header == machineHeader -> "machine"
                            header == eggHeader -> "egg"
                            header == tutorHeader -> "tutor"
                            else -> moveEntry.versionGroupDetails.firstOrNull()?.moveLearnMethod?.name ?: ""
                        }
                        val learnDetail = moveEntry.versionGroupDetails.find { it.moveLearnMethod.name == methodKey }
                        val details = moveDetails[moveEntry.move.name]
                        
                        // Extract machine number if applicable
                        val machineUrl = details?.machines?.firstOrNull()?.machine?.url
                        val machineNumber = machineDetails[machineUrl]?.item?.name?.uppercase()
                        val isExpanded = expandedStates[moveEntry.move.name] ?: false
                        val rotation by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    expandedStates[moveEntry.move.name] = !isExpanded 
                                },
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
                                            text = PokemonNameFormatter.format(moveEntry.move.name),
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
                                                text = stringResource(R.string.move_level_label, level),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        } else if (machineNumber != null) {
                                            Text(
                                                text = machineNumber,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        } else if (header == stringResource(R.string.move_learn_tutor)) {
                                            Text(
                                                text = stringResource(R.string.move_tutor_label),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        } else if (header == evolutionHeader) {
                                            Text(
                                                text = stringResource(R.string.move_evolution_label),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        } else if (header == eggHeader) {
                                            Text(
                                                text = "EGG",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        } else if (header == otherHeader) {
                                            Text(
                                                text = methodKey.uppercase(),
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
                                                    MoveStatItem(label = stringResource(R.string.move_stat_power), value = details.power.toString())
                                                }
                                                if (details.pp != null) {
                                                    MoveStatItem(label = stringResource(R.string.move_stat_pp), value = details.pp.toString())
                                                }
                                                if (details.accuracy != null) {
                                                    MoveStatItem(label = stringResource(R.string.move_stat_accuracy), value = "${details.accuracy}%")
                                                }
                                            }
                                        }
                                    }

                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = if (isExpanded) stringResource(R.string.collapse_desc) else stringResource(R.string.expand_desc),
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
                                        val currentLanguage = java.util.Locale.getDefault().language
                                        val effect = details?.flavorTextEntries
                                            ?.firstOrNull { it.language.name == currentLanguage }
                                            ?.flavorText?.replace("\n", " ")
                                            ?: details?.flavorTextEntries
                                                ?.firstOrNull { it.language.name == "en" }
                                                ?.flavorText?.replace("\n", " ")
                                            ?: details?.effectEntries
                                                ?.firstOrNull { it.language.name == currentLanguage }
                                                ?.shortEffect
                                            ?: details?.effectEntries
                                                ?.firstOrNull { it.language.name == "en" }
                                                ?.shortEffect
                                        
                                        Text(
                                            text = effect ?: if (details == null) stringResource(R.string.loading_description) else stringResource(R.string.no_description),
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
