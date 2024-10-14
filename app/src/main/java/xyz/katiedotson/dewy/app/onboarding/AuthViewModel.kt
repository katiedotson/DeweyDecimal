package xyz.katiedotson.dewy.app.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    getUser: GetUserUseCase,
    private val signInUseCase: SignInUseCase,
    private val createAccountUseCase: CreateAccountUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unknown)
    val authState = _authState.asStateFlow()

    private val _loading = MutableStateFlow(value = false)
    val loading = _loading.asStateFlow()

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
            _loading.update { true }
            signInUseCase(email, password)
                .onSuccess { _ ->
                    _authState.update {
                        AuthState.Authenticated
                    }
                    _loading.update { false }
                }.onFailure { failure ->
                    Timber.e(message = "sign in failed", failure)
                    _loading.update { false }
                    _authState.update {
                        AuthState.Error
                    }
                }
        }
    }

    fun createAccount(email: String, password: String) {
        viewModelScope.launch {
            _loading.update { true }
            createAccountUseCase(email, password)
                .onSuccess { _ ->
                    _authState.update {
                        AuthState.Authenticated
                    }
                    _loading.update { false }
                }.onFailure { failure ->
                    Timber.e(message = "create account failed", failure)
                    _authState.update {
                        AuthState.Error
                    }
                    _loading.update { false }
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
