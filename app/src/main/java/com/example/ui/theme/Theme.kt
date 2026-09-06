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
  primary = MandiDarkPrimary,
  onPrimary = Color(0xFF00390B),
  primaryContainer = MandiDarkPrimaryContainer,
  onPrimaryContainer = Color(0xFFA5F0AA),
  secondary = Color(0xFFFBBF24),
  onSecondary = Color(0xFF451A03),
  background = MandiDarkBackground,
  surface = MandiDarkSurface,
  surfaceVariant = MandiDarkSurfaceVariant,
  outline = Color(0xFF4B5563)
)

private val LightColorScheme = lightColorScheme(
  primary = MandiGreenPrimary,
  onPrimary = MandiGreenOnPrimary,
  primaryContainer = MandiGreenContainer,
  onPrimaryContainer = MandiGreenOnContainer,
  secondary = MandiGoldSecondary,
  onSecondary = Color.White,
  secondaryContainer = MandiGoldContainer,
  onSecondaryContainer = MandiGoldOnContainer,
  tertiary = MandiSoilTertiary,
  background = MandiBackground,
  surface = MandiSurface,
  surfaceVariant = MandiSurfaceVariant,
  outline = MandiOutline
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our brand colors for cohesive mandi aesthetic
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

