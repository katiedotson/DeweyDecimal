package xyz.katiedotson.dewy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import xyz.katiedotson.dewy.bookinput.bookInputScreen
import xyz.katiedotson.dewy.bookinput.navigateToBookInputScreen
import xyz.katiedotson.dewy.camerascan.cameraScanScreen
import xyz.katiedotson.dewy.camerascan.navigateToCameraScanScreen
import xyz.katiedotson.dewy.onboarding.OnboardingRoute
import xyz.katiedotson.dewy.onboarding.onboardingScreen
import xyz.katiedotson.dewy.search.navigateToSearchScreen
import xyz.katiedotson.dewy.search.searchScreen

@Composable
fun MainComponent() {
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(modifier = Modifier.padding(it)) {
            NavHost(
                navController = navController,
                startDestination = OnboardingRoute
            ) {
                onboardingScreen(
                    onNavigateToSearchScreen = navController::navigateToSearchScreen
                )
                searchScreen(
                    onNavigateToCameraScan = navController::navigateToCameraScanScreen,
                    snackbarHostState = snackbarHostState
                )
                cameraScanScreen(
                    onNavigateToBookInput = navController::navigateToBookInputScreen
                )
                bookInputScreen(
                    onNavigateBack = navController::popBackStack
                )
            }
        }
    }
}
