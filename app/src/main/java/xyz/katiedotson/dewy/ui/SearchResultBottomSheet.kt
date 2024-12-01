package xyz.katiedotson.dewy.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xyz.katiedotson.dewy.ui.theme.AppTypography
import xyz.katiedotson.dewy.ui.theme.DeweyDecimalTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultBottomSheetContent(
    bottomSheetState: SearchResultBottomSheetState?,
    sheetState: SheetState,
    scope: CoroutineScope,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 12.dp)
            .padding(bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (bottomSheetState is SearchResultBottomSheetState.MatchNotFound) {
            Text(bottomSheetState.heading, style = AppTypography.headlineMedium)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text(text = "We couldn't find what you were looking for.", style = AppTypography.bodyMedium)
            Row(
                modifier = Modifier.padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(
                    onClick = {
                        scope
                            .launch { sheetState.hide() }
                            .invokeOnCompletion {
                                if (sheetState.isVisible.not()) {
                                    bottomSheetState.onBottomSheetDismissed()
                                }
                            }
                    }
                ) {
                    Text(text = "Try Again", style = AppTypography.labelLarge)
                }
                bottomSheetState.onTryManually?.let {
                    Button(
                        onClick = bottomSheetState.onTryManually
                    ) {
                        Text(text = "Search Manually", style = AppTypography.labelLarge)
                    }
                }
            }
        }
        if (bottomSheetState is SearchResultBottomSheetState.MatchFound) {
            Text(text = bottomSheetState.heading, style = AppTypography.headlineMedium)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text(text = bottomSheetState.title, style = AppTypography.bodyLarge)
            Text(text = bottomSheetState.author, style = AppTypography.bodyMedium)
            Row(
                modifier = Modifier.padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(
                    onClick = {
                        scope
                            .launch { sheetState.hide() }
                            .invokeOnCompletion {
                                if (sheetState.isVisible.not()) {
                                    bottomSheetState.onBottomSheetDismissed()
                                }
                            }
                    }
                ) {
                    Text(text = "Try Again", style = AppTypography.labelLarge)
                }
                Button(
                    onClick = bottomSheetState.onMatchConfirmed
                ) {
                    Text(text = bottomSheetState.confirmationButtonText, style = AppTypography.labelLarge)
                }
            }
        }
    }
}

sealed class SearchResultBottomSheetState(
    open val onBottomSheetDismissed: () -> Unit
) {
    data class MatchFound(
        val heading: String,
        val title: String,
        val author: String,
        val confirmationButtonText: String,
        val onMatchConfirmed: () -> Unit,
        override val onBottomSheetDismissed: () -> Unit,
    ) : SearchResultBottomSheetState(onBottomSheetDismissed)
    data class MatchNotFound(
        val heading: String,
        val onTryManually: (() -> Unit)?,
        override val onBottomSheetDismissed: () -> Unit,
    ) : SearchResultBottomSheetState(onBottomSheetDismissed)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@PreviewLightDark
fun PreviewSearchResultBottomSheetMatchFound() {
    DeweyDecimalTheme {
        Surface {
            SearchResultBottomSheetContent(
                bottomSheetState = SearchResultBottomSheetState.MatchFound(
                    heading = "Match Found",
                    title = "Paradise Lost",
                    author = "John Milton",
                    onBottomSheetDismissed = {},
                    confirmationButtonText = "Confirm",
                    onMatchConfirmed = {},
                ),
                scope = rememberCoroutineScope(),
                sheetState = rememberModalBottomSheetState()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@PreviewLightDark
fun PreviewSearchResultBottomSheetNoMatchFound() {
    DeweyDecimalTheme {
        Surface {
            SearchResultBottomSheetContent(
                bottomSheetState = SearchResultBottomSheetState.MatchNotFound(
                    heading = "Match Not Found",
                    onTryManually = {},
                    onBottomSheetDismissed = {},
                ),
                sheetState = rememberModalBottomSheetState(),
                scope = rememberCoroutineScope()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@PreviewLightDark
fun PreviewSearchResultBottomSheetNoMatchFoundNoManualOption() {
    DeweyDecimalTheme {
        Surface {
            SearchResultBottomSheetContent(
                bottomSheetState = SearchResultBottomSheetState.MatchNotFound(
                    heading = "Match Not Found",
                    onTryManually = null,
                    onBottomSheetDismissed = {},
                ),
                sheetState = rememberModalBottomSheetState(),
                scope = rememberCoroutineScope()
            )
        }
    }
}
