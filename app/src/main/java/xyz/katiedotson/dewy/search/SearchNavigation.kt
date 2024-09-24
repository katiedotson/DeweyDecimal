package xyz.katiedotson.dewy.search

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

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
