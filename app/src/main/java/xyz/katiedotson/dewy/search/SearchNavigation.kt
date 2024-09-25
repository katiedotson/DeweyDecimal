package xyz.katiedotson.dewy.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

fun NavController.navigateToSearchScreen(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = SearchRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.searchScreen(
    onNavigateToCameraScan: () -> Unit
) {
    composable<SearchRoute> {
        SearchScreen(
            onNavigateToCameraScan
        )
    }
}

@Serializable data object SearchRoute
