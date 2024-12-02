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
import xyz.katiedotson.dewy.app.bookview.bookViewScreen
import xyz.katiedotson.dewy.app.bookview.navigateToBookViewScreen
import xyz.katiedotson.dewy.app.camerascan.cameraScanScreen
import xyz.katiedotson.dewy.app.camerascan.navigateToCameraScanScreen
import xyz.katiedotson.dewy.app.librarysettings.librarySettingsScreen
import xyz.katiedotson.dewy.app.librarysettings.navigateToLibrarySettings
import xyz.katiedotson.dewy.app.manualentry.manualEntryScreen
import xyz.katiedotson.dewy.app.manualentry.navigateToManualEntry
import xyz.katiedotson.dewy.app.onboarding.OnboardingRoute
import xyz.katiedotson.dewy.app.onboarding.onboardingScreen
import xyz.katiedotson.dewy.app.search.SearchRoute
import xyz.katiedotson.dewy.app.search.navigateToSearchScreen
import xyz.katiedotson.dewy.app.search.searchScreen
import xyz.katiedotson.dewy.app.searchsettings.navigateToSearchSettings
import xyz.katiedotson.dewy.app.searchsettings.searchSettingsScreen

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
                    onNavigateToBookScreen = navController::navigateToBookViewScreen,
                    onNavigateToSettings = navController::navigateToLibrarySettings,
                    onNavigateToSearchSettings = navController::navigateToSearchSettings,
                    snackbarHostState = snackbarHostState
                )
                cameraScanScreen(
                    onNavigateBack = navController::popBackStack,
                    onNavigateToBookInput = navController::navigateToBookInputScreen,
                    onNavigateToBookView = navController::navigateToBookViewScreen,
                    onNavigateToManualEntry = {
                        navController.navigateToManualEntry {
                            popUpTo(SearchRoute)
                        }
                    },
                )
                manualEntryScreen(
                    onNavigateBack = navController::popBackStack,
                    onNavigateToBookInput = navController::navigateToBookInputScreen,
                    onViewBook = navController::navigateToBookViewScreen,
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
                bookViewScreen(
                    onNavigateBack = navController::popBackStack,
                )
                librarySettingsScreen(
                    onNavigateBack = navController::popBackStack,
                )
                searchSettingsScreen(
                    onNavigateBack = navController::popBackStack,
                )
            }
        }
    }
}
