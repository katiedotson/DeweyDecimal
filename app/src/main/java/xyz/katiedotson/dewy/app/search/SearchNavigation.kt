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
    snackbarHostState: SnackbarHostState
) {
    composable<SearchRoute> {
        SearchScreen(
            onNavigateToCameraScanScreen = onNavigateToCameraScan,
            snackbarHostState = snackbarHostState
        )
    }
}

@Serializable data object SearchRoute
