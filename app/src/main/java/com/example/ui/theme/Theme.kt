package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SchoolBluePrimaryDark,
    onPrimary = Color(0xFF002F6C),
    primaryContainer = SchoolBlueContainerDark,
    onPrimaryContainer = Color(0xFFD6E2FB),
    secondary = SchoolGold,
    onSecondary = Color(0xFF452B00),
    secondaryContainer = Color(0xFF5E3C00),
    onSecondaryContainer = SchoolGoldContainer,
    tertiary = Color(0xFFFFB4AB),
    error = Color(0xFFFFB4AB),
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    onBackground = Color(0xFFF1F5F9),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1)
)

private val LightColorScheme = lightColorScheme(
    primary = SchoolBluePrimary,
    onPrimary = Color.White,
    primaryContainer = SchoolBlueContainer,
    onPrimaryContainer = SchoolBlueDark,
    secondary = SchoolAmber,
    onSecondary = Color.White,
    secondaryContainer = SchoolGoldContainer,
    onSecondaryContainer = Color(0xFF78350F),
    tertiary = SchoolCrimson,
    onTertiary = Color.White,
    tertiaryContainer = SchoolCrimsonContainer,
    onTertiaryContainer = Color(0xFF5F0008),
    error = SchoolCrimson,
    onError = Color.White,
    background = Color(0xFFF8FAFC),
    surface = Color.White,
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outline = CardBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep school brand identity by default
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
