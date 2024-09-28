package xyz.katiedotson.dewy.app.camerascan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xyz.katiedotson.dewy.model.BookModel
import xyz.katiedotson.dewy.service.book.BookRepository
import xyz.katiedotson.dewy.model.key
import javax.inject.Inject

@HiltViewModel
internal class CameraScanViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    private var _match: BookModel? = null
    private val _isbn = MutableStateFlow<String?>(value = null)

    private val _state = MutableStateFlow<CameraScanState>(CameraScanState.Scanning)
    val state = _state.asStateFlow()

    private val _events = MutableStateFlow<List<Event>>(listOf())
    val events = _events.asStateFlow()

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
                            _match = value
                            _state.update {
                                CameraScanState.MatchFound(match = value)
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

    fun onBookResultConfirmed() {
        viewModelScope.launch {
            bookRepository.saveBookResult(_match!!)
                .onSuccess {
                    _events.update { current ->
                        current + Event.BookResultConfirmed(
                            bookId = it.key
                        )
                    }
                }
                .onFailure {
                    println("something went wrong with firebase")
                }
        }
    }

    fun eventHandled(event: Event) {
        _events.update { current ->
            current - event
        }
    }
}

internal sealed class CameraScanState {
    data object Scanning : CameraScanState()
    data object Loading : CameraScanState()
    data class MatchFound(val match: BookModel) : CameraScanState()
    data object MatchNotFound : CameraScanState()
}

internal sealed class Event {
    data class BookResultConfirmed(val bookId: String) : Event()
}
