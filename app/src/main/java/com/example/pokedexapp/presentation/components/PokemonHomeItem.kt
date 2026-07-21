package com.example.pokedexapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.pokedexapp.R
import com.example.pokedexapp.domain.model.PokemonResults
import com.example.pokedexapp.utils.PokemonNameFormatter

@Composable
fun PokemonItem(
    pokemon: PokemonResults,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isFavorite: Boolean,
    onFavouriteClick: () -> Unit = {}
) {
    val gradient = PokemonTypeUtils.getGradientForType(
        pokemon.types?.firstOrNull()
    )

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            gradient.start,
                            gradient.end
                        )
                    )
                )
        ) {
            PokemonWatermark(
                modifier = Modifier.align(Alignment.BottomEnd)
            )

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 4.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${pokemon.id.toString().padStart(3, '0')}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.4f)
                )

                IconButton(
                    onClick = { onFavouriteClick() },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Black.copy(alpha = 0.4f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 16.dp,
                        top = 12.dp,
                        end = 12.dp,
                        bottom = 10.dp
                    )
            ) {
                // The name gets the entire card width.
                Text(
                    text = PokemonNameFormatter.format(pokemon.name),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Clip
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PokemonTypes(
                        types = pokemon.types,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    PokemonImage(
                        pokemon = pokemon,
                        size = 76.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun PokemonTypes(
    types: List<String>?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        types
            ?.take(2)
            ?.forEachIndexed { index, type ->
                PokemonTypePill(type = type)

                if (index < types.take(2).lastIndex) {
                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )
                }
            }
    }
}

@Composable
private fun PokemonWatermark(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(
            id = R.drawable.ic_pokeball
        ),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .size(140.dp)
            .offset(
                x = 28.dp,
                y = 28.dp
            )
            .alpha(0.15f)
    )
}

@Composable
private fun PokemonImage(
    pokemon: PokemonResults,
    size: Dp,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = pokemon.imageUrl,
        contentDescription = pokemon.name,
        contentScale = ContentScale.Fit,
        placeholder = painterResource(
            id = R.drawable.ic_launcher_foreground
        ),
        error = painterResource(
            id = R.drawable.ic_launcher_foreground
        ),
        modifier = modifier.size(size)
    )
}

@Composable
fun PokemonTypePill(
    type: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(
                Color.Black.copy(alpha = 0.1f)
            )
            .padding(
                horizontal = 10.dp,
                vertical = 4.dp
            )
    ) {
        Text(
            text = type.replaceFirstChar { it.uppercase() },
            color = Color.Black,
            fontSize = 11.sp,
            lineHeight = 13.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 360
)
@Composable
private fun PokemonItemPreview() {
    PokemonItem(
        pokemon = PokemonResults(
            name = "gouging-fire",
            url = "",
            imageUrl = "",
            types = listOf(
                "fire",
                "dragon",
            )
        ),
        modifier = Modifier.padding(16.dp),
        isFavorite = true
    )
}