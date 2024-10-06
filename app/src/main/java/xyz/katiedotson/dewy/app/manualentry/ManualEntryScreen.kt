package xyz.katiedotson.dewy.app.manualentry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import xyz.katiedotson.dewy.ui.component.DewyTextField
import xyz.katiedotson.dewy.ui.theme.AppTypography

private const val MaxIsbnLength = 13

@Serializable data object ManualEntryRoute

fun NavController.navigateToManualEntry(navOptions: NavOptionsBuilder.() -> Unit = {}) = navigate(
    route = ManualEntryRoute
) {
    navOptions()
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.manualEntryScreen(
    onNavigateBack: () -> Unit,
    onNavigateToBookInput: (String) -> Unit,
) {
    composable<ManualEntryRoute> {
        val viewModel: ManualEntryViewModel = hiltViewModel()
        val vmState by viewModel.state.collectAsStateWithLifecycle()
        val events by viewModel.events.collectAsStateWithLifecycle()
        val scope = rememberCoroutineScope()
        val sheetState = rememberModalBottomSheetState()
        LaunchedEffect(events) {
            val last = events.lastOrNull()
            last?.let {
                when (it) {
                    is Event.MatchConfirmed -> {
                        onNavigateToBookInput(it.key)
                        viewModel.eventHandled(it)
                    }
                }
            }
        }

        val viewState = ManualEntryScreenState(
            onNavigateBack = onNavigateBack,
            heading = "Enter the ISBN",
            isbnError = when (vmState) {
                is ManualEntryState.FieldError -> "Value is required."
                else -> null
            },
            onSubmit = viewModel::onSubmit,
            onClearError = viewModel::reset,
            submitButtonText = "Search",
            bottomSheetState = when (val s = vmState) {
                is ManualEntryState.FieldError, is ManualEntryState.Default -> null
                is ManualEntryState.MatchNotFound -> BottomSheetState.MatchNotFound(
                    heading = "We couldn't find what you are looking for."
                )

                is ManualEntryState.MatchFound -> BottomSheetState.MatchFound(
                    heading = "Match Found",
                    title = s.match.title,
                    author = s.match.authors.joinToString { it.fullName }
                )
            },
            onBookResultConfirmed = viewModel::bookResultConfirmed,
            onBottomSheetDismissed = viewModel::reset
        )
        ManualEntryScreen(
            viewState = viewState,
            sheetState = sheetState,
            scope = scope,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManualEntryScreen(
    viewState: ManualEntryScreenState,
    sheetState: SheetState,
    scope: CoroutineScope,
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .padding(bottom = 24.dp)
        ) {
            MainContent(
                viewState = viewState
            )
        }
    }
    if (viewState.bottomSheetState != null) {
        ModalBottomSheet(
            onDismissRequest = viewState.onBottomSheetDismissed,
            sheetState = sheetState
        ) {
            BottomSheetContent(
                bottomSheetState = viewState.bottomSheetState,
                sheetState = sheetState,
                scope = scope,
                onBottomSheetDismissed = viewState.onBottomSheetDismissed,
                onMatchConfirmed = viewState.onBookResultConfirmed,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetContent(
    bottomSheetState: BottomSheetState?,
    sheetState: SheetState,
    scope: CoroutineScope,
    onBottomSheetDismissed: () -> Unit,
    onMatchConfirmed: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 12.dp)
            .padding(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (bottomSheetState is BottomSheetState.MatchNotFound) {
            Text(bottomSheetState.heading, style = AppTypography.headlineMedium)
        }
        if (bottomSheetState is BottomSheetState.MatchFound) {
            Text(text = bottomSheetState.heading, style = AppTypography.headlineMedium)
            HorizontalDivider()
            Text(text = bottomSheetState.title, style = AppTypography.bodyLarge)
            Text(text = bottomSheetState.author, style = AppTypography.bodyMedium)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(
                modifier = Modifier.weight(1f)
            )
            OutlinedButton(
                onClick = {
                    scope
                        .launch { sheetState.hide() }
                        .invokeOnCompletion {
                            if (sheetState.isVisible.not()) {
                                onBottomSheetDismissed()
                            }
                        }
                }
            ) {
                Text(text = "Try Again", style = AppTypography.labelLarge)
            }
            if (bottomSheetState is BottomSheetState.MatchFound) {
                Button(
                    onClick = onMatchConfirmed
                ) {
                    Text(text = "Confirm", style = AppTypography.labelLarge)
                }
            }
        }
    }
}

internal data class ManualEntryScreenState(
    val onNavigateBack: () -> Unit,
    val heading: String,
    val isbnError: String?,
    val onSubmit: (String) -> Unit,
    val onClearError: () -> Unit,
    val submitButtonText: String,
    val bottomSheetState: BottomSheetState?,
    val onBottomSheetDismissed: () -> Unit,
    val onBookResultConfirmed: () -> Unit,
)

internal sealed class BottomSheetState {
    data class MatchFound(val heading: String, val title: String, val author: String) : BottomSheetState()
    data class MatchNotFound(val heading: String) : BottomSheetState()
}

@Composable
private fun MainContent(
    viewState: ManualEntryScreenState,
) {
    var isbnValue by remember { mutableStateOf(value = "") }
    Heading(
        heading = viewState.heading,
        onBackClicked = viewState.onNavigateBack
    )
    DewyTextField(
        label = "ISBN",
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        value = isbnValue,
        onValueChange = {
            isbnValue = it.takeIf { it.isDigitsOnly() && it.length <= MaxIsbnLength } ?: isbnValue
            viewState.onClearError()
        },
        isError = viewState.isbnError != null,
        errorText = viewState.isbnError,
    )
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.inverseSurface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        onClick = { viewState.onSubmit(isbnValue) },
    ) {
        Text(
            text = viewState.submitButtonText,
            style = AppTypography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun Heading(
    heading: String,
    onBackClicked: () -> Unit
) {
    IconButton(
        onBackClicked,
        modifier = Modifier
            .padding(bottom = 24.dp)
            .size(48.dp)
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            imageVector = Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = null
        )
    }
    Text(
        modifier = Modifier.padding(bottom = 8.dp),
        text = heading,
        style = AppTypography.displaySmall
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@PreviewLightDark
@PreviewScreenSizes
fun ManualEntryScreenPreview() {
    ManualEntryScreen(
        viewState = ManualEntryScreenState(
            heading = "Enter the ISBN",
            onNavigateBack = {},
            submitButtonText = "Search",
            onSubmit = { _ -> },
            onClearError = {},
            isbnError = null,
            bottomSheetState = null,
            onBottomSheetDismissed = {},
            onBookResultConfirmed = {},
        ),
        sheetState = rememberModalBottomSheetState(),
        scope = rememberCoroutineScope()
    )
}
