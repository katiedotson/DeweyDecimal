package xyz.katiedotson.dewy.app.camerascan

import android.content.Context
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

@Composable
internal fun buildCameraController(
    onTextDetected: (DetectedText) -> Unit,
    context: Context = LocalContext.current,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
): LifecycleCameraController {
    val cameraController = LifecycleCameraController(context)
    cameraController.bindToLifecycle(lifecycleOwner)

    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val analyzer = MlKitAnalyzer(
        listOf(recognizer),
        ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED,
        ContextCompat.getMainExecutor(context)
    ) { result: MlKitAnalyzer.Result? ->
        result?.getValue(recognizer)?.let {
            onTextDetected(it.toDetectedText())
        }
    }

    cameraController.setImageAnalysisAnalyzer(
        Dispatchers.IO.asExecutor(),
        analyzer
    )

    return cameraController
}
