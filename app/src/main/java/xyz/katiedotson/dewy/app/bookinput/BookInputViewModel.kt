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

@Suppress("TooManyFunctions")
@HiltViewModel
class BookInputViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBookInputModel: GetBookInputModelUseCase,
    private val saveBookToLibrary: SaveBookToLibraryUseCase,
    private val getAllBookSubjects: GetAllBookSubjectsUseCase,
    private val saveSubject: SaveBookSubjectUseCase,
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
                    )
                }
            }.onFailure { e ->
                println(e)
            }
        }
        viewModelScope.launch {
            getAllBookSubjects()
                .onSuccess {
                    _allBookSubjects.update {
                        it
                    }
                    _bookSubjects.update {
                        it
                    }
                }.onFailure {
                    println(it)
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
        )
    )
    val state = _state.asStateFlow()

    private val _allBookSubjects = MutableStateFlow<List<ChipState>>(listOf())
    private val _bookSubjects = MutableStateFlow<List<ChipState>>(listOf())
    val bookSubjects = _bookSubjects.asStateFlow()

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
        _bookSubjects.update { current ->
            current.mapIndexed { fieldIndex, field ->
                return@mapIndexed if (index == fieldIndex) {
                    field.copy(isSelected = field.isSelected.not())
                } else {
                    field
                }
            }
        }
    }

    fun onCustomSubjectsFieldChanged(value: String) {
        _bookSubjects.update {
            _allBookSubjects.value.filter {
                it.display.contains(value)
            }
        }
    }

    fun onSaveSubject(subjectName: String) {
        viewModelScope.launch {
            saveSubject(subjectName)
                .onSuccess {
                    _allBookSubjects.update { current ->
                        current.plus(
                            ChipState(
                                index = current.size,
                                display = subjectName,
                                isSelected = true
                            )
                        )
                    }
                }
                .onFailure {
                    it.printStackTrace()
                    _events.update { current ->
                        current + Event.Error
                    }
                }
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
            subjects = listOf()
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
)

data class ChipState(val index: Int, val display: String, val isSelected: Boolean)
