package xyz.katiedotson.deweydecimal

import android.content.Intent
import android.net.Uri
import android.provider.Settings
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import xyz.katiedotson.deweydecimal.bookinput.bookInputScreen
import xyz.katiedotson.deweydecimal.bookinput.navigateToBookInputScreen
import xyz.katiedotson.deweydecimal.camerascan.cameraScanScreen
import xyz.katiedotson.deweydecimal.camerascan.navigateToCameraScanScreen
import xyz.katiedotson.deweydecimal.search.SearchRoute
import xyz.katiedotson.deweydecimal.search.searchScreen
import xyz.katiedotson.deweydecimal.ui.theme.DeweyDecimalTheme

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
    val context = LocalContext.current

    LaunchedEffect(rationaleState) {
        handleRationaleStateChange(
            snackbarHostState = snackbarHostState,
            rationaleState = rationaleState,
            permissionsViewModel = permissionsViewModel,
            cameraPermissionState = cameraPermissionState,
            navigateToScanScreen = navController::navigateToCameraScanScreen,
            navigateToSettings = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.setData(uri)
                startActivity(context, intent, null)
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(modifier = Modifier.padding(it)) {
            NavHost(
                navController = navController,
                startDestination = SearchRoute
            ) {
                searchScreen(
                    onNavigateToCameraScan = navController::navigateToCameraScanScreen
                )
                cameraScanScreen(
                    onNavigateToBookInput = navController::navigateToBookInputScreen
                )
                bookInputScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainApplicationPreview() {
    DeweyDecimalTheme {
        MainComponent()
    }
}
