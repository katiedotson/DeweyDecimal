package xyz.katiedotson.deweydecimal.camerascan

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import xyz.katiedotson.deweydecimal.NavGraphItem

private const val AnimationDuration = 300

fun NavController.navigateToCameraScanScreen() = navigate(route = NavGraphItem.Add.route)

fun NavGraphBuilder.cameraScanScreen() {
    composable(
        route = NavGraphItem.Add.route,
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
        val vmState by cameraScanViewModel.state.collectAsState()
        val viewState = mapCameraScanViewState(
            vmState = vmState,
            onTextDetected = cameraScanViewModel::textDetected,
            onBottomSheetDismissed = cameraScanViewModel::unpause
        )
        CameraScanScreen(
            viewState = viewState
        )
    }
}

internal data class CameraScanViewState(
    val onTextDetected: (DetectedText) -> Unit,
    val onBottomSheetDismissed: () -> Unit,
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
    onBottomSheetDismissed: () -> Unit
): CameraScanViewState {
    val bottomSheetState = mapBottomSheetState(vmState)
    return CameraScanViewState(
        bottomSheetState = bottomSheetState,
        onTextDetected = onTextDetected,
        onBottomSheetDismissed = onBottomSheetDismissed,
        showSheet = bottomSheetState != null
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
                )
            )
        }
        CameraScanState.MatchNotFound -> {
            BottomSheetState.MatchNotFound(
                heading = "No Results Found"
            )
        }
    }
}
