package xyz.katiedotson.dewy.app.bookinput

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import xyz.katiedotson.dewy.ui.component.DewyTextField
import xyz.katiedotson.dewy.ui.theme.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSubjectsBottomSheet(
    onDismiss: () -> Unit,
    onSubjectTextFieldChanged: (String) -> Unit,
    filteredSubjects: ImmutableList<ChipViewState>,
    onSaveSubject: (String) -> Unit,
    onSubjectSelected: (Int) -> Unit,
) {
    var subjectsText by remember { mutableStateOf(value = "") }
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
            subjectsText = ""
            onSubjectTextFieldChanged("")
        }
    ) {
        BottomSheetContent(
            filteredSubjects = filteredSubjects,
            subjectsText = subjectsText,
            onTextFieldChange = {
                subjectsText = it
                onSubjectTextFieldChanged(it)
            },
            onSaveSubject = onSaveSubject,
            onSubjectSelected = onSubjectSelected,
            onDismiss = {
                onDismiss()
                subjectsText = ""
                onSubjectTextFieldChanged("")
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun BottomSheetContent(
    filteredSubjects: ImmutableList<ChipViewState>,
    subjectsText: String,
    onTextFieldChange: (String) -> Unit,
    onSubjectSelected: (Int) -> Unit,
    onSaveSubject: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .padding(bottom = 48.dp)
    ) {
        Text(
            modifier = Modifier.padding(top = 24.dp),
            text = "Manage Subjects",
            style = AppTypography.titleMedium,
        )
        Text(
            text = "Add this book to existing subjects, or add new subjects",
            style = AppTypography.titleSmall,
        )
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        DewyTextField(
            onValueChange = onTextFieldChange,
            value = subjectsText,
            placeholder = "Search for existing subjects, or add a new one...",
            label = "Subject name"
        )
        if (filteredSubjects.isEmpty()) {
            AddNewSubjectButton(
                saveButtonText = "Save new subject",
                onSaveSubject = {
                    onSaveSubject(subjectsText)
                }
            )
        } else {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                filteredSubjects.forEachIndexed { index, chipViewState ->
                    FilterChip(
                        selected = chipViewState.isSelected,
                        onClick = { onSubjectSelected(index) },
                        leadingIcon = {
                            if (chipViewState.isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected"
                                )
                            }
                        },
                        label = {
                            Text(text = chipViewState.display, style = AppTypography.labelMedium)
                        }
                    )
                }
            }
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                onClick = onDismiss,
            ) {
                Text(
                    text = "Done",
                    style = AppTypography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun AddNewSubjectButton(
    onSaveSubject: () -> Unit,
    saveButtonText: String,
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.inverseSurface,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        onClick = onSaveSubject,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.padding(horizontal = 16.dp))
        Text(
            text = saveButtonText,
            style = AppTypography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}
