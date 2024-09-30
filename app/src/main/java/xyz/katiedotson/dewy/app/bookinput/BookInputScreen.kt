package xyz.katiedotson.dewy.app.bookinput

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import xyz.katiedotson.dewy.ui.component.DewyTextField
import xyz.katiedotson.dewy.ui.theme.AppTypography
import xyz.katiedotson.dewy.ui.theme.DeweyDecimalTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun BookInputScreen(
    viewState: BookInputViewState,
    subjectsBottomSheetState: BookSubjectsBottomSheetState,
    onBackClicked: () -> Unit,
) {
    var showBottomSheet by remember { mutableStateOf(value = false) }
    var subjectsText by remember { mutableStateOf(value = "") }
    Surface {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .padding(bottom = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Heading(
                onBackClicked = onBackClicked
            )
            Text(
                text = "Title & Author",
                style = AppTypography.titleMedium,
            )
            TitleSection(
                titleLabel = viewState.titleLabel,
                titleValue = viewState.titleValue,
                onTitleChanged = viewState.onTitleChanged,
                titleError = viewState.titleError
            )
            AuthorSection(
                authorLabel = viewState.authorLabel,
                authors = viewState.authors,
                onAuthorFieldChanged = viewState.onAuthorFieldChanged,
                onRemoveAuthor = viewState.onRemoveAuthor,
                onAddAuthor = viewState.onAddAuthor,
                authorError = viewState.authorError,
            )
            ChipsSection(
                sectionHeading = viewState.languagesHeading,
                sectionSubheading = viewState.languagesSubheading,
                values = viewState.languages,
                onChange = viewState.onLanguageValueChange,
                errorMessage = viewState.languageError
            )
            ChipsSection(
                sectionHeading = viewState.publisherHeading,
                sectionSubheading = viewState.publisherSubheading,
                values = viewState.publishers,
                onChange = viewState.onPublisherValueChange,
                errorMessage = viewState.publisherError
            )
            Text(
                modifier = Modifier.padding(top = 24.dp),
                text = "Subjects",
                style = AppTypography.titleMedium,
            )
            FlowRow(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                subjectsBottomSheetState.subjects.forEach {
                    FilterChip(
                        colors = FilterChipDefaults.filterChipColors().copy(
                            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        selected = true,
                        onClick = {
                            showBottomSheet = true
                        },
                        label = {
                            Text(
                                text = it.display,
                            )
                        }
                    )
                }
            }
            AddCustomSubjectsButton(
                onClick = {
                    showBottomSheet = true
                }
            )
            SaveButton(
                viewState.onSaveClicked
            )
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            }
        ) {
            BottomSheetContent(
                subjectsBottomSheetState = subjectsBottomSheetState,
                subjectsText = subjectsText,
                onTextFieldChange = {
                    subjectsText = it
                    subjectsBottomSheetState.onTextFieldChanged(it)
                }
            )
        }
    }
}

@Composable
private fun BottomSheetContent(
    subjectsBottomSheetState: BookSubjectsBottomSheetState,
    subjectsText: String,
    onTextFieldChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .padding(bottom = 24.dp)
    ) {
        DewyTextField(
            onValueChange = onTextFieldChange,
            value = subjectsText,
            placeholder = "Search for existing subjects, or add a new one...",
            label = "Subject name"
        )
        if (subjectsBottomSheetState.subjects.isEmpty()) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inverseSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                onClick = { subjectsBottomSheetState.onSaveSubject(subjectsText) },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.padding(horizontal = 16.dp))
                Text(
                    text = "Save as new subject",
                    style = AppTypography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            ChipsSection(
                sectionHeading = "Subjects",
                sectionSubheading = null,
                values = subjectsBottomSheetState.subjects,
                onChange = subjectsBottomSheetState.onSubjectSelected,
                errorMessage = null
            )
        }
    }
}

@Composable
private fun AddCustomSubjectsButton(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            onClick = onClick
        ) {
            Text(text = "Add custom subjects")
        }
    }
}

@Composable
private fun Heading(
    onBackClicked: () -> Unit
) {
    IconButton(
        onBackClicked,
        modifier = Modifier
            .padding(bottom = 24.dp)
            .size(48.dp)
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
}

@Composable
private fun TitleSection(
    titleLabel: String,
    titleValue: TextFieldValue,
    onTitleChanged: (TextFieldValue) -> Unit,
    titleError: String?,
) {
    DewyTextField(
        label = titleLabel,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        value = titleValue.text,
        onValueChange = {
            onTitleChanged(TextFieldValue(it))
        },
        isError = titleError != null,
        errorText = titleError,
    )
}

@Composable
private fun AuthorSection(
    authorLabel: String,
    authors: List<TextFieldValue>,
    onAuthorFieldChanged: (Int, TextFieldValue) -> Unit,
    onRemoveAuthor: (Int) -> Unit,
    onAddAuthor: () -> Unit,
    authorError: String?
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
    InlineErrorMessage(
        authorError
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChipsSection(
    sectionHeading: String,
    sectionSubheading: String?,
    values: ImmutableList<ChipViewState>,
    onChange: (Int) -> Unit,
    errorMessage: String?,
) {
    Text(
        modifier = Modifier.padding(top = 24.dp),
        text = sectionHeading,
        style = AppTypography.titleMedium,
    )
    sectionSubheading?.let {
        Text(
            text = sectionSubheading,
            style = AppTypography.titleSmall,
        )
    }
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
    InlineErrorMessage(errorMessage)
}

@Composable
private fun InlineErrorMessage(
    errorMessage: String?
) {
    AnimatedVisibility(
        visible = errorMessage != null
    ) {
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .background(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.onErrorContainer,
                imageVector = Icons.Filled.Info,
                contentDescription = null
            )
            Spacer(
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = errorMessage ?: "",
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun SaveButton(
    onSaveClicked: () -> Unit,
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.inverseSurface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        onClick = onSaveClicked,
    ) {
        Text(
            text = "Save",
            style = AppTypography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
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
                titleValue = TextFieldValue(text = "Burning Chrome"),
                onTitleChanged = {},
                titleError = "Title field is required",
                authorLabel = "Author",
                authors = persistentListOf(TextFieldValue(text = "William Gibson")),
                onAuthorFieldChanged = { _, _ -> },
                onRemoveAuthor = { _ -> },
                onAddAuthor = {},
                authorError = null,
                languagesHeading = "Languages",
                languagesSubheading = "Choose Multiple",
                languages = persistentListOf(ChipViewState(isSelected = true, display = "English")),
                onLanguageValueChange = { _ -> },
                languageError = null,
                publisherHeading = "Publisher",
                publisherSubheading = "Choose 1",
                publishers = persistentListOf(
                    ChipViewState(isSelected = true, display = "Harper Collins"),
                    ChipViewState(isSelected = false, display = "Voyager")
                ),
                onPublisherValueChange = { _ -> },
                publisherError = "At least one publisher must be selected",
                onSaveClicked = {},
            ),
            subjectsBottomSheetState = BookSubjectsBottomSheetState(
                subjects = persistentListOf(ChipViewState(isSelected = true, display = "Shakespeare")),
                onTextFieldChanged = { _ -> },
                onSubjectSelected = { _ -> },
                onSaveSubject = { _ -> },
            )
        )
    }
}

@Composable
@PreviewLightDark
private fun BottomSheetPreview() {
    DeweyDecimalTheme {
        Surface {
            BottomSheetContent(
                subjectsBottomSheetState = BookSubjectsBottomSheetState(
                    subjects = persistentListOf(),
                    onSubjectSelected = { _ -> },
                    onTextFieldChanged = { _ -> },
                    onSaveSubject = { _ -> },
                ),
                subjectsText = "",
                onTextFieldChange = { _ -> }
            )
        }
    }
}
