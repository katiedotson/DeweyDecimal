package xyz.katiedotson.dewy.app.librarysettings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

fun NavController.navigateToLibrarySettings(navOptions: NavOptionsBuilder.() -> Unit = {}) = navigate(
    route = LibrarySettingsRoute
) {
    navOptions()
}

@Serializable data object LibrarySettingsRoute

fun NavGraphBuilder.librarySettingsScreen(
    onNavigateBack: () -> Unit,
) {
    composable<LibrarySettingsRoute> {
        Surface {
            Column {
                Text("Library Settings")
                Button(onNavigateBack) {
                    Text("Navigate Back")
                }
            }
        }
    }
}
