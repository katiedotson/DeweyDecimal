package xyz.katiedotson.deweydecimal.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val GreenOne = Color(0xFFF1FFFA)
val GreenTwo = Color(0xFFCCFCCB)
val GreenThree = Color(0xFF96e6b3)
val GreenFour = Color(0xFF568259)
val GreenFive = Color(0xFF464E47)

val BlueOne = Color(0xFFE2EDF5)
val BlueTwo = Color(0xFFA6C7E0)
val BlueThree = Color(0xFF8DBEE3)
val BlueFour = Color(0xFF5B94BF)
val BlueFive = Color(0xFF3A698D)

@Composable
private fun ColorPreview(
    color: Color,
    name: String
) {
    Box(
        modifier = Modifier
            .background(color = color)
            .fillMaxWidth()
    ) {
        Text(name, Modifier.padding(8.dp))
    }
}

@Suppress("LongMethod")
@Composable
@Preview
fun ColorsPreview() {
    Column {
        ColorPreview(
            color = GreenOne,
            name = "Green One"
        )
        ColorPreview(
            color = GreenTwo,
            name = "Green Two"
        )
        ColorPreview(
            color = GreenThree,
            name = "Green Three"
        )
        ColorPreview(
            color = GreenFour,
            name = "Green Four"
        )
        ColorPreview(
            color = GreenFive,
            name = "Green Five"
        )
        ColorPreview(
            color = BlueOne,
            name = "Blue One"
        )
        ColorPreview(
            color = BlueTwo,
            name = "Blue Two"
        )
        ColorPreview(
            color = BlueThree,
            name = "Blue Three"
        )
        ColorPreview(
            color = BlueFour,
            name = "Blue Four"
        )
        ColorPreview(
            color = BlueFive,
            name = "Blue Five"
        )
    }
}
