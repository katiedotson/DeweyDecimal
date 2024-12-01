package xyz.katiedotson.dewy.app.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.katiedotson.dewy.model.UserBook
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getUserBooksUseCase: GetUserBooksUseCase,
) : ViewModel() {

    private val _books = MutableStateFlow<List<UserBook>>(listOf())
    val books = _books.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _loading.update {
                true
            }
            runCatching {
                getUserBooksUseCase()
                    .onSuccess { userBooks ->
                        _books.update {
                            userBooks
                        }
                        _loading.update {
                            false
                        }
                    }
                    .onFailure { e ->
                        Timber.e(e)
                        _loading.update {
                            false
                        }
                    }
            }.onFailure {
                Timber.e(it)
                _loading.update {
                    false
                }
            }
        }
    }
}
