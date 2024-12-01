package xyz.katiedotson.dewy.app.bookview

import xyz.katiedotson.dewy.app.onboarding.GetUserUseCase
import xyz.katiedotson.dewy.model.UserBook
import xyz.katiedotson.dewy.service.book.BookRepository
import javax.inject.Inject

class GetUserBookUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val getUserUseCase: GetUserUseCase,
) {
    suspend operator fun invoke(key: String): Result<UserBook> {
        return getUserUseCase.invoke().mapCatching { user ->
            val userId = user.userId
            bookRepository.getUserBook(bookKey = key, userId = userId).getOrThrow()
        }
    }
}
