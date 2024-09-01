package xyz.katiedotson.deweydecimal

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import xyz.katiedotson.deweydecimal.dashboard.Dashboard
import xyz.katiedotson.deweydecimal.dashboard.PermissionsViewModel
import xyz.katiedotson.deweydecimal.scan.CameraScanViewModel
import xyz.katiedotson.deweydecimal.scan.ScanNewItem
import xyz.katiedotson.deweydecimal.ui.theme.DeweyDecimalTheme

private const val AnimationDuration = 300

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainComponent() {
    val permissionsViewModel: PermissionsViewModel = hiltViewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()

    val cameraPermissionState = rememberPermissionState(
        permission = android.Manifest.permission.CAMERA,
        onPermissionResult = permissionsViewModel::onPermissionResult
    )
    val rationaleState by permissionsViewModel.permissionEventState.collectAsState()
    LaunchedEffect(rationaleState) {
        handleRationaleStateChange(
            rationaleState = rationaleState,
            permissionsViewModel = permissionsViewModel,
            snackbarHostState = snackbarHostState,
            navController = navController,
            cameraPermissionState = cameraPermissionState
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(modifier = Modifier.padding(it)) {
            NavHost(
                navController = navController,
                startDestination = NavItem.Search.route
            ) {
                composable(route = NavItem.Search.route) {
                    Dashboard(
                        onAddButtonClick = {
                            permissionsViewModel.validateCameraPermission(
                                permissionIsGranted = cameraPermissionState.status.isGranted,
                                shouldShowRationale = cameraPermissionState.status.shouldShowRationale
                            )
                        },
                    )
                }
                composable(
                    route = NavItem.Add.route,
                    enterTransition = {
                        slideIntoContainer(
                            animationSpec = tween(
                                durationMillis = AnimationDuration,
                                easing = EaseIn
                            ),
                            towards = AnimatedContentTransitionScope.SlideDirection.Up
                        )
                    }
                ) {
                    val cameraScanViewModel: CameraScanViewModel = hiltViewModel()
                    ScanNewItem(
                        onTextDetected = cameraScanViewModel::textDetected,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
suspend fun handleRationaleStateChange(
    rationaleState: PermissionsViewModel.PermissionEvent,
    permissionsViewModel: PermissionsViewModel,
    snackbarHostState: SnackbarHostState,
    navController: NavHostController,
    cameraPermissionState: PermissionState
) {
    when (rationaleState) {
        PermissionsViewModel.PermissionEvent.Denied -> {
            snackbarHostState.showSnackbar(
                message = "Permission denied."
            )
            permissionsViewModel.permissionEventHandled()
        }

        PermissionsViewModel.PermissionEvent.Granted -> {
            permissionsViewModel.permissionEventHandled()
            navController.navigate(route = "add")
        }

        PermissionsViewModel.PermissionEvent.LaunchRequest -> {
            cameraPermissionState.launchPermissionRequest()
            permissionsViewModel.permissionEventHandled()
        }

        PermissionsViewModel.PermissionEvent.ShowFinalRationale -> {
            snackbarHostState.showSnackbar(
                message = "Permission is required. Final Rationale"
            )
            permissionsViewModel.permissionEventHandled()
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

sealed class NavItem(val route: String) {
    data object Search : NavItem(route = "search")
    data object Add : NavItem(route = "add")
}

@Preview(showBackground = true)
@Composable
private fun MainApplicationPreview() {
    DeweyDecimalTheme {
        MainComponent()
    }
}
