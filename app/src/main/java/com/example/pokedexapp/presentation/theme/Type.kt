package com.example.pokedexapp.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.pokedexapp.R

val FredokaFontFamily = FontFamily(
    Font(R.font.fredoka_regular, FontWeight.Normal),
    Font(R.font.fredoka_medium, FontWeight.Medium),
    Font(R.font.fredoka_semibold, FontWeight.SemiBold),
    Font(R.font.fredoka_bold, FontWeight.Bold),
)

val NunitoFontFamily = FontFamily(
    Font(R.font.nunito_regular, FontWeight.Normal),
    Font(R.font.nunito_medium, FontWeight.Medium),
    Font(R.font.nunito_semibold, FontWeight.SemiBold),
    Font(R.font.nunito_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
val baseline = Typography()
val Typography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = FredokaFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = FredokaFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = FredokaFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = FredokaFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = FredokaFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = FredokaFontFamily),
    titleLarge = baseline.titleLarge.copy(
        fontFamily = FredokaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = baseline.titleMedium.copy(
        fontFamily = FredokaFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = baseline.titleSmall.copy(fontFamily = FredokaFontFamily),
    bodyLarge = baseline.bodyLarge.copy(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = NunitoFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = NunitoFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = NunitoFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = NunitoFontFamily),
    labelSmall = baseline.labelSmall.copy(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
