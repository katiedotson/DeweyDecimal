package xyz.katiedotson.dewy.app.search

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import xyz.katiedotson.dewy.R
import xyz.katiedotson.dewy.model.UserBook
import xyz.katiedotson.dewy.ui.component.Loader
import xyz.katiedotson.dewy.ui.theme.AppTypography
import xyz.katiedotson.dewy.ui.theme.DeweyDecimalTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun SearchScreen(
    onNavigateToCameraScanScreen: () -> Unit,
    onNavigateToManualEntryScreen: () -> Unit,
    onNavigateToBookScreen: (UserBook) -> Unit,
    snackbarHostState: SnackbarHostState,
    savedBookTitle: String?,
    onSavedBookSnackDismissed: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSearchSettings: () -> Unit,
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
            searchViewModel.loadBooks()
            val result = snackbarHostState.showSnackbar(
                message = "$savedBookTitle was successfully saved to your library.",
                withDismissAction = true
            )
            result.also {
                onSavedBookSnackDismissed()
            }
        }
    }

    Loader(isVisible = isLoading)

    ScreenContent(
        scrollState = scrollState,
        books = userBooks.toImmutableList(),
        onAddWithCameraPressed = {
            permissionsViewModel.validateCameraPermission(
                permissionIsGranted = cameraPermissionState.status.isGranted,
                shouldShowRationale = cameraPermissionState.status.shouldShowRationale
            )
        },
        onAddManuallyPressed = onNavigateToManualEntryScreen,
        onBookClicked = onNavigateToBookScreen,
        onNavigateToSettings = onNavigateToSettings,
        onNavigateToSearchSettings = onNavigateToSearchSettings,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    scrollState: ScrollState,
    books: ImmutableList<UserBook>,
    onAddWithCameraPressed: () -> Unit,
    onAddManuallyPressed: () -> Unit,
    onBookClicked: (UserBook) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSearchSettings: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Your Library")
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min).padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                IconButton(
                    onClick = onAddManuallyPressed,
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new manually")
                }
                IconButton(
                    onClick = onAddWithCameraPressed,
                    modifier = Modifier
                ) {
                    Icon(
                        painter = painterResource(R.drawable.camera_add),
                        contentDescription = "Add new with camera"
                    )
                }
                IconButton(
                    onClick = onNavigateToSettings,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings"
                    )
                }
                IconButton(
                    onClick = onNavigateToSearchSettings,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                    )
                }
            }
        }
    ) {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Column(modifier = Modifier.verticalScroll(scrollState)) {
                    books.forEach {
                        BookCard(it, onBookClicked)
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
@Suppress("LongMethod")
private fun BookCard(book: UserBook, onBookClicked: (UserBook) -> Unit) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Column {
            Spacer(
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Row(
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .weight(weight = 0.9f)
                )
                IconButton(onClick = { onBookClicked(book) }, modifier = Modifier.padding(horizontal = 12.dp)) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "View")
                }
            }
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
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                book.subjects.forEach {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .background(
                                color = MaterialTheme.colorScheme.inversePrimary,
                                shape = FilterChipDefaults.shape
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        text = it,
                        style = AppTypography.labelMedium
                    )
                }
            }
            Spacer(
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun BookCardPreview() {
    DeweyDecimalTheme {
        BookCard(
            book = UserBook(
                key = "",
                userId = "",
                title = "The Complete Idiot's Guide to philosophy",
                authors = listOf("Author C. Clarke"),
                publisher = "Idiot's Guide",
                languages = listOf("English"),
                subjects = listOf("Sci-fi", "Science Fiction", "Philosophy")
            ),
            onBookClicked = {},
        )
    }
}

@Composable
@PreviewLightDark
@PreviewScreenSizes
@Suppress("LongMethod")
private fun SearchScreenPreview() {
    DeweyDecimalTheme {
        Surface {
            ScreenContent(
                scrollState = rememberScrollState(),
                books = persistentListOf(
                    UserBook(
                        key = "",
                        userId = "",
                        title = "The Complete Idiot's Guide to philosophy",
                        authors = listOf("Author C. Clarke"),
                        publisher = "Idiot's Guide",
                        languages = listOf("English"),
                        subjects = listOf("Sci-fi", "Science Fiction", "Philosophy")
                    ),
                    UserBook(
                        key = "",
                        userId = "",
                        title = "1984",
                        authors = listOf("George Orwell"),
                        publisher = "Secker & Warburg",
                        languages = listOf("English"),
                        subjects = listOf("Dystopian", "Political Fiction", "Science Fiction")
                    ),
                    UserBook(
                        key = "",
                        userId = "",
                        title = "To Kill a Mockingbird",
                        authors = listOf("Harper Lee"),
                        publisher = "J.B. Lippincott & Co.",
                        languages = listOf("English"),
                        subjects = listOf("Fiction", "Social Issues", "Southern Gothic")
                    ),
                    UserBook(
                        key = "",
                        userId = "",
                        title = "The Great Gatsby",
                        authors = listOf("F. Scott Fitzgerald"),
                        publisher = "Charles Scribner's Sons",
                        languages = listOf("English"),
                        subjects = listOf("Classic", "American Literature", "Tragedy")
                    ),
                    UserBook(
                        key = "",
                        userId = "",
                        title = "A Brief History of Time",
                        authors = listOf("Stephen Hawking"),
                        publisher = "Bantam Books",
                        languages = listOf("English"),
                        subjects = listOf("Science", "Physics", "Cosmology")
                    ),
                    UserBook(
                        key = "",
                        userId = "",
                        title = "Pride and Prejudice",
                        authors = listOf("Jane Austen"),
                        publisher = "T. Egerton",
                        languages = listOf("English"),
                        subjects = listOf("Romance", "Classic", "Social Commentary")
                    )
                ),
                onAddWithCameraPressed = {},
                onAddManuallyPressed = {},
                onBookClicked = {},
                onNavigateToSearchSettings = {},
                onNavigateToSettings = {},
            )
        }
    }
}
