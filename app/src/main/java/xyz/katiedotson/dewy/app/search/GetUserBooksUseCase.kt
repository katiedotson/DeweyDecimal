package xyz.katiedotson.dewy.app.search

import xyz.katiedotson.dewy.app.onboarding.GetUserUseCase
import xyz.katiedotson.dewy.model.UserBook
import xyz.katiedotson.dewy.service.book.BookRepository
import javax.inject.Inject

class GetUserBooksUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val getUserUseCase: GetUserUseCase,
) {
    suspend operator fun invoke(): Result<List<UserBook>> {
        return getUserUseCase.invoke().mapCatching { user ->
            val userId = user.userId
            bookRepository
                .getUserBooks(userId)
                .getOrDefault(emptyList())
        }
    }
}
