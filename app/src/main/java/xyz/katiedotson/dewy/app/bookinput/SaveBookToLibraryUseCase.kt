package xyz.katiedotson.dewy.app.bookinput

import xyz.katiedotson.dewy.app.onboarding.GetUserUseCase
import xyz.katiedotson.dewy.model.UserBook
import xyz.katiedotson.dewy.service.book.BookRepository
import javax.inject.Inject

class SaveBookToLibraryUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val getUserUseCase: GetUserUseCase,
) {
    suspend operator fun invoke(bookInput: BookInputModel): Result<Unit> {
        return getUserUseCase.invoke().mapCatching { user ->
            val userId = user.userId
            bookRepository.saveBookForUser(
                userBook = UserBook(
                    userId = userId,
                    title = bookInput.title,
                    authors = bookInput.authors,
                    languages = bookInput.languages,
                    publishers = bookInput.publishers,
                    subjects = bookInput.subjects
                )
            )
        }
    }
}
