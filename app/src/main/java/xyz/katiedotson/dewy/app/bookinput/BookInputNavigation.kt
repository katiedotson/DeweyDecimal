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
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.Serializable

fun NavGraphBuilder.bookInputScreen(
    onNavigateBack: () -> Unit,
    onNavigateBackToDashboard: (String) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    composable<BookInputRoute> {
        val viewModel: BookInputViewModel = hiltViewModel()
        val vmState by viewModel.state.collectAsStateWithLifecycle()
        val bookSubjectsState by viewModel.bookSubjects.collectAsStateWithLifecycle()
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
            onSaveClicked = viewModel::onSave
        )
        val bookSubjectsBottomSheetState = mapToBookSubjects(
            bookSubjects = bookSubjectsState,
            onCustomSubjectsFieldChanged = viewModel::onCustomSubjectsFieldChanged,
            onSubjectValueChange = viewModel::onSubjectChipStateChange,
            onSaveSubject = viewModel::onSaveSubject
        )
        BookInputScreen(
            viewState = viewState,
            onBackClicked = onNavigateBack,
            subjectsBottomSheetState = bookSubjectsBottomSheetState,
        )
    }
}

fun mapToBookSubjects(
    bookSubjects: List<ChipState>,
    onCustomSubjectsFieldChanged: (String) -> Unit,
    onSubjectValueChange: (Int) -> Unit,
    onSaveSubject: (String) -> Unit,
): BookSubjectsBottomSheetState {
    return BookSubjectsBottomSheetState(
        subjects = bookSubjects.map {
            ChipViewState(
                isSelected = it.isSelected,
                display = it.display
            )
        }.toImmutableList(),
        onTextFieldChanged = onCustomSubjectsFieldChanged,
        onSubjectSelected = onSubjectValueChange,
        onSaveSubject = onSaveSubject
    )
}

fun mapToViewState(
    vmState: BookInputState,
    onTitleChanged: (TextFieldValue) -> Unit,
    onAuthorsFieldValueChange: (Int, TextFieldValue) -> Unit,
    onRemoveAuthor: (Int) -> Unit,
    onAddAuthor: () -> Unit,
    onLanguageValueChange: (Int) -> Unit,
    onPublisherValueChange: (Int) -> Unit,
    onSaveClicked: () -> Unit
): BookInputViewState {
    return BookInputViewState(
        titleLabel = "Title",
        titleValue = vmState.titleState,
        onTitleChanged = onTitleChanged,
        titleError = if (vmState.titleError) "A title is required" else null,
        authorLabel = "Author",
        authors = vmState.authors.toPersistentList(),
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
        onSaveClicked = onSaveClicked,
    )
}

data class BookInputViewState(
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
    // save button
    val onSaveClicked: () -> Unit
)

data class ChipViewState(
    val isSelected: Boolean,
    val display: String
)

data class BookSubjectsBottomSheetState(
    val subjects: ImmutableList<ChipViewState>,
    val onTextFieldChanged: (String) -> Unit,
    val onSubjectSelected: (Int) -> Unit,
    val onSaveSubject: (String) -> Unit,
)

fun NavController.navigateToBookInputScreen(bookId: String, navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = BookInputRoute(bookId)) {
        navOptions()
    }
}

@Serializable
data class BookInputRoute(val bookId: String)
