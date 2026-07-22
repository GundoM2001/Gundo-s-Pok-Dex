package com.example.pokedexapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pokedexapp.R

@Composable
fun PokedexBackground(
    content: @Composable () -> Unit
) {
    val bgColor = MaterialTheme.colorScheme.background
    val pokeballColor = MaterialTheme.colorScheme.onBackground

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        // Large Pokeball watermark at the top right
        Image(
            painter = painterResource(id = R.drawable.ic_pokeball),
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-50).dp)
                .alpha(0.05f),
            colorFilter = ColorFilter.tint(pokeballColor)
        )

        content()
    }
}
