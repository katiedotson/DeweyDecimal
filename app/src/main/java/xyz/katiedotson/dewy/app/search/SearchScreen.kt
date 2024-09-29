package xyz.katiedotson.dewy.app.search

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import xyz.katiedotson.dewy.ui.theme.DeweyDecimalTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun SearchScreen(
    onNavigateToCameraScanScreen: () -> Unit,
    snackbarHostState: SnackbarHostState,
    savedBookTitle: String?
) {
    val permissionsViewModel: PermissionsViewModel = hiltViewModel()
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
            navigateToScanScreen = onNavigateToCameraScanScreen,
            navigateToSettings = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.setData(uri)
                startActivity(context, intent, null)
            }
        )
    }

    LaunchedEffect(savedBookTitle) {
        if (savedBookTitle != null) {
            snackbarHostState.showSnackbar(
                message = "$savedBookTitle was successfully saved to your library.",
                withDismissAction = true
            )
        }
    }
    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = "home screen / todo")
            OutlinedIconButton(
                onClick = {
                    permissionsViewModel.validateCameraPermission(
                        permissionIsGranted = cameraPermissionState.status.isGranted,
                        shouldShowRationale = cameraPermissionState.status.shouldShowRationale
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp),
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new")
            }
        }
    }
}

@Composable
@PreviewLightDark
@PreviewScreenSizes
private fun SearchScreenPreviewDark() {
    DeweyDecimalTheme {
        SearchScreen(
            onNavigateToCameraScanScreen = {},
            snackbarHostState = SnackbarHostState(),
            savedBookTitle = null
        )
    }
}

