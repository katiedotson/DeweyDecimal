package xyz.katiedotson.dewy.app.bookinput

import xyz.katiedotson.dewy.app.onboarding.GetUserUseCase
import xyz.katiedotson.dewy.model.UserSubject
import xyz.katiedotson.dewy.service.book.BookRepository
import javax.inject.Inject

class GetAllBookSubjectsUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val getUserUseCase: GetUserUseCase,
) {

    suspend operator fun invoke(): Result<List<UserSubject>> {
        return getUserUseCase.invoke().mapCatching { user ->
            val userId = user.userId
            bookRepository
                .getBookSubjectsForUser(userId)
                .getOrThrow()
        }
    }
}
