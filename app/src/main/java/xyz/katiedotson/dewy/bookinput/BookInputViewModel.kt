package xyz.katiedotson.dewy.bookinput

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
import xyz.katiedotson.dewy.book.BookRepository
import javax.inject.Inject

@HiltViewModel
class BookInputViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository
) : ViewModel() {

    private val bookId = savedStateHandle.toRoute<BookInputRoute>().bookId

    init {
        viewModelScope.launch {
            bookRepository.getByKey(
                bookId
            ).onSuccess { book ->
                _state.update {
                    BookInputState(
                        titleState = TextFieldValue(book.title),
                        authors = book.authors.map { authorModel ->
                            TextFieldValue(authorModel.fullName)
                        },
                        languages = book.languages.mapIndexed { index, languageModel ->
                            ChipState(
                                index = index,
                                display = languageModel.abbreviation,
                                isSelected = true,
                            )
                        },
                        publishers = book.publishers.mapIndexed { index, publisherModel ->
                            ChipState(
                                index = index,
                                display = publisherModel.publisherName,
                                isSelected = index == 0,
                            )
                        },
                        subjects = book.subjects.mapIndexed { index, subjectModel ->
                            ChipState(
                                index = index,
                                display = subjectModel.subjectName,
                                isSelected = true,
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
            titleState = TextFieldValue(text = ""),
            authors = listOf(),
            languages = listOf(),
            publishers = listOf(),
            subjects = listOf(),
        )
    )

    val state = _state.asStateFlow()

    fun onTitleValueChange(textFieldValue: TextFieldValue) {
        _state.update { current ->
            current.copy(titleState = textFieldValue)
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
                    }
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
                authors = current.authors.filterIndexed { fieldIndex, _ -> index != fieldIndex }
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
                    }
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
                    }
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
}

data class BookInputState(
    val titleState: TextFieldValue,
    val authors: List<TextFieldValue>,
    val languages: List<ChipState>,
    val publishers: List<ChipState>,
    val subjects: List<ChipState>
)

data class ChipState(val index: Int, val display: String, val isSelected: Boolean)
