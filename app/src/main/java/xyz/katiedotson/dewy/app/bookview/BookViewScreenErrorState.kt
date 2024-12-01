package xyz.katiedotson.dewy.app.bookview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import xyz.katiedotson.dewy.ui.theme.DeweyDecimalTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BookViewScreenErrorState(
    onNavigateBack: () -> Unit,
) {
    Surface {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onNavigateBack
                        ) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Exit"
                            )
                        }
                    },
                    title = {
                        Text(text = "About this Book")
                    }
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Something went wrong.",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "We're sorry, we couldn't load that right now.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Text(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                text = "Error code: 1501",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun BookViewScreenErrorStatePreview() {
    DeweyDecimalTheme {
        BookViewScreenErrorState(
            onNavigateBack = {},
        )
    }
}
