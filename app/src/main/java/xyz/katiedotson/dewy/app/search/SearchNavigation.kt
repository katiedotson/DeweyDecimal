package xyz.katiedotson.dewy.app.search

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

fun NavController.navigateToSearchScreen() {
    navigate(route = SearchRoute) {
        this@navigateToSearchScreen.currentBackStackEntry?.destination?.route?.let {
            popUpTo(it) {
                inclusive = true
            }
        }
    }
}

fun NavGraphBuilder.searchScreen(
    onNavigateToCameraScan: () -> Unit,
    onNavigateToManualEntry: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    composable<SearchRoute> { backStackEntry ->
        SearchScreen(
            onNavigateToCameraScanScreen = onNavigateToCameraScan,
            onNavigateToManualEntryScreen = onNavigateToManualEntry,
            snackbarHostState = snackbarHostState,
            savedBookTitle = backStackEntry.savedStateHandle.get<String>("SavedBookTitle")
        )
    }
}

@Serializable data object SearchRoute
