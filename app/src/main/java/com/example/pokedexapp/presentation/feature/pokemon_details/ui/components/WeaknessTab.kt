package com.example.pokedexapp.presentation.feature.pokemon_details.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedexapp.presentation.components.PokemonTypeBadge
import com.example.pokedexapp.presentation.mock.MockData

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WeaknessTab(advantages: Map<String, Double>) {
    val items = advantages.filter { it.value != 1.0 }.toList()
    
    val weaknesses = items.filter { it.second > 1.0 }.sortedByDescending { it.second }
    val resistances = items.filter { it.second < 1.0 && it.second > 0.0 }.sortedBy { it.second }
    val immunities = items.filter { it.second == 0.0 }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (weaknesses.isNotEmpty()) {
            WeaknessSection(title = "Weaknesses", types = weaknesses, color = MaterialTheme.colorScheme.error)
        }
        
        if (resistances.isNotEmpty()) {
            WeaknessSection(title = "Resistances", types = resistances, color = Color(0xFF2E7D32))
        }

        if (immunities.isNotEmpty()) {
            WeaknessSection(title = "Immunities", types = immunities, color = MaterialTheme.colorScheme.outline)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WeaknessSection(
    title: String,
    types: List<Pair<String, Double>>,
    color: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            types.forEach { (type, multiplier) ->
                TypeMultiplierBadge(type = type, multiplier = multiplier)
            }
        }
    }
}

@Composable
private fun TypeMultiplierBadge(type: String, multiplier: Double) {
    Box(contentAlignment = Alignment.TopEnd) {
        PokemonTypeBadge(
            type = type,
            modifier = Modifier.padding(top = 4.dp, end = 4.dp)
        )
        
        val multiplierText = when (multiplier) {
            4.0 -> "4x"
            2.0 -> "2x"
            0.5 -> "½x"
            0.25 -> "¼x"
            0.0 -> "0x"
            else -> "${multiplier.toString().removeSuffix(".0")}x"
        }
        
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = multiplierText,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeaknessTabPreview() {
    com.example.pokedexapp.presentation.theme.PokeDexAppTheme {
        WeaknessTab(advantages = MockData.mockTypeAdvantages)
    }
}
