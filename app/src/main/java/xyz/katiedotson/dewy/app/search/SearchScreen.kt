package xyz.katiedotson.dewy.app.search

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import xyz.katiedotson.dewy.model.UserBook
import xyz.katiedotson.dewy.ui.component.Loader
import xyz.katiedotson.dewy.ui.theme.DeweyDecimalTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun SearchScreen(
    onNavigateToCameraScanScreen: () -> Unit,
    snackbarHostState: SnackbarHostState,
    savedBookTitle: String?
) {
    val permissionsViewModel: PermissionsViewModel = hiltViewModel()
    val searchViewModel: SearchViewModel = hiltViewModel()
    val userBooks by searchViewModel.books.collectAsStateWithLifecycle()
    val isLoading by searchViewModel.loading.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
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

    Loader(isVisible = isLoading)

    ScreenContent(
        scrollState = scrollState,
        books = userBooks.toImmutableList(),
        onAddButtonClick = {
            permissionsViewModel.validateCameraPermission(
                permissionIsGranted = cameraPermissionState.status.isGranted,
                shouldShowRationale = cameraPermissionState.status.shouldShowRationale
            )
        }
    )
}

@Composable
private fun ScreenContent(
    scrollState: ScrollState,
    books: ImmutableList<UserBook>,
    onAddButtonClick: () -> Unit,
) {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Column {
                books.forEach {
                    BookCard(it)
                }
            }
            OutlinedIconButton(
                onClick = onAddButtonClick,
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
private fun BookCard(book: UserBook) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.padding(8.dp)
    ) {
        Spacer(
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = book.title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 2.dp)
        )
        Spacer(
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = book.authors.joinToString(),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = book.publisher,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
@PreviewLightDark
private fun BookCardPreview() {
    BookCard(
        book = UserBook(
            key = "",
            userId = "",
            title = "The Complete Idiot's Guide to philosophy",
            authors = listOf("Author C. Clarke"),
            publisher = "Idiot's Guide",
            languages = listOf("English"),
            subjects = listOf("Sci-fi", "Science Fiction", "Philosophy")
        )
    )
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
