package xyz.katiedotson.dewy.app.bookinput

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable

fun NavGraphBuilder.bookInputScreen(
    onNavigateBack: () -> Unit,
    onNavigateBackToDashboard: (String) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    composable<BookInputRoute> {
        val viewModel: BookInputViewModel = hiltViewModel()
        val vmState by viewModel.state.collectAsStateWithLifecycle()
        val subjects by viewModel.allBookSubjects.collectAsStateWithLifecycle()
        val events by viewModel.events.collectAsStateWithLifecycle()
        LaunchedEffect(events) {
            val last = events.lastOrNull()
            last?.let {
                when (it) {
                    Event.Error -> {
                        snackbarHostState.showSnackbar(
                            message = "Something went wrong while trying to save your book. Please try again.",
                            withDismissAction = true,
                            duration = SnackbarDuration.Long,
                        )
                    }

                    is Event.Success -> {
                        onNavigateBackToDashboard(it.bookTitle)
                    }
                }
            }
        }

        val viewState = mapToViewState(
            vmState = vmState,
            onTitleChanged = viewModel::onTitleValueChange,
            onAuthorsFieldValueChange = viewModel::onAuthorsFieldValueChange,
            onRemoveAuthor = viewModel::onRemoveAuthor,
            onAddAuthor = viewModel::onAddAuthor,
            onLanguageValueChange = viewModel::onLanguageChipStateChange,
            onPublisherValueChange = viewModel::onPublishersChipStateChange,
            subjects = subjects,
            onCustomSubjectsFieldChanged = viewModel::onCustomSubjectsFieldChanged,
            onSubjectSelected = viewModel::onSubjectChipStateChange,
            onSaveSubject = viewModel::onSaveSubject,
            onSaveClicked = viewModel::onSave,
        )
        BookInputScreen(
            viewState = viewState,
            onBackClicked = onNavigateBack,
        )
    }
}

internal fun mapToViewState(
    vmState: BookInputState,
    onTitleChanged: (TextFieldValue) -> Unit,
    onAuthorsFieldValueChange: (Int, TextFieldValue) -> Unit,
    onRemoveAuthor: (Int) -> Unit,
    onAddAuthor: () -> Unit,
    onLanguageValueChange: (Int) -> Unit,
    onPublisherValueChange: (Int) -> Unit,
    subjects: ImmutableList<SubjectState>,
    onCustomSubjectsFieldChanged: (String) -> Unit,
    onSubjectSelected: (Int) -> Unit,
    onSaveSubject: (String) -> Unit,
    onSaveClicked: () -> Unit
): BookInputViewState {
    return BookInputViewState(
        heading = "We found your book.",
        subheading = "Check over the details before saving it to your library.",
        titleLabel = "Title",
        titleValue = vmState.titleState,
        onTitleChanged = onTitleChanged,
        titleError = if (vmState.titleError) "A title is required" else null,
        authorLabel = "Author",
        authors = vmState.authors.toImmutableList(),
        onAuthorFieldChanged = onAuthorsFieldValueChange,
        onRemoveAuthor = onRemoveAuthor,
        onAddAuthor = onAddAuthor,
        authorError = if (vmState.authorsError) "At least one author is required" else null,
        languagesHeading = "Language(s)",
        languagesSubheading = "Choose Multiple",
        languages = vmState.languages.map { chipState ->
            ChipViewState(
                isSelected = chipState.isSelected,
                display = chipState.display
            )
        }.toImmutableList(),
        onLanguageValueChange = onLanguageValueChange,
        languageError = if (vmState.languageError) "At least one language is required" else null,
        publisherHeading = "Publisher",
        publisherSubheading = "Choose 1",
        publishers = vmState.publishers.map { chipState ->
            ChipViewState(
                isSelected = chipState.isSelected,
                display = chipState.display
            )
        }.toImmutableList(),
        onPublisherValueChange = onPublisherValueChange,
        publisherError = if (vmState.publisherError) "At least one publisher is required" else null,
        subjectsHeading = "Subjects",
        addCustomSubjectButtonText = "Add & manage subjects",
        appliedSubjects = subjects.filter { it.isApplied }.map { it.display }.toImmutableList(),
        filteredSubjects = subjects
            .filter {
                it.showInFiltered
            }
            .map {
                ChipViewState(
                    isSelected = it.isApplied,
                    display = it.display
                )
            }
            .toImmutableList(),
        onSubjectTextFieldChanged = onCustomSubjectsFieldChanged,
        onSubjectSelected = onSubjectSelected,
        onSaveSubject = onSaveSubject,
        saveButtonText = "Save to library",
        onSaveClicked = onSaveClicked,
        selectedChipContentDescription = "Selected"
    )
}

data class BookInputViewState(
    // heading
    val heading: String,
    val subheading: String,
    // title
    val titleLabel: String,
    val titleValue: TextFieldValue,
    val onTitleChanged: (TextFieldValue) -> Unit,
    val titleError: String?,
    // authors
    val authorLabel: String,
    val authors: ImmutableList<TextFieldValue>,
    val onAuthorFieldChanged: (Int, TextFieldValue) -> Unit,
    val onRemoveAuthor: (Int) -> Unit,
    val onAddAuthor: () -> Unit,
    val authorError: String?,
    // languages
    val languagesHeading: String,
    val languagesSubheading: String,
    val languages: ImmutableList<ChipViewState>,
    val onLanguageValueChange: (Int) -> Unit,
    val languageError: String?,
    // publisher
    val publisherHeading: String,
    val publisherSubheading: String,
    val publishers: ImmutableList<ChipViewState>,
    val onPublisherValueChange: (Int) -> Unit,
    val publisherError: String?,
    // subjects
    val subjectsHeading: String,
    val addCustomSubjectButtonText: String,
    val appliedSubjects: ImmutableList<String>,
    val filteredSubjects: ImmutableList<ChipViewState>,
    val onSubjectTextFieldChanged: (String) -> Unit,
    val onSubjectSelected: (Int) -> Unit,
    val onSaveSubject: (String) -> Unit,
    // save button
    val saveButtonText: String,
    val onSaveClicked: () -> Unit,
    // other
    val selectedChipContentDescription: String,
)

data class ChipViewState(
    val isSelected: Boolean,
    val display: String
)

fun NavController.navigateToBookInputScreen(bookId: String, navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = BookInputRoute(bookId)) {
        navOptions()
    }
}

@Serializable
data class BookInputRoute(val bookId: String)
