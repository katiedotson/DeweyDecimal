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
import xyz.katiedotson.dewy.app.bookinput.bookInputScreen
import xyz.katiedotson.dewy.app.bookinput.navigateToBookInputScreen
import xyz.katiedotson.dewy.app.camerascan.cameraScanScreen
import xyz.katiedotson.dewy.app.camerascan.navigateToCameraScanScreen
import xyz.katiedotson.dewy.app.manualentry.manualEntryScreen
import xyz.katiedotson.dewy.app.manualentry.navigateToManualEntry
import xyz.katiedotson.dewy.app.onboarding.OnboardingRoute
import xyz.katiedotson.dewy.app.onboarding.onboardingScreen
import xyz.katiedotson.dewy.app.search.SearchRoute
import xyz.katiedotson.dewy.app.search.navigateToSearchScreen
import xyz.katiedotson.dewy.app.search.searchScreen

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
                    onNavigateToManualEntry = navController::navigateToManualEntry,
                    snackbarHostState = snackbarHostState
                )
                cameraScanScreen(
                    onNavigateBack = navController::popBackStack,
                    onNavigateToBookInput = navController::navigateToBookInputScreen,
                    onNavigateToManualEntry = {
                        navController.navigateToManualEntry {
                            popUpTo(SearchRoute)
                        }
                    },
                )
                manualEntryScreen(
                    onNavigateBack = navController::popBackStack,
                    onNavigateToBookInput = navController::navigateToBookInputScreen,
                )
                bookInputScreen(
                    onNavigateBack = navController::popBackStack,
                    onNavigateBackToDashboard = { savedBookTitle ->
                        navController
                            .popBackStack<SearchRoute>(inclusive = false)
                            .also {
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("SavedBookTitle", savedBookTitle)
                            }
                    },
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}
