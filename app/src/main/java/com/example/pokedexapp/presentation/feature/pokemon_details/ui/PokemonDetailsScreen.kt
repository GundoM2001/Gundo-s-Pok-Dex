package com.example.pokedexapp.presentation.feature.pokemon_details.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.pokedexapp.domain.model.PokemonDetails
import com.example.pokedexapp.domain.model.PokemonSpecies
import com.example.pokedexapp.presentation.components.TabRow
import com.example.pokedexapp.presentation.components.PokemonTypeBadge
import com.example.pokedexapp.presentation.components.PokemonTypeUtils
import com.example.pokedexapp.presentation.feature.pokemon_details.ui.components.AbilitiesTab
import com.example.pokedexapp.presentation.feature.pokemon_details.ui.components.MovesTab
import com.example.pokedexapp.presentation.feature.pokemon_details.ui.components.PokedexEntriesTab
import com.example.pokedexapp.presentation.feature.pokemon_details.ui.components.PokemonDetailsHeader
import com.example.pokedexapp.presentation.feature.pokemon_details.ui.components.StatsTab
import com.example.pokedexapp.presentation.feature.pokemon_details.ui.components.WeaknessTab
import com.example.pokedexapp.utils.PokemonNameFormatter

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
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
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
                    val headerHeight = 350.dp
                    
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        PokemonDetailsHeader(
                            activeDetails = activeDetails,
                            variants = variants,
                            onVariantChanged = onVariantChanged
                        )

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

                                TabRow(
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
