package xyz.katiedotson.dewy.service.auth

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import xyz.katiedotson.dewy.IoDispatcher
import javax.inject.Inject
import kotlin.coroutines.resume

data class DewyUser(
    val email: String
)

interface AuthService {
    fun loadUser(): Result<DewyUser>
    suspend fun signIn(email: String, password: String): Result<DewyUser>
    suspend fun createAccount(email: String, password: String): Result<DewyUser>
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun provideAuthServiceService(authServiceImpl: AuthServiceImpl): AuthService
}

class AuthServiceImpl @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : AuthService {

    override fun loadUser(): Result<DewyUser> {
        val currentUserEmail = Firebase.auth.currentUser?.email
        return if (currentUserEmail != null) {
            Result.success(DewyUser(currentUserEmail))
        } else {
            Firebase.auth.signOut()
            Result.failure(Throwable("User was unauthenticated"))
        }
    }

    override suspend fun signIn(email: String, password: String): Result<DewyUser> = withContext(dispatcher) {
        return@withContext suspendCancellableCoroutine { continuation ->
            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { _ ->
                    continuation.resume(Result.success(DewyUser(email)))
                }.addOnFailureListener { e ->
                    continuation.resume(Result.failure(e))
                }.addOnCanceledListener {
                    continuation.cancel(cause = null)
                }
        }
    }

    override suspend fun createAccount(email: String, password: String): Result<DewyUser> = withContext(dispatcher) {
        return@withContext suspendCancellableCoroutine { continuation ->
            Firebase.auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener { _ ->
                continuation.resume(Result.success(DewyUser(email)))
            }.addOnFailureListener { e ->
                continuation.resume(Result.failure(e))
            }.addOnCanceledListener {
                continuation.cancel(cause = null)
            }
        }
    }
}
