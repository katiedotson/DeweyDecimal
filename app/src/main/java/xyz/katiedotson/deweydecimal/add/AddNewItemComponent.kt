package xyz.katiedotson.deweydecimal.add

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.mlkit.vision.text.Text
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AddNewItem(
    onTextDetected: (Text) -> Unit,
    onTorchButtonClicked: () -> Unit,
    torchEnabledFlow: StateFlow<Boolean>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val lifecycleCameraController = buildCameraController(onTextDetected)
        CameraView(
            cameraController = lifecycleCameraController,
            torchEnabledFlow = torchEnabledFlow,
            onTorchButtonClicked = onTorchButtonClicked,
        )
    }
}

@Composable
fun CameraView(
    cameraController: LifecycleCameraController,
    torchEnabledFlow: StateFlow<Boolean>,
    onTorchButtonClicked: () -> Unit
) {
    val torchEnabled by torchEnabledFlow.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { context ->
            PreviewView(context).apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                scaleType = PreviewView.ScaleType.FILL_START
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                this.controller = cameraController
            }
        })
        Spacer(modifier = Modifier.padding(16.dp))

        Button(onClick = onTorchButtonClicked) {
            val text = if (torchEnabled) "Turn Off Torch" else "Turn On Torch"
            Text(text = text)
        }
    }
}

@Composable
@Preview
fun CameraViewPreview() {
    CameraView(
        cameraController = LifecycleCameraController(LocalContext.current),
        torchEnabledFlow = MutableStateFlow(false),
        onTorchButtonClicked = {}
    )
}
