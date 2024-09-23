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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import xyz.katiedotson.deweydecimal.ui.theme.Typography

@Composable
internal fun BookInputScreen(
    viewState: BookInputViewState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TitleSection(
            titleLabel = viewState.titleLabel,
            titleValue = viewState.titleValue,
            onTitleChanged = viewState.onTitleChanged,
        )
        AuthorSection(
            authorLabel = viewState.authorLabel,
            authors = viewState.authors,
            onAuthorFieldChanged = viewState.onAuthorFieldChanged,
            onRemoveAuthor = viewState.onRemoveAuthor,
            onAddAuthor = viewState.onAddAuthor,
        )
        ChipsSection(
            sectionHeading = viewState.languagesHeading,
            sectionSubheading = viewState.languagesSubheading,
            values = viewState.languages,
            onChange = viewState.onLanguageValueChange,
        )
        ChipsSection(
            sectionHeading = viewState.publisherHeading,
            sectionSubheading = viewState.publisherSubheading,
            values = viewState.publishers,
            onChange = viewState.onPublisherValueChange,
        )
        ChipsSection(
            sectionHeading = viewState.subjectsHeading,
            sectionSubheading = viewState.subjectsSubheading,
            values = viewState.subjects,
            onChange = viewState.onSubjectValueChange
        )
    }
}

@Composable
private fun TitleSection(
    titleLabel: String,
    titleValue: TextFieldValue,
    onTitleChanged: (TextFieldValue) -> Unit,
) {
    OutlinedTextField(
        label = {
            Text(text = titleLabel)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        value = titleValue,
        onValueChange = onTitleChanged
    )
}

@Composable
private fun AuthorSection(
    authorLabel: String,
    authors: List<TextFieldValue>,
    onAuthorFieldChanged: (Int, TextFieldValue) -> Unit,
    onRemoveAuthor: (Int) -> Unit,
    onAddAuthor: () -> Unit
) {
    authors.forEachIndexed { index, textFieldValue ->
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                label = {
                    Text(text = authorLabel)
                },
                modifier = Modifier.weight(1f),
                value = textFieldValue,
                onValueChange = {
                    onAuthorFieldChanged(index, it)
                }
            )
            IconButton(
                onClick = {
                    onRemoveAuthor(index)
                }
            ) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "Remove author")
            }
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
            onClick = onAddAuthor
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add another author"
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChipsSection(
    sectionHeading: String,
    sectionSubheading: String,
    values: ImmutableList<ChipViewState>,
    onChange: (Int) -> Unit
) {
    Spacer(modifier = Modifier.padding(vertical = 8.dp))
    Text(text = sectionHeading, style = Typography.labelLarge)
    Text(text = sectionSubheading, style = Typography.labelSmall)
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        values.forEachIndexed { index, chipViewState ->
            FilterChip(
                selected = chipViewState.isSelected,
                onClick = { onChange(index) },
                leadingIcon = {
                    if (chipViewState.isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected"
                        )
                    }
                },
                label = {
                    Text(text = chipViewState.display)
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun BookInputScreenPreview() {
    BookInputScreen(
        viewState = BookInputViewState(
            titleLabel = "Title",
            titleValue = TextFieldValue(""),
            onTitleChanged = {},
            authorLabel = "Author",
            authors = persistentListOf(TextFieldValue("")),
            onAuthorFieldChanged = { _, _ -> },
            onRemoveAuthor = { _ -> },
            onAddAuthor = {},
            languagesHeading = "Languages",
            languagesSubheading = "Choose Multiple",
            languages = persistentListOf(ChipViewState(isSelected = true, display = "English")),
            onLanguageValueChange = { _ -> },
            publisherHeading = "Publisher",
            publisherSubheading = "Choose 1",
            publishers = persistentListOf(
                ChipViewState(isSelected = true, display = "Harper Collins"),
                ChipViewState(isSelected = false, display = "Voyager")
            ),
            onPublisherValueChange = { _ -> },
            subjectsHeading = "Subjects",
            subjectsSubheading = "Choose Multiple",
            subjects = persistentListOf(
                ChipViewState(isSelected = true, display = "Sci Fi"),
                ChipViewState(isSelected = true, display = "American Literature")
            ),
            onSubjectValueChange = { _ -> }
        )
    )
}
