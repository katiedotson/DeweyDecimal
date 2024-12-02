package xyz.katiedotson.dewy.app.searchsettings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

fun NavController.navigateToSearchSettings(navOptions: NavOptionsBuilder.() -> Unit = {}) = navigate(
    route = SearchSettingsRoute
) {
    navOptions()
}

@Serializable data object SearchSettingsRoute

fun NavGraphBuilder.searchSettingsScreen(
    onNavigateBack: () -> Unit,
) {
    composable<SearchSettingsRoute> {
        Surface {
            Column {
                Text("Search Settings")
                Button(onNavigateBack) {
                    Text("Navigate Back")
                }
            }
        }
    }
}
