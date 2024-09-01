package xyz.katiedotson.deweydecimal.scan

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraScanViewModel @Inject constructor() : ViewModel() {
    fun textDetected(text: DetectedText) {
        text.blocks
            .firstOrNull { it.startsWith(prefix = "ISBN") }
            ?.let {
                println(it)
            }
    }
}
