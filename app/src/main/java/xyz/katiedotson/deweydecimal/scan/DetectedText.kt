package xyz.katiedotson.deweydecimal.scan

import com.google.mlkit.vision.text.Text

data class DetectedText(val blocks: List<String>)

fun Text.toDetectedText(): DetectedText {
    return DetectedText(
        blocks = this.textBlocks.map { it.text }
    )
}
