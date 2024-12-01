package xyz.katiedotson.dewy.app.camerascan

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import xyz.katiedotson.dewy.ui.SearchResultBottomSheetState

private const val AnimationDuration = 300

@Serializable data object CameraScanRoute

fun NavController.navigateToCameraScanScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(route = CameraScanRoute) {
    navOptions()
}

fun NavGraphBuilder.cameraScanScreen(
    onNavigateBack: () -> Unit,
    onNavigateToBookInput: (String) -> Unit,
    onNavigateToManualEntry: () -> Unit,
) {
    composable<CameraScanRoute>(
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
        val vmState by cameraScanViewModel.state.collectAsStateWithLifecycle()
        val events by cameraScanViewModel.events.collectAsStateWithLifecycle()

        LaunchedEffect(events) {
            val last = events.lastOrNull()
            last?.let {
                when (it) {
                    is Event.BookResultConfirmed -> {
                        onNavigateToBookInput(it.bookId)
                        cameraScanViewModel.eventHandled(it)
                    }
                }
            }
        }

        val viewState = mapCameraScanViewState(
            vmState = vmState,
            onBackClicked = onNavigateBack,
            onTextDetected = cameraScanViewModel::textDetected,
            onBottomSheetDismissed = cameraScanViewModel::unpause,
            onConfirmBookResult = cameraScanViewModel::onBookResultConfirmed,
            onGoToManualEntry = onNavigateToManualEntry,
        )
        CameraScanScreen(
            viewState = viewState
        )
    }
}

internal data class CameraScanViewState(
    val onBackClicked: () -> Unit,
    val isLoading: Boolean,
    val onTextDetected: (DetectedText) -> Unit,
    val showSheet: Boolean,
    val bottomSheetState: SearchResultBottomSheetState?,
    val goToManualEntry: () -> Unit,
)

@Composable
internal fun mapCameraScanViewState(
    vmState: CameraScanState,
    onBackClicked: () -> Unit,
    onTextDetected: (DetectedText) -> Unit,
    onBottomSheetDismissed: () -> Unit,
    onConfirmBookResult: () -> Unit,
    onGoToManualEntry: () -> Unit,
): CameraScanViewState {
    val bottomSheetState = mapBottomSheetState(vmState, onBottomSheetDismissed, onConfirmBookResult, onGoToManualEntry)
    return CameraScanViewState(
        onBackClicked = onBackClicked,
        isLoading = vmState is CameraScanState.Loading,
        bottomSheetState = bottomSheetState,
        onTextDetected = onTextDetected,
        showSheet = bottomSheetState != null,
        goToManualEntry = onGoToManualEntry,
    )
}

@Composable
internal fun mapBottomSheetState(
    vmState: CameraScanState,
    onBottomSheetDismissed: () -> Unit,
    onConfirmBookResult: () -> Unit,
    onGoToManualEntry: () -> Unit
): SearchResultBottomSheetState? {
    return when (vmState) {
        CameraScanState.Loading,
        CameraScanState.Scanning -> null

        is CameraScanState.MatchFound -> {
            SearchResultBottomSheetState.MatchFound(
                heading = "Result Found",
                title = vmState.match.title,
                author = vmState.match.authors.joinToString(
                    limit = 4
                ) {
                    it.fullName
                },
                onMatchConfirmed = onConfirmBookResult,
                onBottomSheetDismissed = onBottomSheetDismissed,
            )
        }
        CameraScanState.MatchNotFound -> {
            SearchResultBottomSheetState.MatchNotFound(
                heading = "No Results Found",
                onBottomSheetDismissed = onBottomSheetDismissed,
                onTryManually = onGoToManualEntry,
            )
        }
    }
}
