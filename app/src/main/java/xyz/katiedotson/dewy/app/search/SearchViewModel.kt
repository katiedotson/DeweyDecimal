package xyz.katiedotson.dewy.app.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xyz.katiedotson.dewy.model.UserBook
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getUserBooksUseCase: GetUserBooksUseCase,
) : ViewModel() {

    private val _books = MutableStateFlow<List<UserBook>>(listOf())
    val books = _books.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching {
                getUserBooksUseCase()
                    .onSuccess { userBooks ->
                        _books.update {
                            userBooks
                        }
                    }
                    .onFailure { e ->
                        println(e)
                    }
            }.onFailure {
                println(it)
            }
        }
    }
}
