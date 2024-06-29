package xyz.katiedotson.deweydecimal

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.katiedotson.deweydecimal.navigation.BottomNavBar
import xyz.katiedotson.deweydecimal.ui.theme.DeweyDecimalTheme

data class AppState(
    val selectedNavItem: NavItem,
)

sealed class NavItem {
    object Search : NavItem()
    object Add : NavItem()
}

@Composable
fun MainApplication() {
    var selectedNavItem by remember { mutableStateOf<NavItem>(NavItem.Search) }
    MainApplicationComponent(
        state = AppState(
            selectedNavItem = selectedNavItem,
        ),
        onNavItemClicked = {
            selectedNavItem = it
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainApplicationComponent(
    state: AppState,
    onNavItemClicked: (NavItem) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedNavItem = state.selectedNavItem,
                onAddClicked = { onNavItemClicked(NavItem.Add) },
                onSearchClicked = { onNavItemClicked(NavItem.Search) }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Crossfade(
                targetState = state.selectedNavItem,
                label = "main_cross_fade",
                animationSpec = spring(stiffness = Spring.StiffnessLow)
            ) {
                when (it) {
                    NavItem.Add -> {
                        Text(text = "Add")
                    }

                    NavItem.Search -> {
                        Text(text = "Search")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainApplicationPreview() {
    DeweyDecimalTheme {
        MainApplication()
    }
}
