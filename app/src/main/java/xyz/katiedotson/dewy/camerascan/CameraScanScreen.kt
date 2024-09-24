package xyz.katiedotson.dewy.camerascan

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xyz.katiedotson.dewy.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CameraScanScreen(
    viewState: CameraScanViewState,
) {
    val cameraController = buildCameraController(viewState.onTextDetected)
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        scaleType = PreviewView.ScaleType.FILL_START
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        controller = cameraController
                    }
                }
            )
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
                        onMatchConfirmed = viewState.onBookResultConfirmed
                    )
                }
            }
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
    onMatchConfirmed: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 12.dp)
            .padding(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (bottomSheetState is BottomSheetState.MatchNotFound) {
            Text(bottomSheetState.heading, style = Typography.headlineMedium)
        }
        if (bottomSheetState is BottomSheetState.MatchFound) {
            Text(text = bottomSheetState.heading, style = Typography.headlineMedium)
            HorizontalDivider()
            Text(text = bottomSheetState.title, style = Typography.bodyLarge)
            Text(text = bottomSheetState.author, style = Typography.bodyMedium)
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
                Text(text = "Try Again")
            }
            Button(
                onClick = onMatchConfirmed
            ) {
                Text(text = "Confirm")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
private fun BottomSheetContentMatchFoundPreview() {
    BottomSheetContent(
        bottomSheetState = BottomSheetState.MatchFound(
            heading = "Result Found",
            title = "Burning Chrome",
            author = "William Gibson"
        ),
        onBottomSheetDismissed = {},
        onMatchConfirmed = {},
        scope = rememberCoroutineScope(),
        sheetState = rememberModalBottomSheetState()
    )
}
