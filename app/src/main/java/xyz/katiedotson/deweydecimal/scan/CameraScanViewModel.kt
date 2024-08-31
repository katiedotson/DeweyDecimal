package xyz.katiedotson.deweydecimal.scan

import androidx.lifecycle.ViewModel

class CameraScanViewModel : ViewModel() {
    fun textDetected(text: DetectedText) {
        text.blocks
            .firstOrNull { it.startsWith(prefix = "ISBN") }
            ?.let {
                println(it)
            }
    }
}
