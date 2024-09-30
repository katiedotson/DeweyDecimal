package xyz.katiedotson.dewy.app.bookinput

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookInputViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBookInputModel: GetBookInputModelUseCase,
    private val saveBookToLibrary: SaveBookToLibraryUseCase
) : ViewModel() {

    private val key = savedStateHandle.toRoute<BookInputRoute>().bookId

    init {
        viewModelScope.launch {
            getBookInputModel(
                key
            ).onSuccess { book ->
                _state.update {
                    BookInputState(
                        titleState = TextFieldValue(book.title),
                        titleError = false,
                        authors = book.authors.map {
                            TextFieldValue(it)
                        },
                        authorsError = false,
                        languages = book.languages.mapIndexed { index, language ->
                            ChipState(
                                index = index,
                                display = language,
                                isSelected = false,
                            )
                        },
                        languageError = false,
                        publishers = book.publishers.mapIndexed { index, publisherName ->
                            ChipState(
                                index = index,
                                display = publisherName,
                                isSelected = false,
                            )
                        },
                        publisherError = false,
                        subjects = book.subjects.mapIndexed { index, subjectName ->
                            ChipState(
                                index = index,
                                display = subjectName,
                                isSelected = false,
                            )
                        },
                    )
                }
            }.onFailure { e ->
                println(e)
            }
        }
    }

    private val _state = MutableStateFlow(
        BookInputState(
            titleState = TextFieldValue(),
            titleError = false,
            authors = listOf(),
            authorsError = false,
            languages = listOf(),
            languageError = false,
            publishers = listOf(),
            publisherError = false,
            subjects = listOf(),
        )
    )
    val state = _state.asStateFlow()

    private val _events = MutableStateFlow<List<Event>>(listOf())
    val events = _events.asStateFlow()

    fun onTitleValueChange(textFieldValue: TextFieldValue) {
        _state.update { current ->
            current.copy(
                titleState = textFieldValue,
                titleError = textFieldValue.text.isBlank()
            )
        }
    }

    fun onAuthorsFieldValueChange(index: Int, textFieldValue: TextFieldValue) {
        _state.update { current ->
            current.copy(
                authors = current
                    .authors
                    .mapIndexed { fieldIndex, field ->
                        return@mapIndexed if (index == fieldIndex) {
                            textFieldValue
                        } else {
                            field
                        }
                    },
                authorsError = false,
            )
        }
    }

    fun onAddAuthor() {
        _state.update { current ->
            current.copy(
                authors = current.authors.plus(TextFieldValue())
            )
        }
    }

    fun onRemoveAuthor(index: Int) {
        if (_state.value.authors.size == 1) return
        _state.update { current ->
            current.copy(
                authors = current.authors.filterIndexed { fieldIndex, _ -> index != fieldIndex },
                authorsError = false,
            )
        }
    }

    fun onLanguageChipStateChange(index: Int) {
        _state.update { current ->
            current.copy(
                languages = current
                    .languages
                    .mapIndexed { fieldIndex, field ->
                        return@mapIndexed if (index == fieldIndex) {
                            field.copy(isSelected = field.isSelected.not())
                        } else {
                            field
                        }
                    },
                languageError = false,
            )
        }
    }

    fun onPublishersChipStateChange(index: Int) {
        _state.update { current ->
            current.copy(
                publishers = current
                    .publishers
                    .mapIndexed { fieldIndex, field ->
                        return@mapIndexed when {
                            index == fieldIndex -> field.copy(isSelected = true)
                            else -> field.copy(isSelected = false)
                        }
                    },
                publisherError = false,
            )
        }
    }

    fun onSubjectChipStateChange(index: Int) {
        _state.update { current ->
            current.copy(
                subjects = current
                    .subjects
                    .mapIndexed { fieldIndex, field ->
                        return@mapIndexed if (index == fieldIndex) {
                            field.copy(isSelected = field.isSelected.not())
                        } else {
                            field
                        }
                    }
            )
        }
    }

    fun onSave() {
        if (validate()) {
            viewModelScope.launch {
                val bookInputModel = _state.value.toBookInputModel()
                saveBookToLibrary(bookInputModel)
                    .onSuccess {
                        _events.update { current ->
                            current + Event.Success(bookTitle = bookInputModel.title)
                        }
                    }.onFailure {
                        _events.update { current ->
                            current + Event.Error
                        }
                    }
            }
        }
    }

    @Suppress("ComplexCondition")
    private fun validate(): Boolean {
        val currentState = _state.value
        val titleIsValid = currentState.titleState.text.isNotBlank()
        val authorIsValid = currentState.authors.all { it.text.isNotBlank() }
        val publisherIsValid = currentState.publishers.any { it.isSelected }
        val languageIsValid = currentState.languages.any { it.isSelected }
        if (titleIsValid.not() || authorIsValid.not() || publisherIsValid.not() || languageIsValid.not()) {
            _state.update { current ->
                current.copy(
                    titleError = titleIsValid.not(),
                    authorsError = authorIsValid.not(),
                    publisherError = publisherIsValid.not(),
                    languageError = languageIsValid.not(),
                )
            }
        }
        return titleIsValid && authorIsValid && publisherIsValid && languageIsValid
    }

    private fun BookInputState.toBookInputModel(): BookInputModel {
        return BookInputModel(
            key = key,
            title = this.titleState.text,
            authors = this.authors.map {
                it.text
            },
            languages = this.languages.map {
                it.display
            },
            publishers = this.publishers.map {
                it.display
            },
            subjects = this.subjects.map {
                it.display
            }
        )
    }
}

sealed class Event {
    data class Success(val bookTitle: String) : Event()
    data object Error : Event()
}

data class BookInputState(
    val titleState: TextFieldValue,
    val titleError: Boolean,
    val authors: List<TextFieldValue>,
    val authorsError: Boolean,
    val languages: List<ChipState>,
    val languageError: Boolean,
    val publishers: List<ChipState>,
    val publisherError: Boolean,
    val subjects: List<ChipState>
)

data class ChipState(val index: Int, val display: String, val isSelected: Boolean)
