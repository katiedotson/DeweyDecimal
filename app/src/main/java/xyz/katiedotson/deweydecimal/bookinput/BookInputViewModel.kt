package xyz.katiedotson.deweydecimal.bookinput

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import xyz.katiedotson.deweydecimal.book.BookInputRepository
import xyz.katiedotson.deweydecimal.book.BookModel
import javax.inject.Inject

@HiltViewModel
class BookInputViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    bookInputRepository: BookInputRepository
) : ViewModel() {
    private val isbn = savedStateHandle.toRoute<BookInputRoute>().isbn

    private val _state = MutableStateFlow(
        BookInputState(
            bookInputRepository.getBookResult()!!
        )
    )

    val state = _state.asStateFlow()
}

data class BookInputState(
    val model: BookModel
)
