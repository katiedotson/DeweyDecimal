package xyz.katiedotson.deweydecimal.add

import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.text.Text
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CameraScanViewModel : ViewModel() {

    val torchFlow: StateFlow<Boolean> get() = _torchFlow
    private val _torchFlow = MutableStateFlow(false)

    // todo: coupled to com.google.mlkit.vision.text.Text -- pass custom model here
    fun textDetected(text: Text) {
        val isbnCandidate = text.textBlocks.filter { it.text.startsWith("ISBN") }
        isbnCandidate.firstOrNull()?.let {
            println(it.text)
        }
    }

    fun updateTorchEnabled() {
        _torchFlow.value = !_torchFlow.value
    }
}
