package xyz.katiedotson.dewy.app.camerascan

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import xyz.katiedotson.dewy.ui.SearchResultBottomSheetContent
import xyz.katiedotson.dewy.ui.component.Loader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CameraScanScreen(
    viewState: CameraScanViewState,
) {
    val cameraController = buildCameraController(viewState.onTextDetected)
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    Loader(isVisible = viewState.isLoading)
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
            Button(
                onClick = viewState.goToManualEntry,
                modifier = Modifier.align(Alignment.TopEnd).padding(24.dp)
            ) {
                Text(text = "Search Manually")
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
            if (viewState.bottomSheetState != null) {
                ModalBottomSheet(
                    onDismissRequest = viewState.bottomSheetState.onBottomSheetDismissed,
                    sheetState = sheetState
                ) {
                    SearchResultBottomSheetContent(
                        bottomSheetState = viewState.bottomSheetState,
                        sheetState = sheetState,
                        scope = scope,
                    )
                }
            }
        }
    }
}
