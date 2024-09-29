package xyz.katiedotson.dewy.app.bookinput

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
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
        val events by viewModel.events.collectAsStateWithLifecycle()
        LaunchedEffect(events) {
            val last = events.lastOrNull()
            last?.let {
                when (it) {
                    Event.Error -> {
                        snackbarHostState.showSnackbar(
                            object : SnackbarVisuals {
                                override val actionLabel: String?
                                    get() = null
                                override val duration: SnackbarDuration
                                    get() = SnackbarDuration.Long
                                override val message: String
                                    get() = "Something went wrong while trying to save your book. Please try again."
                                override val withDismissAction: Boolean
                                    get() = true
                            }
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
            onSubjectValueChange = viewModel::onSubjectChipStateChange,
            onSaveClicked = viewModel::onSave
        )
        BookInputScreen(
            viewState = viewState,
            onBackClicked = onNavigateBack
        )
    }
}

fun mapToViewState(
    vmState: BookInputState,
    onTitleChanged: (TextFieldValue) -> Unit,
    onAuthorsFieldValueChange: (Int, TextFieldValue) -> Unit,
    onRemoveAuthor: (Int) -> Unit,
    onAddAuthor: () -> Unit,
    onLanguageValueChange: (Int) -> Unit,
    onPublisherValueChange: (Int) -> Unit,
    onSubjectValueChange: (Int) -> Unit,
    onSaveClicked: () -> Unit
): BookInputViewState {
    return BookInputViewState(
        titleLabel = "Title",
        titleValue = vmState.titleState,
        onTitleChanged = onTitleChanged,
        authorLabel = "Author",
        authors = vmState.authors.toPersistentList(),
        onAuthorFieldChanged = onAuthorsFieldValueChange,
        onRemoveAuthor = onRemoveAuthor,
        onAddAuthor = onAddAuthor,
        languagesHeading = "Language(s)",
        languagesSubheading = "Choose Multiple",
        languages = vmState.languages.map { chipState ->
            ChipViewState(
                isSelected = chipState.isSelected,
                display = chipState.display
            )
        }.toImmutableList(),
        onLanguageValueChange = onLanguageValueChange,
        publisherHeading = "Publisher",
        publisherSubheading = "Choose 1",
        publishers = vmState.publishers.map { chipState ->
            ChipViewState(
                isSelected = chipState.isSelected,
                display = chipState.display
            )
        }.toImmutableList(),
        onPublisherValueChange = onPublisherValueChange,
        subjectsHeading = "Subject(s)",
        subjectsSubheading = "Choose Multiple",
        subjects = vmState.subjects.map { chipState ->
            ChipViewState(
                isSelected = chipState.isSelected,
                display = chipState.display
            )
        }.toImmutableList(),
        onSubjectValueChange = onSubjectValueChange,
        onSaveClicked = onSaveClicked,
    )
}

data class BookInputViewState(
    // title
    val titleLabel: String,
    val titleValue: TextFieldValue,
    val onTitleChanged: (TextFieldValue) -> Unit,
    // authors
    val authorLabel: String,
    val authors: ImmutableList<TextFieldValue>,
    val onAuthorFieldChanged: (Int, TextFieldValue) -> Unit,
    val onRemoveAuthor: (Int) -> Unit,
    val onAddAuthor: () -> Unit,
    // languages
    val languagesHeading: String,
    val languagesSubheading: String,
    val languages: ImmutableList<ChipViewState>,
    val onLanguageValueChange: (Int) -> Unit,
    // publisher
    val publisherHeading: String,
    val publisherSubheading: String,
    val publishers: ImmutableList<ChipViewState>,
    val onPublisherValueChange: (Int) -> Unit,
    // subjects
    val subjectsHeading: String,
    val subjectsSubheading: String,
    val subjects: ImmutableList<ChipViewState>,
    val onSubjectValueChange: (Int) -> Unit,
    // save button
    val onSaveClicked: () -> Unit
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
