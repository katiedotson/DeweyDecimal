package xyz.katiedotson.deweydecimal.camerascan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xyz.katiedotson.deweydecimal.book.BookModel
import xyz.katiedotson.deweydecimal.book.BookRepository
import javax.inject.Inject

@HiltViewModel
internal class CameraScanViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _isbn = MutableStateFlow<String?>(value = null)

    private val _state = MutableStateFlow<CameraScanState>(CameraScanState.Scanning)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _isbn
                .filterNotNull()
                .collect { isbn ->
                    _state.update {
                        CameraScanState.Loading
                    }
                    val result: Result<BookModel> = bookRepository.getByIsbn(isbn)
                    result
                        .onSuccess { value ->
                            _state.update {
                                CameraScanState.MatchFound(value)
                            }
                        }
                        .onFailure { e ->
                            println(e)
                            _state.update {
                                CameraScanState.MatchNotFound
                            }
                        }
                }
        }
    }

    @Suppress("MagicNumber")
    fun textDetected(text: DetectedText) {
        if (_state.value != CameraScanState.Scanning) return
        text.blocks
            .firstOrNull { string ->
                string.startsWith(prefix = "ISBN") &&
                    (string.filterIsbn().length == 13 || string.filterIsbn().length == 10)
            }
            ?.let { string ->
                _isbn
                    .update {
                        string.filterIsbn()
                    }
            }
    }

    fun unpause() {
        _state.update {
            CameraScanState.Scanning
        }
    }

    private fun String.filterIsbn(): String {
        return this
            .replace(oldValue = "ISBN-13", newValue = "")
            .replace(oldValue = "ISBN-10", newValue = "")
            .filter { it.isDigit() }
    }
}

internal sealed class CameraScanState {
    data object Scanning : CameraScanState()
    data object Loading : CameraScanState()
    data class MatchFound(val match: BookModel) : CameraScanState()
    data object MatchNotFound : CameraScanState()
}
