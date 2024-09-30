package xyz.katiedotson.dewy.app.onboarding

import xyz.katiedotson.dewy.service.auth.AuthService
import xyz.katiedotson.dewy.service.auth.DewyUser
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(private val authService: AuthService) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<DewyUser> {
        return authService.createAccount(
            email = email,
            password = password
        )
    }
}
