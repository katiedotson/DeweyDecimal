package xyz.katiedotson.dewy.app.bookview

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

fun NavGraphBuilder.bookViewScreen(
    onNavigateBack: () -> Unit,
) {
    composable<BookViewRoute> {
        val viewModel: BookViewViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        when (val _state = state) {
            is BookViewViewModel.BookViewViewState.Initial -> {

            }
            is BookViewViewModel.BookViewViewState.Error -> {
                BookViewScreenErrorState(
                    onNavigateBack = onNavigateBack
                )
            }
            is BookViewViewModel.BookViewViewState.Regular -> {
                BookViewScreen(
                    book = _state.book,
                    onNavigateBack = onNavigateBack
                )
            }
        }
    }
}

fun NavController.navigateToBookViewScreen(bookId: String, navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = BookViewRoute(bookId)) {
        navOptions()
    }
}

@Serializable
data class BookViewRoute(val bookId: String)
