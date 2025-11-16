package com.mouse.mouse

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mouse.mouse.navigation.*
import com.mouse.mouse.presentation.home.HomeScreen
import com.mouse.mouse.presentation.history.HistoryScreen
import com.mouse.mouse.presentation.onboarding.OnboardingScreen
import com.mouse.mouse.presentation.settings.SettingsScreen
import com.mouse.mouse.ui.theme.MorseTheme

@Composable
fun App() {
    MorseTheme {
        AppNavigation()
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    // TODO: Check if onboarding is completed from preferences
    // For now, always start with onboarding
    var hasCompletedOnboarding by remember { mutableStateOf(false) }
    
    val startDestination = if (hasCompletedOnboarding) HomeRoute else OnboardingRoute

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<OnboardingRoute> {
            OnboardingScreen(
                onOnboardingComplete = {
                    hasCompletedOnboarding = true
                    navController.navigate(HomeRoute) {
                        popUpTo<OnboardingRoute> { inclusive = true }
                    }
                }
            )
        }

        composable<HomeRoute> {
            HomeScreen(
                onNavigateToHistory = {
                    navController.navigate(HistoryRoute)
                },
                onNavigateToSettings = {
                    navController.navigate(SettingsRoute)
                }
            )
        }

        composable<HistoryRoute> {
            HistoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<SettingsRoute> {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
