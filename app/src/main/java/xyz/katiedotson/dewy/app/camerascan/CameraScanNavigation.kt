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

private const val AnimationDuration = 300

@Serializable data object CameraScanRoute

fun NavController.navigateToCameraScanScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(route = CameraScanRoute) {
    navOptions()
}

fun NavGraphBuilder.cameraScanScreen(
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
    val onTextDetected: (DetectedText) -> Unit,
    val onBottomSheetDismissed: () -> Unit,
    val onBookResultConfirmed: () -> Unit,
    val onGoToManualEntry: () -> Unit,
    val showSheet: Boolean,
    val bottomSheetState: BottomSheetState?,
)

internal sealed class BottomSheetState {
    data class MatchFound(val heading: String, val title: String, val author: String) : BottomSheetState()
    data class MatchNotFound(val heading: String) : BottomSheetState()
}

@Composable
internal fun mapCameraScanViewState(
    vmState: CameraScanState,
    onTextDetected: (DetectedText) -> Unit,
    onBottomSheetDismissed: () -> Unit,
    onConfirmBookResult: () -> Unit,
    onGoToManualEntry: () -> Unit,
): CameraScanViewState {
    val bottomSheetState = mapBottomSheetState(vmState)
    return CameraScanViewState(
        bottomSheetState = bottomSheetState,
        onTextDetected = onTextDetected,
        onBottomSheetDismissed = onBottomSheetDismissed,
        showSheet = bottomSheetState != null,
        onBookResultConfirmed = onConfirmBookResult,
        onGoToManualEntry = onGoToManualEntry,
    )
}

@Composable
internal fun mapBottomSheetState(vmState: CameraScanState): BottomSheetState? {
    return when (vmState) {
        CameraScanState.Loading,
        CameraScanState.Scanning -> null

        is CameraScanState.MatchFound -> {
            BottomSheetState.MatchFound(
                heading = "Result Found",
                title = vmState.match.title,
                author = vmState.match.authors.joinToString(
                    limit = 4
                ) {
                    it.fullName
                }
            )
        }
        CameraScanState.MatchNotFound -> {
            BottomSheetState.MatchNotFound(
                heading = "No Results Found"
            )
        }
    }
}
