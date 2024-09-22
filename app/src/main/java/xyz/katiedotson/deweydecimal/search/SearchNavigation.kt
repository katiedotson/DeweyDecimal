package xyz.katiedotson.deweydecimal.search

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import xyz.katiedotson.deweydecimal.NavGraphItem

fun NavGraphBuilder.searchScreen(
    onNavigateToCameraScan: () -> Unit
) {
    composable(route = NavGraphItem.Search.route) {
        SearchScreen(
            onNavigateToCameraScan
        )
    }
}
