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
    primary = SchoolNavyPrimaryDark,
    onPrimary = SchoolNavyDark,
    primaryContainer = SchoolNavyLight,
    onPrimaryContainer = Color.White,
    secondary = SchoolTealLight,
    onSecondary = SchoolNavyDark,
    tertiary = SchoolGoldAccent,
    background = SchoolNavyDarkSurface,
    surface = SchoolNavyCardDark,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFF2E3856),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF4B5563)
)

private val LightColorScheme = lightColorScheme(
    primary = SchoolNavyPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD6E4FF),
    onPrimaryContainer = SchoolNavyDark,
    secondary = SchoolTealSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCEE5FF),
    onSecondaryContainer = Color(0xFF001D32),
    tertiary = SchoolGoldAccent,
    onTertiary = Color.White,
    tertiaryContainer = SchoolGoldLight,
    onTertiaryContainer = Color(0xFF281800),
    background = SurfaceLight,
    surface = SurfaceCardLight,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextSecondaryLight,
    outline = SurfaceBorderLight
)

@Composable
fun ParadiseSchoolTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    ParadiseSchoolTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
