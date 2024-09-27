package xyz.katiedotson.dewy.search

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
internal suspend fun handleRationaleStateChange(
    snackbarHostState: SnackbarHostState,
    rationaleState: PermissionsViewModel.PermissionEvent,
    permissionsViewModel: PermissionsViewModel,
    cameraPermissionState: PermissionState,
    navigateToScanScreen: () -> Unit,
    navigateToSettings: () -> Unit,
) {
    when (rationaleState) {
        PermissionsViewModel.PermissionEvent.Denied -> {
            snackbarHostState.showSnackbar(
                message = "Please allow camera permissions to add a book to your collection.",
                withDismissAction = true
            )
            permissionsViewModel.permissionEventHandled()
        }

        PermissionsViewModel.PermissionEvent.Granted -> {
            permissionsViewModel.permissionEventHandled()
            navigateToScanScreen()
        }

        PermissionsViewModel.PermissionEvent.LaunchRequest -> {
            cameraPermissionState.launchPermissionRequest()
            permissionsViewModel.permissionEventHandled()
        }

        PermissionsViewModel.PermissionEvent.ShowFinalRationale -> {
            val result = snackbarHostState.showSnackbar(
                message = "Please allow camera permissions to add a book to your collection.",
                actionLabel = "Go To Settings",
                duration = SnackbarDuration.Long
            )
            if (result == SnackbarResult.ActionPerformed) {
                navigateToSettings()
            }
        }

        PermissionsViewModel.PermissionEvent.ShowInitialRationale -> {
            cameraPermissionState.launchPermissionRequest()
            permissionsViewModel.permissionEventHandled()
        }

        PermissionsViewModel.PermissionEvent.None -> {
            // nothing
        }
    }
}
