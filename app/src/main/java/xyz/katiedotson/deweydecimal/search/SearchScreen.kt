package xyz.katiedotson.deweydecimal.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import xyz.katiedotson.deweydecimal.ui.theme.BlueFour
import xyz.katiedotson.deweydecimal.ui.theme.BlueOne

@Composable
internal fun SearchScreen(
    onNavigateToCameraScanScreen: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("home screen")
        IconButton(
            onClick = onNavigateToCameraScanScreen,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            colors = IconButtonColors(
                containerColor = BlueFour,
                contentColor = BlueOne,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            )
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new")
        }
    }
}
