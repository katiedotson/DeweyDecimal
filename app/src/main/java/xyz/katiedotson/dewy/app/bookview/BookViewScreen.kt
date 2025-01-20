package xyz.katiedotson.dewy.app.bookview

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import xyz.katiedotson.dewy.model.UserBook
import xyz.katiedotson.dewy.ui.theme.AppTypography
import xyz.katiedotson.dewy.ui.theme.DeweyDecimalTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookViewScreen(
    book: UserBook,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState()
) {
    Surface {
        Box(
            modifier = modifier.fillMaxSize()
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
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(24.dp)
                        .padding(bottom = 24.dp)
                ) {
                    TextProperty(
                        propertyName = "Title",
                        propertyValue = book.title,
                        onEdit = {}
                    )
                    TextProperty(
                        propertyName = "Author",
                        propertyValue = book.authors.joinToString(),
                        onEdit = {}
                    )
                    TextProperty(
                        propertyName = "Publisher",
                        propertyValue = book.publisher,
                        onEdit = {}
                    )
                    MultiTextProperty(
                        propertyName = "Subject(s)",
                        propertyValues = book.subjects,
                        onEdit = {}
                    )
                    MultiTextProperty(
                        propertyName = "Language(s)",
                        propertyValues = book.languages,
                        onEdit = {}
                    )
                    ActionButtons(
                        onClick = onNavigateBack
                    )
                }
            }
        }
    }
}

@Composable
private fun TextProperty(
    propertyName: String,
    propertyValue: String,
    onEdit: () -> Unit,
) {
    Text(
        text = propertyName,
        style = MaterialTheme.typography.labelSmall,
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = propertyValue,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onEdit) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit"
            )
        }
    }

    HorizontalDivider(
        modifier = Modifier.padding(vertical = 20.dp)
    )
    Spacer(
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MultiTextProperty(
    propertyName: String,
    propertyValues: List<String>,
    onEdit: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = propertyName,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onEdit) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit"
            )
        }
    }
    Spacer(
        modifier = Modifier.padding(vertical = 4.dp)
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        propertyValues.forEach {
            Text(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = FilterChipDefaults.shape
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                text = it,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = AppTypography.labelMedium
            )
        }
    }
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 20.dp)
    )
    Spacer(
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun ActionButtons(onClick: () -> Unit) {
    Row(
        modifier = Modifier.padding(top = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(
            modifier = Modifier.weight(1f)
        )
        OutlinedButton(onClick = onClick) {
            Text(text = "Done", style = AppTypography.labelLarge)
        }
    }
}

@Composable
@PreviewLightDark
@PreviewScreenSizes
private fun BookViewScreenPreview() {
    DeweyDecimalTheme {
        BookViewScreen(
            book = UserBook(
                key = "",
                userId = "",
                title = "Media Control",
                authors = listOf("Noam Chomsky"),
                languages = listOf("English"),
                publisher = "Penguin Classics",
                subjects = listOf("Philosophy", "Economics", "Political Science", "Humanities"),
            ),
            onNavigateBack = {}
        )
    }
}
