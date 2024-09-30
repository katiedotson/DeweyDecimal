package xyz.katiedotson.dewy.app.onboarding

import xyz.katiedotson.dewy.service.auth.AuthService
import xyz.katiedotson.dewy.service.auth.DewyUser
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val authService: AuthService) {
    operator fun invoke(): Result<DewyUser> {
        return authService.loadUser()
    }
}
