package xyz.katiedotson.deweydecimal.bookinput

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.katiedotson.deweydecimal.ui.theme.Typography

@Composable
internal fun BookInputScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp)
    ) {
        TitleSection()
        AuthorSection()
        LanguageSection()
        PublisherSection()
        PublishedYearSection()
        SubjectsSection()
    }
}

@Composable
private fun TitleSection() {
    OutlinedTextField(
        label = {
            Text(text = "Title")
        },
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        value = TextFieldValue(text = "Burning Chrome"),
        onValueChange = {}
    )
}

@Composable
private fun AuthorSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            label = {
                Text(text = "Author")
            },
            modifier = Modifier.weight(1f),
            value = TextFieldValue(text = "William Gibson"),
            onValueChange = {}
        )
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Clear, contentDescription = "Remove author")
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Add another author",
            style = Typography.labelSmall
        )
        IconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add another author"
            )
        }
    }
}

@Composable
private fun LanguageSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            label = {
                Text(text = "Language")
            },
            modifier = Modifier.weight(1f),
            value = TextFieldValue(text = "English"),
            onValueChange = {}
        )
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Clear, contentDescription = "Remove author")
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Add another language",
            style = Typography.labelSmall
        )
        IconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add another language"
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PublisherSection() {
    Spacer(modifier = Modifier.padding(vertical = 8.dp))
    Text(text = "Publisher", style = Typography.labelLarge)
    Text(text = "Choose 1", style = Typography.labelSmall)
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = true,
            onClick = {},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected"
                )
            },
            label = {
                Text(text = "Harper Collins")
            }
        )
        FilterChip(
            selected = false,
            onClick = {},
            leadingIcon = null,
            label = {
                Text(text = "Scholastic")
            }
        )
        FilterChip(
            selected = false,
            onClick = {},
            leadingIcon = null,
            label = {
                Text(text = "Harvard Business Review")
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PublishedYearSection() {
    Spacer(modifier = Modifier.padding(vertical = 8.dp))
    Text(text = "Year Published", style = Typography.labelLarge)
    Text(text = "Choose 1", style = Typography.labelSmall)
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = true,
            onClick = {},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected"
                )
            },
            label = {
                Text(text = "1989")
            }
        )
        FilterChip(
            selected = false,
            onClick = {},
            leadingIcon = null,
            label = {
                Text(text = "1992")
            }
        )
        FilterChip(
            selected = false,
            onClick = {},
            leadingIcon = null,
            label = {
                Text(text = "1994")
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SubjectsSection() {
    Spacer(modifier = Modifier.padding(vertical = 8.dp))
    Text(text = "Subjects", style = Typography.labelLarge)
    Text(text = "Choose Multiple", style = Typography.labelSmall)
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = true,
            onClick = {},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected"
                )
            },
            label = {
                Text(text = "American Science fiction")
            }
        )
        FilterChip(
            selected = true,
            onClick = {},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected"
                )
            },
            label = {
                Text(text = "Fiction")
            }
        )
        FilterChip(
            selected = true,
            onClick = {},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected"
                )
            },
            label = {
                Text(text = "Cyberspace")
            }
        )
        FilterChip(
            selected = true,
            onClick = {},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected"
                )
            },
            label = {
                Text(text = "American literature")
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun BookInputScreenPreview() {
    BookInputScreen()
}
