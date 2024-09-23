package xyz.katiedotson.deweydecimal.bookinput

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import xyz.katiedotson.deweydecimal.book.BookInputRepository
import javax.inject.Inject

@HiltViewModel
class BookInputViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    bookInputRepository: BookInputRepository
) : ViewModel() {
    // TODO: keep the scan result in BookInputRepository using some type of persistence until this is completed
    private val isbn = savedStateHandle.toRoute<BookInputRoute>().isbn

    private val book = bookInputRepository.getBookResult()!!

    private val _state = MutableStateFlow(
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
