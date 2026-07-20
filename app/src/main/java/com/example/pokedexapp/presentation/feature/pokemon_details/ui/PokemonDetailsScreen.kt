package com.example.pokedexapp.presentation.feature.pokemon_details.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.pokedexapp.domain.model.PokemonDetails
import com.example.pokedexapp.domain.model.PokemonSpecies
import com.example.pokedexapp.utils.PokemonNameFormatter
import com.example.pokedexapp.presentation.components.PokemonTypeBadge
import com.example.pokedexapp.presentation.components.PokemonTypeUtils
import com.example.pokedexapp.utils.PokemonVersionUtils
import com.example.pokedexapp.presentation.components.StatBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailsScreen(
    viewModel: PokemonDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val details by viewModel.pokemonDetails.collectAsState()
    val species by viewModel.pokemonSpecies.collectAsState()
    val variants by viewModel.pokemonVariants.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val typeAdvantages by viewModel.typeAdvantages.collectAsState()
    val abilityDetails by viewModel.abilityDetails.collectAsState()
    val selectedTabIndex by viewModel.selectedTabIndex.collectAsState()

    PokemonDetailsContent(
        details = details,
        species = species,
        variants = variants,
        isLoading = isLoading,
        error = error,
        typeAdvantages = typeAdvantages,
        abilityDetails = abilityDetails,
        selectedTabIndex = selectedTabIndex,
        onBackClick = onBackClick,
        onVariantChanged = { viewModel.onVariantChanged(it) },
        onTabSelected = { viewModel.onTabSelected(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailsContent(
    details: PokemonDetails?,
    species: PokemonSpecies?,
    variants: List<PokemonDetails>,
    isLoading: Boolean,
    error: String?,
    typeAdvantages: Map<String, Double>,
    abilityDetails: List<com.example.pokedexapp.domain.model.AbilityDetails>,
    selectedTabIndex: Int,
    onBackClick: () -> Unit,
    onVariantChanged: (PokemonDetails) -> Unit,
    onTabSelected: (Int) -> Unit
) {
    if (isLoading && details == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Error: $error", color = MaterialTheme.colorScheme.error)
        }
    } else {
        details?.let { activeDetails ->
            val gradient = PokemonTypeUtils.getGradientForType(activeDetails.types.firstOrNull()?.type?.name)
            val backgroundColor by animateColorAsState(
                targetValue = gradient.start,
                animationSpec = tween(durationMillis = 500),
                label = "backgroundColor"
            )

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {},
                        navigationIcon = {
                            IconButton(
                                onClick = onBackClick,
                                modifier = Modifier.size(48.dp) // Smaller icon button
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        // Reduced height for the TopAppBar
                        modifier = Modifier.height(48.dp),
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                    )
                },
                containerColor = backgroundColor
            ) { innerPadding ->
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = innerPadding.calculateTopPadding())
                ) {
                    val screenHeight = maxHeight
                    val scrollState = rememberScrollState()
                    
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        val headerHeight = 350.dp 

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(headerHeight),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (variants.isNotEmpty()) {
                                val initialPage = variants.indexOfFirst { it.id == activeDetails.id }.coerceAtLeast(0)
                                val pagerState = rememberPagerState(initialPage = initialPage) { variants.size }

                                LaunchedEffect(pagerState.currentPage) {
                                    onVariantChanged(variants[pagerState.currentPage])
                                }

                                HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(320.dp)
                                ) { page ->
                                    val variant = variants[page]
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(variant.sprites.other?.officialArtwork?.frontDefault)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = variant.name,
                                            modifier = Modifier.size(300.dp),
                                            contentScale = ContentScale.Fit
                                        )
                                    }
                                }

                                Row(
                                    Modifier
                                        .height(20.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    repeat(variants.size) { iteration ->
                                        val color = if (pagerState.currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
                                        Box(
                                            modifier = Modifier
                                                .padding(2.dp)
                                                .clip(CircleShape)
                                                .background(color)
                                                .size(if (pagerState.currentPage == iteration) 10.dp else 8.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = screenHeight - headerHeight),
                            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = PokemonNameFormatter.format(activeDetails.name),
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                
                                val genus = species?.genera?.firstOrNull { it.language.name == "en" }?.genus
                                if (genus != null) {
                                    Text(
                                        text = genus,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                }

                                Row(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    activeDetails.types.forEach { type ->
                                        PokemonTypeBadge(type = type.type.name)
                                    }
                                }

                                val description = species?.flavorTextEntries
                                    ?.firstOrNull { it.language.name == "en" }
                                    ?.flavorText?.replace("\n", " ")
                                
                                if (description != null) {
                                    Text(
                                        text = description,
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(vertical = 16.dp)
                                    )
                                }

                                ModernTabRow(
                                    selectedTabIndex = selectedTabIndex,
                                    onTabSelected = onTabSelected,
                                    activeColor = backgroundColor
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                when (selectedTabIndex) {
                                    0 -> StatsTab(activeDetails)
                                    1 -> MovesTab(activeDetails)
                                    2 -> AbilitiesTab(activeDetails, abilityDetails)
                                    3 -> WeaknessTab(typeAdvantages)
                                    4 -> PokedexEntriesTab(species)
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
fun ModernTabRow(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    activeColor: Color
) {
    val tabs = listOf("Stats", "Moves", "Abilities", "Weakness", "Entries")
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEachIndexed { index, title ->
            val isSelected = selectedTabIndex == index
            Surface(
                onClick = { onTabSelected(index) },
                shape = RoundedCornerShape(50),
                color = if (isSelected) activeColor else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                contentColor = if (isSelected) Color.White else activeColor,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier.padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun StatsTab(details: PokemonDetails) {
    val statMap = mapOf(
        "HP" to details.stats.find { it.stat.name == "hp" }?.baseStat,
        "ATK" to details.stats.find { it.stat.name == "attack" }?.baseStat,
        "DEF" to details.stats.find { it.stat.name == "defense" }?.baseStat,
        "SATK" to details.stats.find { it.stat.name == "special-attack" }?.baseStat,
        "SDEF" to details.stats.find { it.stat.name == "special-defense" }?.baseStat,
        "SPD" to details.stats.find { it.stat.name == "speed" }?.baseStat
    )

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

@Composable
fun AbilitiesTab(
    details: PokemonDetails,
    abilityDetails: List<com.example.pokedexapp.domain.model.AbilityDetails>
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        details.abilities.forEach { abilityEntry ->
            val detail = abilityDetails.find { it.name == abilityEntry.ability.name }
            val description = detail?.effectEntries?.find { it.language.name == "en" }?.shortEffect 
                ?: "No description available."

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = abilityEntry.ability.name.replace("-", " ").uppercase(),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (abilityEntry.isHidden) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            ) {
                                Text(
                                    text = "HIDDEN",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

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

@Composable
fun WeaknessTab(advantages: Map<String, Double>) {
    val items = advantages.filter { it.value != 1.0 }.toList().sortedByDescending { it.second }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { (type, multiplier) ->
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                multiplier > 1.0 -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                                multiplier < 1.0 -> Color(0xFFE8F5E9).copy(alpha = 0.5f)
                                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            PokemonTypeBadge(type = type)
                            Text(
                                text = "${multiplier}x",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    multiplier > 1.0 -> MaterialTheme.colorScheme.error
                                    multiplier < 1.0 -> Color(0xFF2E7D32)
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    }
                }
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonDetailsScreenPreview() {
    val mockDetails = PokemonDetails(
        id = 25,
        name = "pikachu",
        baseExperience = 112,
        height = 4,
        isDefault = true,
        order = 35,
        weight = 60,
        abilities = emptyList(),
        forms = emptyList(),
        gameIndices = emptyList(),
        heldItems = emptyList(),
        locationAreaEncounters = "",
        moves = emptyList(),
        species = com.example.pokedexapp.domain.model.NamedApiResource("pikachu", ""),
        sprites = com.example.pokedexapp.domain.model.Sprites(
            null, null, null, null, null, null, null, null,
            com.example.pokedexapp.domain.model.OtherSprites(
                null, null,
                com.example.pokedexapp.domain.model.SpriteVariant(
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png",
                    null, null, null, null, null, null, null
                ),
                null
            ),
            null
        ),
        cries = com.example.pokedexapp.domain.model.Cries("", ""),
        stats = listOf(
            com.example.pokedexapp.domain.model.Stat(35, 0, com.example.pokedexapp.domain.model.NamedApiResource("hp", "")),
            com.example.pokedexapp.domain.model.Stat(55, 0, com.example.pokedexapp.domain.model.NamedApiResource("attack", "")),
            com.example.pokedexapp.domain.model.Stat(40, 0, com.example.pokedexapp.domain.model.NamedApiResource("defense", "")),
            com.example.pokedexapp.domain.model.Stat(50, 0, com.example.pokedexapp.domain.model.NamedApiResource("special-attack", "")),
            com.example.pokedexapp.domain.model.Stat(50, 0, com.example.pokedexapp.domain.model.NamedApiResource("special-defense", "")),
            com.example.pokedexapp.domain.model.Stat(90, 0, com.example.pokedexapp.domain.model.NamedApiResource("speed", ""))
        ),
        types = listOf(
            com.example.pokedexapp.domain.model.Type(1, com.example.pokedexapp.domain.model.NamedApiResource("electric", ""))
        ),
        pastAbilities = emptyList(),
        pastStats = emptyList(),
        pastTypes = emptyList()
    )

    val mockSpecies = PokemonSpecies(
        id = 25,
        name = "pikachu",
        flavorTextEntries = listOf(
            com.example.pokedexapp.domain.model.FlavorTextEntry(
                "When several of these POKéMON gather, their electricity could build and cause lightning storms.",
                com.example.pokedexapp.domain.model.NamedApiResource("en", ""),
                com.example.pokedexapp.domain.model.NamedApiResource("red", "")
            ),
            com.example.pokedexapp.domain.model.FlavorTextEntry(
                "It keeps its tail raised to monitor its surroundings. If you pull its tail, it will try to bite you.",
                com.example.pokedexapp.domain.model.NamedApiResource("en", ""),
                com.example.pokedexapp.domain.model.NamedApiResource("yellow", "")
            )
        ),
        genera = listOf(
            com.example.pokedexapp.domain.model.Genus("Mouse Pokémon", com.example.pokedexapp.domain.model.NamedApiResource("en", ""))
        ),
        varieties = emptyList()
    )

    MaterialTheme {
        PokemonDetailsContent(
            details = mockDetails,
            species = mockSpecies,
            variants = listOf(mockDetails),
            isLoading = false,
            error = null,
            typeAdvantages = emptyMap(),
            abilityDetails = emptyList(),
            selectedTabIndex = 0,
            onBackClick = {},
            onVariantChanged = {},
            onTabSelected = {}
        )
    }
}
