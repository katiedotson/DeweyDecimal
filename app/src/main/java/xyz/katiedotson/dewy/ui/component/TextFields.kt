package xyz.katiedotson.dewy.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import xyz.katiedotson.dewy.ui.theme.DeweyDecimalTheme

@Suppress("AnnotationOnSeparateLine")
@Composable
fun DewyTextField(
    onValueChange: (String) -> Unit,
    value: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorText: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        colors = TextFieldDefaults.colors(
            errorContainerColor = MaterialTheme.colorScheme.background,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            disabledContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background
        ),
        isError = isError,
        supportingText = {
            if (errorText != null) {
                Text(
                    text = errorText,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        trailingIcon = trailingIcon,
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = label?.let { { Text(text = label) } },
        placeholder = placeholder?.let { { Text(text = placeholder) } },
        visualTransformation = visualTransformation
    )
}

@PreviewLightDark
@Composable
private fun DewyTextFieldPreview() {
    DeweyDecimalTheme {
        Surface {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Label and placeholder with text", fontWeight = FontWeight.Bold)
                DewyTextField(
                    onValueChange = { _ -> },
                    value = "Here is some text",
                    label = "Label",
                    placeholder = "Placeholder"
                )
                Text(text = "Placeholder only", fontWeight = FontWeight.Bold)
                DewyTextField(
                    onValueChange = { _ -> },
                    value = "",
                    label = null,
                    placeholder = "Placeholder"
                )
            }
        }
    }
}
