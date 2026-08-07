package com.example.pokedexapp.presentation.feature.team_builder.pokemon_customization.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.pokedexapp.R
import com.example.pokedexapp.domain.model.MoveDetails
import com.example.pokedexapp.domain.model.Nature
import com.example.pokedexapp.domain.model.PokemonDetails
import com.example.pokedexapp.presentation.components.ErrorState
import com.example.pokedexapp.presentation.components.PokemonTypeBadge
import com.example.pokedexapp.presentation.mock.MockData
import com.example.pokedexapp.presentation.theme.PokeDexAppTheme
import com.example.pokedexapp.utils.PokemonImageUtils
import com.example.pokedexapp.utils.PokemonNameFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonCustomizationScreen(
    viewModel: PokemonCustomizationViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.customize_pokemon_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back_desc))
                    }
                },
                actions = {
                    Button(
                        onClick = { viewModel.save(onSaveSuccess) },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(stringResource(R.string.btn_save))
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.error != null && state.pokemonDetails == null) {
            ErrorState(
                error = state.error,
                onRetry = { viewModel.onRetry() }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                state.pokemonDetails?.let { details ->
                    AsyncImage(
                        model = PokemonImageUtils.getOfficialArtworkUrl(details.id),
                        contentDescription = null,
                        modifier = Modifier.size(150.dp)
                    )
                    
                    Text(
                        text = PokemonNameFormatter.format(details.name),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (state.varieties.size > 1) {
                        Spacer(modifier = Modifier.height(16.dp))
                        VarietySelector(
                            currentVariety = details,
                            allVarieties = state.varieties,
                            onVarietySelected = { viewModel.onVarietyChanged(it) }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.nickname ?: "",
                        onValueChange = { viewModel.onNicknameChanged(it) },
                        label = { Text(stringResource(R.string.nickname_label)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    StatRow(label = stringResource(R.string.level_label), value = state.level, range = 1f..100f) {
                        viewModel.onLevelChanged(it)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    NatureSelector(selectedNature = state.selectedNature) {
                        viewModel.onNatureChanged(it)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    AbilitySelector(
                        selectedAbility = state.selectedAbility ?: "",
                        abilities = details.abilities.map { it.ability.name },
                        isEditable = details.abilities.size > 1,
                        onAbilitySelected = { viewModel.onAbilityChanged(it) }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

                    Text(
                        text = stringResource(R.string.tab_moves),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
                    )
                    
                    val allMoveEntries = details.moves
                    
                    MoveSelector(1, state.move1, allMoveEntries, state.moveDetails, state.moveSearchQuery, { viewModel.onMoveSearchQueryChanged(it) }) { viewModel.onMoveSelected(1, it) }
                    MoveSelector(2, state.move2, allMoveEntries, state.moveDetails, state.moveSearchQuery, { viewModel.onMoveSearchQueryChanged(it) }) { viewModel.onMoveSelected(2, it) }
                    MoveSelector(3, state.move3, allMoveEntries, state.moveDetails, state.moveSearchQuery, { viewModel.onMoveSearchQueryChanged(it) }) { viewModel.onMoveSelected(3, it) }
                    MoveSelector(4, state.move4, allMoveEntries, state.moveDetails, state.moveSearchQuery, { viewModel.onMoveSearchQueryChanged(it) }) { viewModel.onMoveSelected(4, it) }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

                    val totalEvs = state.hpEv + state.atkEv + state.defEv + state.spaEv + state.spdEv + state.speEv
                    Text(
                        text = stringResource(R.string.evs_label, totalEvs),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(Alignment.Start).padding(bottom = 16.dp)
                    )

                    val stats = listOf(
                        Triple(stringResource(R.string.stat_hp), details.stats.find { it.stat.name == "hp" }?.baseStat ?: 0, "hp"),
                        Triple(stringResource(R.string.stat_atk), details.stats.find { it.stat.name == "attack" }?.baseStat ?: 0, "attack"),
                        Triple(stringResource(R.string.stat_def), details.stats.find { it.stat.name == "defense" }?.baseStat ?: 0, "defense"),
                        Triple(stringResource(R.string.stat_spa), details.stats.find { it.stat.name == "special-attack" }?.baseStat ?: 0, "special-attack"),
                        Triple(stringResource(R.string.stat_spd), details.stats.find { it.stat.name == "special-defense" }?.baseStat ?: 0, "special-defense"),
                        Triple(stringResource(R.string.stat_spe), details.stats.find { it.stat.name == "speed" }?.baseStat ?: 0, "speed")
                    )

                    val currentNature = Nature.fromName(state.selectedNature)

                    stats.forEach { (label, base, statKey) ->
                        val ev = when(statKey) {
                            "hp" -> state.hpEv
                            "attack" -> state.atkEv
                            "defense" -> state.defEv
                            "special-attack" -> state.spaEv
                            "special-defense" -> state.spdEv
                            "speed" -> state.speEv
                            else -> 0
                        }
                        val iv = when(statKey) {
                            "hp" -> state.hpIv
                            "attack" -> state.atkIv
                            "defense" -> state.defIv
                            "special-attack" -> state.spaIv
                            "special-defense" -> state.spdIv
                            "speed" -> state.speIv
                            else -> 31
                        }
                        
                        val isHp = label == "HP"
                        val finalStat = viewModel.calculateStat(base, ev, iv, state.level, isHp, if (isHp) null else statKey)
                        
                        val multiplier = if (isHp) 1.0 else currentNature.getMultiplierForStat(statKey)
                        val multiplierColor = when {
                            multiplier > 1.0 -> Color(0xFFE57373) // Light Red for Boost
                            multiplier < 1.0 -> Color(0xFF64B5F6) // Light Blue for Hindrance
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }

                        StatEditor(
                            label = label,
                            base = base,
                            ev = ev,
                            iv = iv,
                            finalStat = finalStat,
                            multiplierLabel = when {
                                multiplier > 1.0 -> "(+)"
                                multiplier < 1.0 -> "(-)"
                                else -> null
                            },
                            multiplierColor = multiplierColor,
                            onEvChange = { viewModel.onEvChanged(label, it) },
                            onIvChange = { viewModel.onIvChanged(label, it) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun VarietySelector(
    currentVariety: PokemonDetails,
    allVarieties: List<PokemonDetails>,
    onVarietySelected: (PokemonDetails) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = PokemonNameFormatter.format(currentVariety.name),
            onValueChange = {},
            label = { Text(stringResource(R.string.form_label)) },
            modifier = Modifier.fillMaxWidth().clickable { expanded = true },
            readOnly = true,
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledBorderColor = MaterialTheme.colorScheme.outline
            )
        )
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            allVarieties.forEach { variety ->
                DropdownMenuItem(
                    text = { Text(PokemonNameFormatter.format(variety.name)) },
                    onClick = {
                        onVarietySelected(variety)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun NatureSelector(selectedNature: String, onNatureSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedNature,
            onValueChange = {},
            label = { Text(stringResource(R.string.nature_label)) },
            modifier = Modifier.fillMaxWidth().clickable { expanded = true },
            readOnly = true,
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledBorderColor = MaterialTheme.colorScheme.outline
            )
        )
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f).heightIn(max = 400.dp)
        ) {
            Nature.entries.forEach { nature ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(nature.displayName, modifier = Modifier.weight(1f))
                            if (nature.boostedStat != null && nature.hinderedStat != null) {
                                Text(
                                    text = "(+${nature.boostedStat.take(3).uppercase()} / -${nature.hinderedStat.take(3).uppercase()})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    onClick = {
                        onNatureSelected(nature.displayName)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AbilitySelector(
    selectedAbility: String,
    abilities: List<String>,
    isEditable: Boolean,
    onAbilitySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedAbility.replace("-", " ").uppercase(),
            onValueChange = {},
            label = { Text(stringResource(R.string.ability_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .then(if (isEditable) Modifier.clickable { expanded = true } else Modifier),
            readOnly = true,
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        if (isEditable) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                abilities.forEach { ability ->
                    DropdownMenuItem(
                        text = { Text(ability.replace("-", " ").uppercase()) },
                        onClick = {
                            onAbilitySelected(ability)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StatEditor(
    label: String,
    base: Int,
    ev: Int,
    iv: Int,
    finalStat: Int,
    multiplierLabel: String?,
    multiplierColor: Color,
    onEvChange: (Int) -> Unit,
    onIvChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = label, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    if (multiplierLabel != null) {
                        Text(
                            text = multiplierLabel,
                            color = multiplierColor,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 4.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Text(
                    text = finalStat.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Text(
                text = stringResource(R.string.base_stat_label, base),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            StatRow(label = stringResource(R.string.ev_label), value = ev, range = 0f..252f, onValueChange = onEvChange)
            StatRow(label = stringResource(R.string.iv_label), value = iv, range = 0f..31f, onValueChange = onIvChange)
        }
    }
}

@Composable
fun StatRow(label: String, value: Int, range: ClosedFloatingPointRange<Float>, onValueChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$label: $value", modifier = Modifier.width(70.dp), style = MaterialTheme.typography.bodyMedium)
        
        IconButton(
            onClick = { onValueChange((value - 1).coerceIn(range.start.toInt(), range.endInclusive.toInt())) },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = stringResource(R.string.decrease_desc),
                modifier = Modifier.size(18.dp)
            )
        }

        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = range,
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = { onValueChange((value + 1).coerceIn(range.start.toInt(), range.endInclusive.toInt())) },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.increase_desc),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoveSelector(
    index: Int,
    currentMove: String?,
    allMoveEntries: List<com.example.pokedexapp.domain.model.Move>,
    moveDetailsMap: Map<String, MoveDetails>,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onMoveSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        OutlinedCard(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            val details = currentMove?.let { moveDetailsMap[it] }
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentMove?.replace("-", " ")?.uppercase() ?: stringResource(R.string.empty_slot_index, index),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    if (details != null) {
                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PokemonTypeBadge(type = details.type.name)
                            Text(
                                text = "${stringResource(R.string.move_stat_power)}: ${details.power ?: "--"}  ${stringResource(R.string.move_stat_accuracy)}: ${details.accuracy ?: "--"}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        if (expanded) {
            AlertDialog(
                onDismissRequest = { expanded = false },
                title = { Text(stringResource(R.string.tab_moves)) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChanged,
                            placeholder = { Text(stringResource(R.string.search_moves_placeholder)) },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            singleLine = true
                        )
                        
                        Box(modifier = Modifier.height(400.dp)) {
                            val filteredMoves = remember(allMoveEntries, searchQuery) {
                                allMoveEntries.filter { 
                                    it.move.name.contains(searchQuery, ignoreCase = true) 
                                }
                            }
                            
                            LazyColumn {
                                item {
                                    TextButton(
                                        onClick = { onMoveSelected(null); expanded = false },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(stringResource(R.string.none_label), modifier = Modifier.fillMaxWidth())
                                    }
                                }
                                items(filteredMoves) { moveEntry ->
                                    val moveName = moveEntry.move.name
                                    val details = moveDetailsMap[moveName]
                                    
                                    Card(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        onClick = { onMoveSelected(moveName); expanded = false },
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                text = moveName.replace("-", " ").uppercase(),
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                            if (details != null) {
                                                Row(
                                                    modifier = Modifier.padding(top = 4.dp),
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    PokemonTypeBadge(type = details.type.name)
                                                    Text(
                                                        text = "${stringResource(R.string.move_stat_power)}: ${details.power ?: "--"} ${stringResource(R.string.move_stat_accuracy)}: ${details.accuracy ?: "--"} ${stringResource(R.string.move_stat_pp)}: ${details.pp ?: "--"}",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            } else {
                                                Text(stringResource(R.string.loading_description), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonCustomizationScreenPreview() {
    PokeDexAppTheme {
        // Simplified content for preview
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = PokemonImageUtils.getOfficialArtworkUrl(25),
                    contentDescription = null,
                    modifier = Modifier.size(150.dp)
                )
                Text(
                    text = "Pikachu",
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                VarietySelector(
                    currentVariety = MockData.mockDetails,
                    allVarieties = listOf(MockData.mockDetails, MockData.mockDetails.copy(name = "pikachu-mega")),
                    onVarietySelected = {}
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = "Sparky",
                    onValueChange = {},
                    label = { Text("Nickname") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
