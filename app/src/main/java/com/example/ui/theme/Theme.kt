package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = CoinGold,
    onPrimary = BoldSurface,
    primaryContainer = CoinGoldBg,
    onPrimaryContainer = CoinGoldDark,
    secondary = SaveBlue,
    onSecondary = BoldSurface,
    secondaryContainer = SaveBlueBg,
    onSecondaryContainer = SaveBlueDark,
    tertiary = GardenGreen,
    onTertiary = BoldSurface,
    tertiaryContainer = GardenGreenMint,
    onTertiaryContainer = GardenGreenDark,
    background = BoldCanvas,
    onBackground = BoldTextPrimary,
    surface = BoldSurface,
    onSurface = BoldTextPrimary,
    surfaceVariant = BoldSurfaceVariant,
    onSurfaceVariant = BoldTextSecondary,
    outline = BoldBorder,
    outlineVariant = BoldBorderSubtle
)

private val DarkColorScheme = darkColorScheme(
    primary = CoinGoldLight,
    onPrimary = BoldCanvasDark,
    primaryContainer = CoinGoldDark,
    onPrimaryContainer = CoinGoldBg,
    secondary = SaveBlueLight,
    onSecondary = BoldCanvasDark,
    secondaryContainer = SaveBlueDark,
    onSecondaryContainer = SaveBlueBg,
    tertiary = GardenGreenLight,
    onTertiary = BoldCanvasDark,
    tertiaryContainer = GardenGreenDark,
    onTertiaryContainer = GardenGreenMint,
    background = BoldCanvasDark,
    onBackground = BoldTextPrimaryDark,
    surface = BoldSurfaceDark,
    onSurface = BoldTextPrimaryDark,
    surfaceVariant = BoldSurfaceVariantDark,
    onSurfaceVariant = BoldTextSecondaryDark,
    outline = BoldBorderDark,
    outlineVariant = BoldSurfaceVariantDark
)

@Composable
fun MoneyAdventureTheme(
    darkTheme: Boolean = false, // Keep consistent bright, friendly, high-contrast palette
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

