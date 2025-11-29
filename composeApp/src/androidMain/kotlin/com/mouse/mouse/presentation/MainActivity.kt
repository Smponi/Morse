package com.mouse.mouse.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mouse.mouse.presentation.screens.HistoryScreen
import com.mouse.mouse.presentation.screens.TransmitterScreen
import com.mouse.mouse.presentation.viewmodel.MorseSuiteViewModel
import com.mouse.mouse.ui.theme.AppDimensions

/**
 * Haupt-Activity der App
 * 
 * Verantwortlich für:
 * - App-Initialisierung
 * - Theme Setup
 * - Navigation zwischen Screens
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialisiere Platform Context
        com.mouse.mouse.platform.initializeHardware(this)
        
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFF80D8FF),        // Neon Blue
                    secondary = Color(0xFFFFD180),      // Warm Signal
                    tertiary = Color(0xFF00BFA5),       // Teal Accent
                    background = Color(0xFF101418),     // Deep Dark
                    surface = Color(0xFF1C2229),        // Slightly lighter dark
                    onSurface = Color(0xFFE0E0E0),
                    surfaceVariant = Color(0xFF2B343D)
                )
            ) {
                MorseApp()
            }
        }
    }
}

/**
 * Haupt-Composable der App
 * Verwaltet Navigation zwischen den verschiedenen Screens
 */
@Composable
fun MorseApp(viewModel: MorseSuiteViewModel = viewModel()) {
    var currentScreen by remember { mutableStateOf(Screen.Transmitter) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                tonalElevation = AppDimensions.Spacing.xSmall
            ) {
                Screen.values().forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentScreen == screen,
                        onClick = { currentScreen = screen },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            selectedIconColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            androidx.compose.animation.Crossfade(
                targetState = currentScreen,
                label = "ScreenNav"
            ) { screen ->
                when (screen) {
                    Screen.Transmitter -> TransmitterScreen(viewModel)
                    Screen.History -> HistoryScreen(viewModel, showFavoritesOnly = false)
                    Screen.Favorites -> HistoryScreen(viewModel, showFavoritesOnly = true)
                }
            }
        }
    }
}

/**
 * Navigation Tabs
 */
enum class Screen(val label: String, val icon: ImageVector) {
    Transmitter("Transmit", Icons.Rounded.WifiTethering),
    History("History", Icons.Rounded.History),
    Favorites("Favorites", Icons.Rounded.Star)
}
