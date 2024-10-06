package xyz.katiedotson.dewy.app.manualentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xyz.katiedotson.dewy.model.BookSearchResult
import xyz.katiedotson.dewy.model.key
import xyz.katiedotson.dewy.service.book.BookRepository
import javax.inject.Inject

@HiltViewModel
class ManualEntryViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ManualEntryState>(ManualEntryState.Default)
    val state = _state.asStateFlow()

    private val _events = MutableStateFlow<List<Event>>(listOf())
    val events = _events.asStateFlow()

    fun onSubmit(isbn: String) {
        if (isbn.isBlank()) {
            _state.update {
                ManualEntryState.FieldError
            }
            return
        }
        viewModelScope.launch {
            val result: Result<BookSearchResult> = bookRepository.getByIsbn(isbn)
            result
                .onSuccess { value ->
                    _state.update {
                        ManualEntryState.MatchFound(match = value)
                    }
                }
                .onFailure { e ->
                    println(e)
                    _state.update {
                        ManualEntryState.MatchNotFound
                    }
                }
        }
    }

    fun bookResultConfirmed() {
        (_state.value as? ManualEntryState.MatchFound)?.let {
            val match = it.match
            viewModelScope.launch {
                bookRepository.saveBookResult(match)
                    .onSuccess {
                        _events.update { events ->
                            events + Event.MatchConfirmed(match.key)
                        }
                    }.onFailure {
                        println("something went wrong with firebase")
                    }
            }
            return
        }
        println("Book result confirmed when no match exists")
    }

    fun reset() {
        _state.update {
            ManualEntryState.Default
        }
    }

    fun eventHandled(event: Event) {
        _events.update { current ->
            current - event
        }
    }
}

sealed class Event {
    data class MatchConfirmed(val key: String) : Event()
}

sealed class ManualEntryState {
    data object Default : ManualEntryState()
    data object FieldError : ManualEntryState()
    data class MatchFound(val match: BookSearchResult) : ManualEntryState()
    data object MatchNotFound : ManualEntryState()
}
