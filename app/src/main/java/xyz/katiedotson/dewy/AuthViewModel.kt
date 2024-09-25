package xyz.katiedotson.dewy

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xyz.katiedotson.dewy.auth.AuthService
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authService: AuthService) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unknown)
    val authState = _authState.asStateFlow()

    init {
        authService.loadUser().onSuccess {
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
            authService.signIn(
                email = email,
                password = password
            ).onSuccess { _ ->
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
            authService.createAccount(
                email = email,
                password = password
            ).onSuccess { _ ->
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
