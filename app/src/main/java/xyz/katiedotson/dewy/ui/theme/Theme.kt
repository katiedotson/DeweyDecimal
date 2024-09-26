package xyz.katiedotson.dewy.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val primaryLight = Color(0xFF00696C)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFF9CF1F3)
val onPrimaryContainerLight = Color(0xFF002021)
val secondaryLight = Color(0xFF4A6364)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFCCE8E8)
val onSecondaryContainerLight = Color(0xFF041F20)
val tertiaryLight = Color(0xFF4D5F7C)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFD5E3FF)
val onTertiaryContainerLight = Color(0xFF061C36)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF410002)
val backgroundLight = Color(0xFFF4FBFA)
val onBackgroundLight = Color(0xFF161D1D)
val surfaceLight = Color(0xFFF4FBFA)
val onSurfaceLight = Color(0xFF161D1D)
val surfaceVariantLight = Color(0xFFDAE4E4)
val onSurfaceVariantLight = Color(0xFF3F4949)
val outlineLight = Color(0xFF6F7979)
val outlineVariantLight = Color(0xFFBEC8C8)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF2B3232)
val inverseOnSurfaceLight = Color(0xFFECF2F2)
val inversePrimaryLight = Color(0xFF80D4D7)
val surfaceDimLight = Color(0xFFD5DBDB)
val surfaceBrightLight = Color(0xFFF4FBFA)
val surfaceContainerLowestLight = Color(0xFFFFFFFF)
val surfaceContainerLowLight = Color(0xFFEFF5F4)
val surfaceContainerLight = Color(0xFFE9EFEF)
val surfaceContainerHighLight = Color(0xFFE3E9E9)
val surfaceContainerHighestLight = Color(0xFFDDE4E3)

val primaryDark = Color(0xFF80D4D7)
val onPrimaryDark = Color(0xFF003738)
val primaryContainerDark = Color(0xFF004F51)
val onPrimaryContainerDark = Color(0xFF9CF1F3)
val secondaryDark = Color(0xFFB0CCCC)
val onSecondaryDark = Color(0xFF1B3435)
val secondaryContainerDark = Color(0xFF324B4C)
val onSecondaryContainerDark = Color(0xFFCCE8E8)
val tertiaryDark = Color(0xFFB4C7E9)
val onTertiaryDark = Color(0xFF1E314C)
val tertiaryContainerDark = Color(0xFF354763)
val onTertiaryContainerDark = Color(0xFFD5E3FF)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF0E1415)
val onBackgroundDark = Color(0xFFDDE4E3)
val surfaceDark = Color(0xFF0E1415)
val onSurfaceDark = Color(0xFFDDE4E3)
val surfaceVariantDark = Color(0xFF3F4949)
val onSurfaceVariantDark = Color(0xFFBEC8C8)
val outlineDark = Color(0xFF899393)
val outlineVariantDark = Color(0xFF3F4949)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFDDE4E3)
val inverseOnSurfaceDark = Color(0xFF2B3232)
val inversePrimaryDark = Color(0xFF00696C)
val surfaceDimDark = Color(0xFF0E1415)
val surfaceBrightDark = Color(0xFF343A3A)
val surfaceContainerLowestDark = Color(0xFF090F0F)
val surfaceContainerLowDark = Color(0xFF161D1D)
val surfaceContainerDark = Color(0xFF1A2121)
val surfaceContainerHighDark = Color(0xFF252B2B)
val surfaceContainerHighestDark = Color(0xFF303636)

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

@Composable
fun DeweyDecimalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
