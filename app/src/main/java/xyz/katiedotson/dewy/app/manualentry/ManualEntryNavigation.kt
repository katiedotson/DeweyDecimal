package xyz.katiedotson.dewy.app.manualentry

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import xyz.katiedotson.dewy.ui.SearchResultBottomSheetState

fun NavController.navigateToManualEntry(navOptions: NavOptionsBuilder.() -> Unit = {}) = navigate(
    route = ManualEntryRoute
) {
    navOptions()
}

@Serializable data object ManualEntryRoute

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.manualEntryScreen(
    onNavigateBack: () -> Unit,
    onNavigateToBookInput: (String) -> Unit,
) {
    composable<ManualEntryRoute> {
        val viewModel: ManualEntryViewModel = hiltViewModel()
        val vmState by viewModel.state.collectAsStateWithLifecycle()
        val events by viewModel.events.collectAsStateWithLifecycle()
        val loading by viewModel.loading.collectAsStateWithLifecycle()
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
            loading = loading,
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
                is ManualEntryState.MatchNotFound -> SearchResultBottomSheetState.MatchNotFound(
                    heading = "We couldn't find what you are looking for.",
                    onBottomSheetDismissed = viewModel::reset,
                    onTryManually = null
                )

                is ManualEntryState.MatchFound -> SearchResultBottomSheetState.MatchFound(
                    heading = "Match Found",
                    title = s.match.title,
                    author = s.match.authors.joinToString { it.fullName },
                    onMatchConfirmed = viewModel::bookResultConfirmed,
                    onBottomSheetDismissed = viewModel::reset,
                )
            },
        )

        ManualEntryScreen(
            viewState = viewState,
            sheetState = sheetState,
            scope = scope,
        )
    }
}

internal data class ManualEntryScreenState(
    val loading: Boolean,
    val onNavigateBack: () -> Unit,
    val heading: String,
    val isbnError: String?,
    val onSubmit: (String) -> Unit,
    val onClearError: () -> Unit,
    val submitButtonText: String,
    val bottomSheetState: SearchResultBottomSheetState?,
)
