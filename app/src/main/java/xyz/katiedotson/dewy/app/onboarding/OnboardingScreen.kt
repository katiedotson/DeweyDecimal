package xyz.katiedotson.dewy.app.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import xyz.katiedotson.dewy.R
import xyz.katiedotson.dewy.ui.component.DewyTextField
import xyz.katiedotson.dewy.ui.component.Loader
import xyz.katiedotson.dewy.ui.theme.AppTypography
import xyz.katiedotson.dewy.ui.theme.DeweyDecimalTheme

@Composable
internal fun OnboardingScreen(
    onSignInClicked: (String, String) -> Unit,
    onCreateAccountClicked: (String, String) -> Unit,
    loading: Boolean,
) {
    var email by remember { mutableStateOf(value = "") }
    var password by remember { mutableStateOf(value = "") }

    Loader(isVisible = loading)

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(R.drawable.books_stock),
                    contentScale = ContentScale.Crop,
                    alpha = 0.2f
                )
                .padding(16.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MainContent(
                email = email,
                emailLabel = "Email",
                onEmailChanged = { newText -> email = newText },
                password = password,
                passwordLabel = "Password",
                onPasswordChanged = { newText -> password = newText },
                onCreateAccountClicked = {
                    onCreateAccountClicked(
                        email,
                        password
                    )
                },
                onSignInClicked = {
                    onSignInClicked(
                        email,
                        password
                    )
                }
            )
        }
    }
}

@Composable
private fun ColumnScope.MainContent(
    email: String,
    emailLabel: String,
    onEmailChanged: (String) -> Unit,
    password: String,
    passwordLabel: String,
    onPasswordChanged: (String) -> Unit,
    onCreateAccountClicked: () -> Unit,
    onSignInClicked: () -> Unit,
) {
    var showPassword by remember { mutableStateOf(value = false) }

    Spacer(
        modifier = Modifier.padding(16.dp)
    )

    Heading()

    Spacer(
        modifier = Modifier.padding(16.dp)
    )

    DewyTextField(
        modifier = Modifier.widthIn(max = 400.dp),
        value = email,
        onValueChange = onEmailChanged,
        label = emailLabel
    )

    Spacer(
        modifier = Modifier.padding(16.dp)
    )

    DewyTextField(
        modifier = Modifier.widthIn(max = 400.dp),
        value = password,
        onValueChange = onPasswordChanged,
        trailingIcon = {
            IconButton(
                onClick = { showPassword = showPassword.not() }
            ) {
                AnimatedVisibility(visible = showPassword, enter = fadeIn(), exit = fadeOut()) {
                    Icon(painter = painterResource(R.drawable.visibility_off), contentDescription = "Hide Password")
                }
                AnimatedVisibility(visible = showPassword.not(), enter = fadeIn(), exit = fadeOut()) {
                    Icon(painter = painterResource(R.drawable.visibility_on), contentDescription = "Show Password")
                }
            }
        },
        label = passwordLabel,
        visualTransformation = if (showPassword.not()) PasswordVisualTransformation() else VisualTransformation.None
    )

    Spacer(
        modifier = Modifier.weight(1f)
    )

    Buttons(
        onSignInClicked = onSignInClicked,
        onCreateAccountClicked = onCreateAccountClicked
    )
}

@Composable
private fun Heading() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Welcome to Dewy",
        textAlign = TextAlign.Center,
        style = AppTypography.displaySmall,
        color = MaterialTheme.colorScheme.onBackground
    )

    Box(
        modifier = Modifier
            .height(2.dp)
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(MaterialTheme.colorScheme.onBackground)
    )

    Spacer(
        modifier = Modifier.padding(4.dp)
    )

    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "To continue, please sign in or sign up.",
        textAlign = TextAlign.Center,
        style = AppTypography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun Buttons(
    onSignInClicked: () -> Unit,
    onCreateAccountClicked: () -> Unit,
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.inverseSurface
        ),
        modifier = Modifier.widthIn(max = 400.dp).fillMaxWidth(),
        onClick = onSignInClicked
    ) {
        Text(text = "Sign In", style = AppTypography.bodyLarge, fontWeight = FontWeight.Bold)
    }

    Spacer(
        modifier = Modifier.padding(8.dp)
    )

    OutlinedButton(
        modifier = Modifier.widthIn(max = 400.dp).fillMaxWidth(),
        onClick = onCreateAccountClicked
    ) {
        Text(text = "Create Account")
    }
}

@Composable
@PreviewLightDark
@PreviewScreenSizes
private fun OnboardingScreenPreview() {
    DeweyDecimalTheme {
        OnboardingScreen(
            loading = false,
            onSignInClicked = { _, _ -> },
            onCreateAccountClicked = { _, _ -> },
        )
    }
}
