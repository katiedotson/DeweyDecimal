package xyz.katiedotson.dewy.app.bookinput

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import xyz.katiedotson.dewy.MainDispatcherRule
import xyz.katiedotson.dewy.model.UserSubject

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class BookInputViewModelTest {

    private fun initViewModel(
        mockGetBookInputModel: GetBookInputModelUseCase? = null,
        mockSaveBookToLibrary: SaveBookToLibraryUseCase? = null,
        mockGetAllBookSubjects: GetAllBookSubjectsUseCase? = null,
        mockSaveBookSubject: SaveBookSubjectUseCase? = null,
    ): BookInputViewModel {
        val savedStateHandle = SavedStateHandle(
            route = BookInputRoute("BOOK_ID")
        )
        val successfulGetBookModel = mockk<GetBookInputModelUseCase>(relaxed = true)
        coEvery { successfulGetBookModel("BOOK_ID") } returns Result.success(defaultBookInputModel)

        val successfulBookSubjects = mockk<GetAllBookSubjectsUseCase>(relaxed = true)
        coEvery { successfulBookSubjects() } returns Result.success(emptyList())

        val successfulSaveBook = mockk<SaveBookToLibraryUseCase>(relaxed = true)
        coEvery { successfulSaveBook(any()) } returns Result.success(Unit)

        val successfulSaveBookSubject = mockk<SaveBookSubjectUseCase>(relaxed = true)
        coEvery { successfulSaveBookSubject(any()) } returns Result.success("New Subject")

        // Initialize the viewModel with the mocked SavedStateHandle
        return BookInputViewModel(
            savedStateHandle = savedStateHandle,
            getBookInputModel = mockGetBookInputModel ?: successfulGetBookModel,
            saveBookToLibrary = mockSaveBookToLibrary ?: successfulSaveBook,
            getAllBookSubjects = mockGetAllBookSubjects ?: successfulBookSubjects,
            saveSubject = mockSaveBookSubject ?: successfulSaveBookSubject,
        )
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `onTitleValueChange updates title and error state`() = runTest {
        // Arrange
        val viewModel = initViewModel()

        // Act
        viewModel.onTitleValueChange(TextFieldValue("New Title"))

        // Assert
        val state = viewModel.state.first()
        assertThat(state.titleState.text).isEqualTo("New Title")
        assertThat(state.titleError).isFalse()
    }

    @Test
    fun `onAuthorsFieldValueChange updates specific author`() = runTest {
        // Arrange
        val viewModel = initViewModel()

        // Arrange
        viewModel.onAddAuthor()
        viewModel.onAddAuthor()

        // Act
        viewModel.onAuthorsFieldValueChange(1, TextFieldValue("Author 2"))

        // Assert
        val state = viewModel.state.first()
        assertThat(state.authors[1].text).isEqualTo("Author 2")
    }

    @Test
    fun `onAddAuthor adds a new author`() = runTest {
        // Arrange
        val viewModel = initViewModel()

        // Act
        viewModel.onAddAuthor()

        // Assert
        val state = viewModel.state.first()
        assertThat(state.authors).hasSize(3)
    }

    @Test
    fun `onRemoveAuthor removes specified author`() = runTest {
        // Arrange
        val viewModel = initViewModel()

        // Act
        viewModel.onRemoveAuthor(0)

        // Assert
        val state = viewModel.state.first()
        assertThat(state.authors).hasSize(1)
    }

    @Test
    fun `onLanguageChipStateChange toggles language selection`() = runTest {
        // Arrange
        val viewModel = initViewModel()

        // Act
        viewModel.onLanguageChipStateChange(0)

        // Assert
        val state = viewModel.state.first()
        assertThat(state.languages[0].isSelected).isTrue()
    }

    @Test
    fun `onSaveSubject adds a new subject`() = runTest {
        // Arrange
        val viewModel = initViewModel()

        // Act
        viewModel.onSaveSubject("New Subject")

        // Assert
        val allSubjects = viewModel.allBookSubjects.first()
        assertThat(allSubjects).hasSize(1)
        assertThat(allSubjects[0].display).isEqualTo("New Subject")
    }

    @Test
    fun `onSave emits success event if validation passes`() = runTest {
        // Arrange
        val viewModel = initViewModel()

        viewModel.onTitleValueChange(TextFieldValue("Valid Title"))
        viewModel.onLanguageChipStateChange(0) // Assume first language is valid
        viewModel.onPublishersChipStateChange(0) // Assume first publisher is valid

        // Act
        viewModel.onSave()

        // Assert
        val events = viewModel.events.first()
        assertThat(events).contains(Event.Success("Valid Title"))
    }

    @Test
    fun `init loads book input model and subjects`() = runTest {
        // Arrange
        val mockGetBookInputModel = mockk<GetBookInputModelUseCase>(relaxed = true)
        coEvery { mockGetBookInputModel("BOOK_ID") } returns Result.success(defaultBookInputModel)

        val mockGetAllBookSubjects = mockk<GetAllBookSubjectsUseCase>(relaxed = true)
        coEvery { mockGetAllBookSubjects() } returns Result.success(emptyList())

        val viewModel = initViewModel(
            mockGetBookInputModel = mockGetBookInputModel,
            mockGetAllBookSubjects = mockGetAllBookSubjects
        )

        // Act
        advanceUntilIdle()

        // Assert: Verify that the book input model is loaded into the state
        val state = viewModel.state.first()
        assertThat(state.titleState.text).isEqualTo(defaultBookInputModel.title)
        assertThat(state.authors[0]).isEqualTo(TextFieldValue(defaultBookInputModel.authors[0]))
        assertThat(state.authors[1]).isEqualTo(TextFieldValue(defaultBookInputModel.authors[1]))

        // Assert: Verify that the subjects are loaded
        val allSubjects = viewModel.allBookSubjects.first()
        assertThat(allSubjects).isEmpty()
    }

    @Test
    fun `onSave calls saveBookToLibrary use case`() = runTest {
        // Arrange
        val mockSaveBookToLibrary = mockk<SaveBookToLibraryUseCase>(relaxed = true)
        coEvery { mockSaveBookToLibrary(any()) } returns Result.success(Unit)

        val viewModel = initViewModel(
            mockSaveBookToLibrary = mockSaveBookToLibrary
        )
        val savedBookInputSlot = slot<SavedBookInput>()

        viewModel.onTitleValueChange(TextFieldValue("Valid Title"))
        viewModel.onLanguageChipStateChange(0)
        viewModel.onPublishersChipStateChange(0)
        viewModel.onSave()

        coVerify { mockSaveBookToLibrary(capture(savedBookInputSlot)) }

        assertThat(savedBookInputSlot.captured.title).isEqualTo("Valid Title")
        assertThat(savedBookInputSlot.captured.authors).contains("AUTHOR_ONE")
        assertThat(savedBookInputSlot.captured.authors).contains("AUTHOR_TWO")
    }

    @Test
    fun `onSave updates field validation if validation fails`() = runTest {
        // Arrange
        val viewModel = initViewModel()

        // Act: Set invalid values for all fields
        viewModel.onTitleValueChange(TextFieldValue("")) // Empty title is invalid
        viewModel.onAuthorsFieldValueChange(0, TextFieldValue("")) // Empty author is invalid

        // Act: Trigger the save action
        viewModel.onSave()

        // Assert: Check that all relevant error states are updated
        val state = viewModel.state.first()

        // Title error should be true
        assertThat(state.titleError).isTrue()

        // Author error should be true (since the author name is empty)
        assertThat(state.authorsError).isTrue()

        // Publisher error should be true (since no publisher is selected)
        assertThat(state.publisherError).isTrue()

        // Language error should be true (since no language is selected)
        assertThat(state.languageError).isTrue()
    }

    @Test
    fun `init loads book input model and subjects on failure`() = runTest {
        // Arrange: Mock a failure response from GetBookInputModelUseCase
        val mockGetBookInputModel = mockk<GetBookInputModelUseCase>(relaxed = true)
        coEvery { mockGetBookInputModel("BOOK_ID") } returns
            Result.failure(Exception("Failed to load book input model"))
        val viewModel = initViewModel(mockGetBookInputModel = mockGetBookInputModel)

        // Act
        advanceUntilIdle()

        // Assert: Ensure the error is properly handled (e.g., by checking for an error state in the view model)
        val events = viewModel.events.first()
        assertThat(events).contains(Event.Error)
    }

    @Test
    fun `onSave calls saveBookToLibrary and handles failure`() = runTest {
        // Arrange: Mock failure in saveBookToLibrary use case
        val mockSaveBookToLibrary = mockk<SaveBookToLibraryUseCase>(relaxed = true)
        coEvery { mockSaveBookToLibrary(any()) } returns Result.failure(Exception("Failed to save book"))

        val viewModel = initViewModel(mockSaveBookToLibrary = mockSaveBookToLibrary)

        // Act: Set valid data and try to save
        viewModel.onTitleValueChange(TextFieldValue("Valid Title"))
        viewModel.onLanguageChipStateChange(0)
        viewModel.onPublishersChipStateChange(0)
        viewModel.onSave()

        // Assert: Check that the error event was emitted when save fails
        val events = viewModel.events.first()
        assertThat(events).contains(Event.Error)
    }

    @Test
    fun `onSave includes selected added subjects when saving book`() = runTest {
        // Arrange
        val mockSaveBookToLibrary = mockk<SaveBookToLibraryUseCase>(relaxed = true)
        coEvery { mockSaveBookToLibrary(any()) } returns Result.success(Unit)

        val mockSaveBookSubject = mockk<SaveBookSubjectUseCase>(relaxed = true)
        coEvery { mockSaveBookSubject(any()) } returns Result.success("Fiction")

        val viewModel = initViewModel(
            mockSaveBookToLibrary = mockSaveBookToLibrary,
            mockSaveBookSubject = mockSaveBookSubject,
        )
        val savedBookInputSlot = slot<SavedBookInput>()

        // Set initial fields with a valid title and author
        viewModel.onTitleValueChange(TextFieldValue("Valid Title"))
        viewModel.onLanguageChipStateChange(0)
        viewModel.onPublishersChipStateChange(0)

        // Add a subject and select it
        viewModel.onSaveSubject("Fiction")

        // Act: Trigger the save action
        viewModel.onSave()

        // Ensure coroutines finish before verifying
        advanceUntilIdle()

        // Assert: Verify that saveBookToLibrary was called and includes the selected subjects
        coVerify { mockSaveBookToLibrary(capture(savedBookInputSlot)) }

        // Assert: Check the captured SavedBookInput to ensure subjects are saved
        assertThat(savedBookInputSlot.captured.title).isEqualTo("Valid Title")
        assertThat(savedBookInputSlot.captured.subjects).contains("Fiction")
    }

    @Test
    fun `onSave includes selected existing subjects when saving book`() = runTest {
        // Arrange
        val mockSaveBookToLibrary = mockk<SaveBookToLibraryUseCase>(relaxed = true)
        coEvery { mockSaveBookToLibrary(any()) } returns Result.success(Unit)

        val getBookSubjects = mockk<GetAllBookSubjectsUseCase>(relaxed = true)
        coEvery { getBookSubjects() } returns Result.success(listOf(UserSubject(name = "SUBJECT_ONE", userId = "")))

        val viewModel = initViewModel(
            mockSaveBookToLibrary = mockSaveBookToLibrary,
            mockGetAllBookSubjects = getBookSubjects,
        )
        val savedBookInputSlot = slot<SavedBookInput>()

        viewModel.onLanguageChipStateChange(0)
        viewModel.onPublishersChipStateChange(0)

        // Add a subject and select it
        viewModel.onSubjectChipStateChange(0)

        // Act: Trigger the save action
        viewModel.onSave()

        // Ensure coroutines finish before verifying
        advanceUntilIdle()

        // Assert: Verify that saveBookToLibrary was called and includes the selected subjects
        coVerify { mockSaveBookToLibrary(capture(savedBookInputSlot)) }

        // Assert: Check the captured SavedBookInput to ensure subjects are saved
        assertThat(savedBookInputSlot.captured.subjects).contains("SUBJECT_ONE")
    }

    @Test
    fun `onSave does not include added subjects if unselected when saving book`() = runTest {
        // Arrange
        val mockSaveBookToLibrary = mockk<SaveBookToLibraryUseCase>(relaxed = true)
        coEvery { mockSaveBookToLibrary(any()) } returns Result.success(Unit)

        val mockSaveBookSubject = mockk<SaveBookSubjectUseCase>(relaxed = true)
        coEvery { mockSaveBookSubject(any()) } returns Result.success("Fiction")

        val viewModel = initViewModel(
            mockSaveBookToLibrary = mockSaveBookToLibrary,
            mockSaveBookSubject = mockSaveBookSubject,
        )
        val savedBookInputSlot = slot<SavedBookInput>()

        // Set initial fields
        viewModel.onLanguageChipStateChange(0)
        viewModel.onPublishersChipStateChange(0)

        // Add a subject and select it
        viewModel.onSaveSubject("Fiction")
        viewModel.onSubjectChipStateChange(0)

        // Act: Trigger the save action
        viewModel.onSave()

        // Ensure coroutines finish before verifying
        advanceUntilIdle()

        // Assert: Verify that saveBookToLibrary was called and includes the selected subjects
        coVerify { mockSaveBookToLibrary(capture(savedBookInputSlot)) }

        // Assert: Check the captured SavedBookInput to ensure subjects are saved
        assertThat(savedBookInputSlot.captured.subjects).isEmpty()
    }

    companion object {
        private val defaultBookInputModel = BookInputModel(
            key = "KEY",
            title = "TITLE",
            authors = listOf("AUTHOR_ONE", "AUTHOR_TWO"),
            languages = listOf("LANGUAGE_ONE", "LANGUAGE_TWO", "LANGUAGE_THREE"),
            publishers = listOf("PUBLISHER_ONE", "PUBLISHER_TWO"),
            subjects = emptyList(),
        )
    }
}
