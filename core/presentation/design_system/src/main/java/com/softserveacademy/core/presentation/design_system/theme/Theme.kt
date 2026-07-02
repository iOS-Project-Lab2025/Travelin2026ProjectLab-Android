package com.softserveacademy.core.presentation.design_system.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
private val DarkColorScheme = darkColorScheme(
    primary = Teal40,
    onPrimary = GrayLight20,
    secondary = Green50,
    onSecondary = BlueDark90,
    tertiary = Yellow50,
    background = BlueDark90,
    onBackground = GrayLight20,
    surface = BlueDark80,
    onSurface = GrayLight20,
    surfaceVariant = BlueDark80,
    onSurfaceVariant = Gray40,
    error = Red50,
    onError = White100,
    outline = Gray80,
    primaryContainer = TravelBlue40,
    secondaryContainer = TravelGray30,
)

private val LightColorScheme = lightColorScheme(
    primary = Teal40,
    onPrimary = White100,
    secondary = Green80,
    onSecondary = White100,
    tertiary = Yellow50,
    background = Gray10,
    onBackground = Gray80,
    surface = White100,
    onSurface = Gray80,
    surfaceVariant = Gray10,
    onSurfaceVariant = Gray80,
    error = Red50,
    onError = White100,
    outline = Gray80,
    primaryContainer = TravelBlue40,
    secondaryContainer = TravelGray30,

)

@Composable
fun Travelin2026ProjectLabTheme(
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
        typography = TravelinTypography,
        content = content
    )
}