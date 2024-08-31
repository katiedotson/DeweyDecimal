package xyz.katiedotson.deweydecimal

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import xyz.katiedotson.deweydecimal.add.AddNewItem
import xyz.katiedotson.deweydecimal.add.CameraScanViewModel
import xyz.katiedotson.deweydecimal.ui.theme.DeweyDecimalTheme

private const val AnimationDuration = 300

@Composable
fun MainComponent() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavItem.Search.route
    ) {
        composable(NavItem.Search.route) {
            SearchScreen(
                navController
            )
        }
        // TODO: only navigate if location permissions are enabled
        composable(
            NavItem.Add.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = AnimationDuration,
                        easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(
                        durationMillis = AnimationDuration,
                        easing = EaseIn
                    ),
                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                )
            }
        ) {
            val cameraScanViewModel: CameraScanViewModel = viewModel()
            Scaffold {
                Column(modifier = Modifier.padding(it)) {
                    AddNewItem(
                        onTextDetected = cameraScanViewModel::textDetected,
                        onTorchButtonClicked = cameraScanViewModel::updateTorchEnabled,
                        torchEnabledFlow = cameraScanViewModel.torchFlow
                    )
                }
            }
        }
    }
}

@Composable
fun SearchScreen(navController: NavHostController) {
    Scaffold(
        floatingActionButton = {
            IconButton(onClick = { navController.navigate(NavItem.Add.route) }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new")
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Text("home screen")
        }
    }
}

sealed class NavItem(val route: String) {
    object Search : NavItem("search")
    object Add : NavItem("add")
}

@Preview(showBackground = true)
@Composable
private fun MainApplicationPreview() {
    DeweyDecimalTheme {
        MainComponent()
    }
}
