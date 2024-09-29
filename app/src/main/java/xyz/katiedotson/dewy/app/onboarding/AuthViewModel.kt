package xyz.katiedotson.dewy.app.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xyz.katiedotson.dewy.service.auth.AuthService
import xyz.katiedotson.dewy.service.auth.DewyUser
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    getUser: GetUserUseCase,
    private val signInUseCase: SignInUseCase,
    private val createAccountUseCase: CreateAccountUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unknown)
    val authState = _authState.asStateFlow()

    init {
        getUser()
            .onSuccess {
                _authState.update {
                    AuthState.Authenticated
                }
            }.onFailure {
                _authState.update {
                    AuthState.Unauthenticated
                }
            }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            signInUseCase(email, password)
                .onSuccess { _ ->
                    _authState.update {
                        AuthState.Authenticated
                    }
                }.onFailure { failure ->
                    Log.e("AuthViewModel", "sign in failed", failure)
                    _authState.update {
                        AuthState.Error
                    }
                }
        }
    }

    fun createAccount(email: String, password: String) {
        viewModelScope.launch {
            createAccountUseCase(email, password)
                .onSuccess { _ ->
                    _authState.update {
                        AuthState.Authenticated
                    }
                }.onFailure { failure ->
                    Log.e("AuthViewModel", "create account failed", failure)
                    _authState.update {
                        AuthState.Error
                    }
                }
        }
    }

    sealed class AuthState {
        data object Unknown : AuthState()
        data object Authenticated : AuthState()
        data object Unauthenticated : AuthState()
        data object Error : AuthState()
    }
}

class GetUserUseCase @Inject constructor(private val authService: AuthService) {
    operator fun invoke(): Result<DewyUser> {
        return authService.loadUser()
    }
}

class SignInUseCase @Inject constructor(private val authService: AuthService) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ): Result<DewyUser> {
        return authService.signIn(
            email = email,
            password = password
        )
    }
}

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
