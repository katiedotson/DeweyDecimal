package xyz.katiedotson.dewy.app.camerascan

import xyz.katiedotson.dewy.app.bookview.GetUserBookUseCase
import xyz.katiedotson.dewy.model.BookSearchResult
import xyz.katiedotson.dewy.model.key
import xyz.katiedotson.dewy.service.book.BookRepository
import javax.inject.Inject

class GetBookResultUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val getUserBookUseCase: GetUserBookUseCase,
) {
    suspend operator fun invoke(isbn: String): Result<GetBookResult> {
        return bookRepository
            .getByIsbn(isbn)
            .mapCatching { bookSearchResult ->
                return searchForAlreadySavedBook(bookSearchResult)
            }
    }

    private suspend fun searchForAlreadySavedBook(bookSearchResult: BookSearchResult): Result<GetBookResult> {
        return getUserBookUseCase(bookSearchResult.key)
            .fold(
                onSuccess = {
                    Result.success(GetBookResult.BookAlreadySaved(bookSearchResult))
                },
                onFailure = {
                    Result.success(GetBookResult.BookFound(bookSearchResult))
                }
            )
    }
}

sealed class GetBookResult {
    data class BookFound(val bookSearchResult: BookSearchResult) : GetBookResult()
    data class BookAlreadySaved(val bookSearchResult: BookSearchResult) : GetBookResult()
}
