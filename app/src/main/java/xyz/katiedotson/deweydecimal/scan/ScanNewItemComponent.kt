package xyz.katiedotson.deweydecimal.scan

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ScanNewItem(
    onTextDetected: (DetectedText) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val cameraController = buildCameraController(onTextDetected)
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
        }
    }
}

@Composable
@Preview
fun CameraViewPreview() {
    ScanNewItem(
        onTextDetected = {}
    )
}
