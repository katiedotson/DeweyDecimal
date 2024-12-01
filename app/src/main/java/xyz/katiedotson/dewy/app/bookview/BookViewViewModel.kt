package xyz.katiedotson.dewy.app.bookview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.katiedotson.dewy.model.UserBook
import javax.inject.Inject

@HiltViewModel
class BookViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserBookUseCase: GetUserBookUseCase,
) : ViewModel() {
    private val key = savedStateHandle.toRoute<BookViewRoute>().bookId
    private val _state = MutableStateFlow<BookViewViewState>(value = BookViewViewState.Initial)

    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getUserBookUseCase(
                key
            ).onSuccess { result ->
                _state.update {
                    BookViewViewState.Regular(result)
                }
            }.onFailure {
                Timber.e(it)
                _state.update {
                    BookViewViewState.Error
                }
            }
        }
    }

    sealed class BookViewViewState {
        data object Initial : BookViewViewState()
        data class Regular(val book: UserBook) : BookViewViewState()
        data object Error : BookViewViewState()
    }
}
