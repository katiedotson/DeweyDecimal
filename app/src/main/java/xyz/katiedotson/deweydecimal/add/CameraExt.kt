package xyz.katiedotson.deweydecimal.add

import android.content.Context
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@Composable
fun buildCameraController(
    onTextDetected: (Text) -> Unit,
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
        val text = result?.getValue(recognizer)
        text?.let {
            onTextDetected(it)
        }
    }

    cameraController.setImageAnalysisAnalyzer(
        ContextCompat.getMainExecutor(context),
        analyzer
    )

    return cameraController
}
