package com.example.pokedexapp.presentation.feature.team_builder.team_builder_home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.pokedexapp.R
import com.example.pokedexapp.data.local.entities.TeamEntity
import com.example.pokedexapp.data.local.entities.TeamPokemonEntity
import com.example.pokedexapp.data.local.entities.TeamWithPokemon
import com.example.pokedexapp.presentation.theme.PokeDexAppTheme
import com.example.pokedexapp.utils.PokemonImageUtils

@Composable
fun TeamCard(
    teamWithPokemon: TeamWithPokemon,
    onClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = teamWithPokemon.team.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Team Name",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (i in 0 until 6) {
                    val pokemon = teamWithPokemon.pokemon.find { it.slot == i }
                    PokemonSlot(pokemonId = pokemon?.pokemonId)
                }
            }
        }
    }
}

@Composable
fun PokemonSlot(pokemonId: Int?) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (pokemonId != null) {
            AsyncImage(
                model = PokemonImageUtils.getOfficialArtworkUrl(pokemonId),
                contentDescription = null,
                placeholder = painterResource(R.drawable.ic_pokeball),
                error = painterResource(R.drawable.ic_pokeball),
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_pokeball),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeamCardPreview() {
    val sampleTeam = TeamWithPokemon(
        team = TeamEntity(id = 1, name = "Kanto Champions"),
        pokemon = listOf(
            TeamPokemonEntity(teamId = 1, pokemonId = 1, slot = 0),
            TeamPokemonEntity(teamId = 1, pokemonId = 4, slot = 1),
            TeamPokemonEntity(teamId = 1, pokemonId = 7, slot = 2)
        )
    )
    PokeDexAppTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            TeamCard(
                teamWithPokemon = sampleTeam,
                onClick = {},
                onEditClick = {}
            )
        }
    }
}
