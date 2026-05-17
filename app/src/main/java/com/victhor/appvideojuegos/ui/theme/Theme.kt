package com.victhor.appvideojuegos.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = AcentoNeonBlue,
    secondary = AcentoNeonMagenta,
    tertiary = AcentoNeonCyan,
    background = FondoPantallaNegro,
    surface = FondoContenedoresOscuro,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = TextoPrincipalBlanco,
    onSurface = TextoPrincipalBlanco,
    surfaceVariant = FondoTarjetasGris,
    onSurfaceVariant = TextoSecundarioGris,
    error = AlertaNeonRojo
)

@Composable
fun AppVideojuegosTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = FondoPantallaNegro.toArgb()
            window.navigationBarColor = FondoPantallaNegro.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
