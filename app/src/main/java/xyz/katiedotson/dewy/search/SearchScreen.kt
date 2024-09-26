package xyz.katiedotson.dewy.search

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.katiedotson.dewy.ui.theme.DeweyDecimalTheme

@Composable
internal fun SearchScreen(
    onNavigateToCameraScanScreen: () -> Unit
) {
    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = "home screen")
            OutlinedIconButton(
                onClick = onNavigateToCameraScanScreen,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp),
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new")
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun SearchScreenPreviewDark() {
    DeweyDecimalTheme {
        SearchScreen(
            onNavigateToCameraScanScreen = {}
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun SearchScreenPreviewLight() {
    DeweyDecimalTheme {
        SearchScreen(
            onNavigateToCameraScanScreen = {}
        )
    }
}
