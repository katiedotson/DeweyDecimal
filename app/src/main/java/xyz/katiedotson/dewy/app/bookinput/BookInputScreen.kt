package xyz.katiedotson.dewy.app.bookinput

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import xyz.katiedotson.dewy.ui.component.DewyTextField
import xyz.katiedotson.dewy.ui.theme.AppTypography
import xyz.katiedotson.dewy.ui.theme.DeweyDecimalTheme

@Composable
internal fun BookInputScreen(
    viewState: BookInputViewState,
    onBackClicked: () -> Unit
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            IconButton(
                onBackClicked,
                modifier = Modifier.padding(bottom = 24.dp).size(48.dp)
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null
                )
            }
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "We found your book.",
                style = AppTypography.displaySmall
            )
            Text(
                modifier = Modifier.padding(bottom = 24.dp),
                text = "Check over the details before saving it to your library.",
                style = AppTypography.bodyLarge
            )
            Text(
                text = "Title & Author",
                style = AppTypography.titleMedium,
            )
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
}

@Composable
private fun TitleSection(
    titleLabel: String,
    titleValue: TextFieldValue,
    onTitleChanged: (TextFieldValue) -> Unit,
) {
    DewyTextField(
        label = titleLabel,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        value = titleValue.text,
        onValueChange = {
            onTitleChanged(TextFieldValue(it))
        }
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
            DewyTextField(
                label = authorLabel,
                modifier = Modifier.weight(1f),
                value = textFieldValue.text,
                onValueChange = {
                    onAuthorFieldChanged(index, TextFieldValue(it))
                }
            )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            OutlinedIconButton(
                colors = IconButtonDefaults.outlinedIconButtonColors(),
                onClick = {
                    onRemoveAuthor(index)
                }
            ) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "Remove author")
            }
        }
    }
    Row(
        modifier = Modifier.padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Add another author",
            style = AppTypography.titleSmall,
        )
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        OutlinedIconButton(
            colors = IconButtonDefaults.outlinedIconButtonColors(),
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
    Text(
        modifier = Modifier.padding(top = 24.dp),
        text = sectionHeading,
        style = AppTypography.titleMedium,
    )
    Text(
        text = sectionSubheading,
        style = AppTypography.titleSmall,
    )
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
                    Text(text = chipViewState.display, style = AppTypography.labelMedium)
                }
            )
        }
    }
}

@Composable
@PreviewLightDark
@PreviewScreenSizes
private fun BookInputScreenPreview() {
    DeweyDecimalTheme {
        BookInputScreen(
            onBackClicked = {},
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
}
