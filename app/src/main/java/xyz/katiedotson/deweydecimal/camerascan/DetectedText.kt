package xyz.katiedotson.deweydecimal.camerascan

import com.google.mlkit.vision.text.Text

internal data class DetectedText(val blocks: List<String>)

internal fun Text.toDetectedText(): DetectedText {
    return DetectedText(
        blocks = this.textBlocks.map { it.text }
    )
}
