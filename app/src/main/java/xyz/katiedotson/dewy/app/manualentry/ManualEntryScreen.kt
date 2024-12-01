package xyz.katiedotson.dewy.app.manualentry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import kotlinx.coroutines.CoroutineScope
import xyz.katiedotson.dewy.ui.SearchResultBottomSheetContent
import xyz.katiedotson.dewy.ui.component.DewyTextField
import xyz.katiedotson.dewy.ui.component.Loader
import xyz.katiedotson.dewy.ui.theme.AppTypography

private const val MaxIsbnLength = 13

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ManualEntryScreen(
    viewState: ManualEntryScreenState,
    sheetState: SheetState,
    scope: CoroutineScope,
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Loader(viewState.loading)
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .padding(bottom = 24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MainContent(
                viewState = viewState
            )
        }
    }
    if (viewState.bottomSheetState != null) {
        ModalBottomSheet(
            onDismissRequest = viewState.bottomSheetState.onBottomSheetDismissed,
            sheetState = sheetState
        ) {
            SearchResultBottomSheetContent(
                bottomSheetState = viewState.bottomSheetState,
                sheetState = sheetState,
                scope = scope,
            )
        }
    }
}

@Composable
private fun ColumnScope.MainContent(
    viewState: ManualEntryScreenState,
) {
    var isbnValue by remember { mutableStateOf(value = "") }
    Heading(
        heading = viewState.heading,
        onBackClicked = viewState.onNavigateBack
    )
    DewyTextField(
        label = "ISBN",
        modifier = Modifier
            .widthIn(max = 400.dp)
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        value = isbnValue,
        onValueChange = {
            isbnValue = it.takeIf { it.isDigitsOnly() && it.length <= MaxIsbnLength } ?: isbnValue
            viewState.onClearError()
        },
        isError = viewState.isbnError != null,
        errorText = viewState.isbnError,
    )
    Spacer(modifier = Modifier.weight(1f))
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.inverseSurface
        ),
        modifier = Modifier
            .widthIn(max = 400.dp)
            .fillMaxWidth()
            .padding(top = 24.dp),
        onClick = { viewState.onSubmit(isbnValue) },
    ) {
        Text(
            text = viewState.submitButtonText,
            style = AppTypography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun Heading(
    heading: String,
    onBackClicked: () -> Unit
) {
    Row {
        IconButton(
            onBackClicked,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .size(32.dp)
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
    Text(
        modifier = Modifier.padding(bottom = 8.dp),
        text = heading,
        style = AppTypography.displaySmall
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@PreviewLightDark
@PreviewScreenSizes
fun ManualEntryScreenPreview() {
    ManualEntryScreen(
        viewState = ManualEntryScreenState(
            loading = false,
            heading = "Enter the ISBN",
            onNavigateBack = {},
            submitButtonText = "Search",
            onSubmit = { _ -> },
            onClearError = {},
            isbnError = null,
            bottomSheetState = null,
        ),
        sheetState = rememberModalBottomSheetState(),
        scope = rememberCoroutineScope()
    )
}
