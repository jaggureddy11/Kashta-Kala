package com.example.kashtakala.ui.theme

import android.app.Activity
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
    primary = Amber,
    onPrimary = WoodDark,
    secondary = WoodLight,
    onSecondary = WoodDark,
    background = WoodDark,
    onBackground = Cream,
    surface = Color(0xFF5A3C1A),
    onSurface = Cream,
    tertiary = WoodMedium
)

private val LightColorScheme = lightColorScheme(
    primary = WoodMedium,
    onPrimary = Color.White,
    secondary = WoodDark,
    onSecondary = Color.White,
    background = Cream,
    onBackground = WoodDark,
    surface = Color.White,
    onSurface = WoodDark,
    tertiary = Amber,
    surfaceVariant = Color(0xFFF5EBE0),
    onSurfaceVariant = WoodDark,
    outline = WoodLight
)

@Composable
fun KashtaKalaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+ (disabled by default for cohesive branding)
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