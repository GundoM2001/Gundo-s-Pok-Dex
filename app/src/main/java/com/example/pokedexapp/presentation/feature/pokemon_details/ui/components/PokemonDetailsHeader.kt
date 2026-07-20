package com.example.pokedexapp.presentation.feature.pokemon_details.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.pokedexapp.domain.model.PokemonDetails

@Composable
fun PokemonDetailsHeader(
    activeDetails: PokemonDetails,
    variants: List<PokemonDetails>,
    onVariantChanged: (PokemonDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    val headerHeight = 350.dp

    Column(
        modifier = modifier
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
}
