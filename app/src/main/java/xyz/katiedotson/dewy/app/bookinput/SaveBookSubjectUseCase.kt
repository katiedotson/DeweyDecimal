package xyz.katiedotson.dewy.app.bookinput

import xyz.katiedotson.dewy.app.onboarding.GetUserUseCase
import xyz.katiedotson.dewy.model.UserSubject
import xyz.katiedotson.dewy.service.book.BookRepository
import javax.inject.Inject

class SaveBookSubjectUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val getUserUseCase: GetUserUseCase,
) {
    suspend operator fun invoke(name: String): Result<String> {
        return getUserUseCase().mapCatching {
            val userSubject = UserSubject(name = name.trim(), userId = it.userId)
            bookRepository
                .saveSubjectForUser(userSubject)
                .getOrThrow()
        }
    }
}
