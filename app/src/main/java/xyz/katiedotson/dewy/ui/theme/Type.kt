package xyz.katiedotson.dewy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import xyz.katiedotson.dewy.R

private val kayPhoDuFontFamily = FontFamily(
    Font(R.font.ka_pho_du_regular, weight = FontWeight.Normal),
    Font(R.font.kay_pho_du_medium, weight = FontWeight.Medium),
    Font(R.font.kay_pho_du_bold, weight = FontWeight.Bold),
    Font(R.font.kay_pho_du_semibold, weight = FontWeight.SemiBold)
)

private val notoSans = FontFamily(
    Font(R.font.noto_sans_regular),
    Font(R.font.noto_sans_italic)
)

// Set of Material typography styles to start with

private val defaultTypography = Typography()

// Default Material 3 Typography implementation
val AppTypography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = kayPhoDuFontFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = kayPhoDuFontFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = kayPhoDuFontFamily),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = kayPhoDuFontFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = kayPhoDuFontFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = kayPhoDuFontFamily),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = kayPhoDuFontFamily),
    titleMedium = defaultTypography.headlineMedium.copy(fontFamily = kayPhoDuFontFamily),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = kayPhoDuFontFamily),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = notoSans),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = notoSans),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = notoSans),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = notoSans),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = notoSans),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = notoSans),
)

