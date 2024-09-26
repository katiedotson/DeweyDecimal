package xyz.katiedotson.dewy.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.katiedotson.dewy.R
import xyz.katiedotson.dewy.component.DewyTextField
import xyz.katiedotson.dewy.ui.theme.AppTypography
import xyz.katiedotson.dewy.ui.theme.DeweyDecimalTheme

@Composable
internal fun OnboardingScreen(
    onSignInClicked: (String, String) -> Unit,
    onCreateAccountClicked: (String, String) -> Unit
) {
    var email by remember { mutableStateOf(value = "") }
    var password by remember { mutableStateOf(value = "") }

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
        ) {

            Spacer(
                modifier = Modifier.padding(16.dp)
            )

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

            Spacer(
                modifier = Modifier.padding(16.dp)
            )

            DewyTextField(
                value = email,
                onValueChange = { newText -> email = newText },
                label = "Email"
            )

            Spacer(
                modifier = Modifier.padding(16.dp)
            )

            DewyTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { newText -> password = newText },
                label = "Password",
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inverseSurface
                ),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onSignInClicked(
                        email,
                        password
                    )
                }
            ) {
                Text(text = "Sign In", style = AppTypography.bodyLarge, fontWeight = FontWeight.Bold)
            }

            Spacer(
                modifier = Modifier.padding(8.dp)
            )

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onCreateAccountClicked(
                        email,
                        password
                    )
                }
            ) {
                Text(text = "Create Account")
            }
        }
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun OnboardingScreenPreviewLightMode() {
    DeweyDecimalTheme {
        OnboardingScreen(
            onSignInClicked = { _, _ -> },
            onCreateAccountClicked = { _, _ -> },
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun OnboardingScreenPreviewDarkMode() {
    DeweyDecimalTheme {
        OnboardingScreen(
            onSignInClicked = { _, _ -> },
            onCreateAccountClicked = { _, _ -> },
        )
    }
}
