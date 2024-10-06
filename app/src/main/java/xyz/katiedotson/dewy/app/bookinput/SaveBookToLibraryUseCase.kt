package xyz.katiedotson.dewy.app.bookinput

import xyz.katiedotson.dewy.app.onboarding.GetUserUseCase
import xyz.katiedotson.dewy.model.UserBook
import xyz.katiedotson.dewy.service.book.BookRepository
import javax.inject.Inject

class SaveBookToLibraryUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val getUserUseCase: GetUserUseCase,
) {
    suspend operator fun invoke(bookInput: SavedBookInput): Result<Unit> {
        return getUserUseCase.invoke().mapCatching { user ->
            val userId = user.userId
            bookRepository.saveBookForUser(
                userBook = UserBook(
                    key = bookInput.key,
                    userId = userId,
                    title = bookInput.title,
                    authors = bookInput.authors,
                    languages = bookInput.languages,
                    publisher = bookInput.publisher,
                    subjects = bookInput.subjects
                )
            )
        }
    }
}

class SavedBookInput(
    val key: String,
    val title: String,
    val authors: List<String>,
    val languages: List<String>,
    val publisher: String,
    val subjects: List<String>,
)
