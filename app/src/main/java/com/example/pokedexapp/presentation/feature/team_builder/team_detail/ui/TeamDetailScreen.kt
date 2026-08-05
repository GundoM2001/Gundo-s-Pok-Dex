package com.example.pokedexapp.presentation.feature.team_builder.team_detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.pokedexapp.R
import com.example.pokedexapp.data.local.entities.TeamEntity
import com.example.pokedexapp.data.local.entities.TeamPokemonEntity
import com.example.pokedexapp.data.local.entities.TeamWithPokemon
import com.example.pokedexapp.presentation.theme.PokeDexAppTheme
import com.example.pokedexapp.utils.PokemonImageUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailScreen(
    viewModel: TeamDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onAddPokemonClick: (Int, Int) -> Unit, // teamId, slot
    onMemberClick: (Int, Int, Int, Int) -> Unit // teamId, slot, pokemonId, memberId
) {
    val state by viewModel.state.collectAsState()
    val teamWithPokemon = state.team

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(teamWithPokemon?.team?.name ?: stringResource(R.string.team_details_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back_desc))
                    }
                }
            )
        }
    ) { padding ->
        teamWithPokemon?.let { team ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(count = 6) { index ->
                    val member = team.pokemon.find { it.slot == index }
                    TeamSlotItem(
                        slotIndex = index,
                        member = member,
                        onClick = {
                            if (member == null) {
                                onAddPokemonClick(team.team.id, index)
                            } else {
                                onMemberClick(team.team.id, index, member.pokemonId, member.id)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TeamSlotItem(
    slotIndex: Int,
    member: TeamPokemonEntity?,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (member == null) 
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                if (member != null) {
                    AsyncImage(
                        model = PokemonImageUtils.getOfficialArtworkUrl(member.pokemonId),
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.ic_pokeball),
                        error = painterResource(R.drawable.ic_pokeball),
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                if (member != null) {
                    Text(
                        text = stringResource(R.string.slot_label, slotIndex + 1),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    val displayName = if (!member.nickname.isNullOrBlank()) {
                        member.nickname
                    } else {
                        com.example.pokedexapp.utils.PokemonNameFormatter.format(member.pokemonName)
                    }
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    val moves = listOfNotNull(member.move1, member.move2, member.move3, member.move4)
                    if (moves.isNotEmpty()) {
                        Text(
                            text = moves.joinToString(" • "),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1
                        )
                    }
                } else {
                    Text(
                        text = stringResource(R.string.slot_label, slotIndex + 1),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = stringResource(R.string.empty_slot_label),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeamDetailScreenPreview() {
    val sampleTeam = TeamWithPokemon(
        team = TeamEntity(id = 1, name = "Kanto Champions"),
        pokemon = listOf(
            TeamPokemonEntity(
                id = 1, 
                teamId = 1, 
                pokemonId = 1, 
                pokemonName = "bulbasaur",
                slot = 0, 
                nickname = "Bulba",
                move1 = "Tackle",
                move2 = "Growl"
            ),
            TeamPokemonEntity(
                id = 2, 
                teamId = 1, 
                pokemonId = 4, 
                pokemonName = "charmander",
                slot = 1
            )
        )
    )
    PokeDexAppTheme {
        // Mocked view model behavior for preview isn't easy, but we can preview the content
        Surface {
            // Simplification for preview
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                repeat(6) { index ->
                    TeamSlotItem(
                        slotIndex = index,
                        member = sampleTeam.pokemon.find { it.slot == index },
                        onClick = {}
                    )
                }
            }
        }
    }
}
