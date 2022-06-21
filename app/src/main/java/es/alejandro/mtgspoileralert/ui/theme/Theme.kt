package es.alejandro.mtgspoileralert.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Indigo300,
    primaryVariant = Indigo300Dark,
    secondary = DeepPurple300,
    secondaryVariant = DeepPurple300Dark
)

private val LightColorPalette = lightColors(
    primary = Indigo300,
    primaryVariant = Indigo300Dark,
    secondary = DeepPurple300,
    secondaryVariant = DeepPurple300Dark

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun MTGSpoilerAlertTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
