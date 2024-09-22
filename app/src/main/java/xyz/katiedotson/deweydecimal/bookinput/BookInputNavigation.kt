package xyz.katiedotson.deweydecimal.bookinput

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

fun NavGraphBuilder.bookInputScreen() {
    composable<BookInputRoute> {
        val viewModel: BookInputViewModel = hiltViewModel()
        val vmState by viewModel.state.collectAsStateWithLifecycle()
        println(vmState)
        BookInputScreen()
    }
}

fun NavController.navigateToBookInputScreen(isbn: String, navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = BookInputRoute(isbn)) {
        navOptions()
    }
}

@Serializable data class BookInputRoute(val isbn: String)

