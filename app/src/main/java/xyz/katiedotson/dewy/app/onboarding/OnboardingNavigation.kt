package xyz.katiedotson.dewy.app.onboarding

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

fun NavGraphBuilder.onboardingScreen(onNavigateToSearchScreen: () -> Unit) {
    composable<OnboardingRoute> {
        val authViewModel: AuthViewModel = hiltViewModel()
        val authState by authViewModel.authState.collectAsStateWithLifecycle()

        LaunchedEffect(authState) {
            when (authState) {
                AuthViewModel.AuthState.Authenticated -> {
                    onNavigateToSearchScreen()
                }

                else -> {
                    // show screen
                }
            }
        }

        OnboardingScreen(
            onSignInClicked = { email, password -> authViewModel.signIn(email, password) },
            onCreateAccountClicked = { email, password -> authViewModel.createAccount(email, password) }
        )
    }
}

@Serializable
data object OnboardingRoute
