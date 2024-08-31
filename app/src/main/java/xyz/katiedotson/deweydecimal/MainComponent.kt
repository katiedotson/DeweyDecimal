package xyz.katiedotson.deweydecimal

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import xyz.katiedotson.deweydecimal.scan.CameraScanViewModel
import xyz.katiedotson.deweydecimal.scan.ScanNewItem
import xyz.katiedotson.deweydecimal.ui.theme.BlueFour
import xyz.katiedotson.deweydecimal.ui.theme.BlueOne
import xyz.katiedotson.deweydecimal.ui.theme.DeweyDecimalTheme

private const val AnimationDuration = 300

@Composable
fun MainComponent() {
    val navController = rememberNavController()
    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            NavHost(
                navController = navController,
                startDestination = NavItem.Search.route
            ) {
                composable(route = NavItem.Search.route) {
                    Dashboard(
                        navController
                    )
                }
                composable(
                    route = NavItem.Add.route,
                    enterTransition = {
                        slideIntoContainer(
                            animationSpec = tween(
                                durationMillis = AnimationDuration,
                                easing = EaseIn
                            ),
                            towards = AnimatedContentTransitionScope.SlideDirection.Up
                        )
                    }
                ) {
                    val cameraScanViewModel: CameraScanViewModel = viewModel()
                    ScanNewItem(
                        onTextDetected = cameraScanViewModel::textDetected,
                    )
                }
            }
        }
    }
}

@Composable
fun Dashboard(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("home screen")
        IconButton(
            onClick = { navController.navigate(NavItem.Add.route) },
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

sealed class NavItem(val route: String) {
    object Search : NavItem(route = "search")
    object Add : NavItem(route = "add")
}

@Preview(showBackground = true)
@Composable
private fun MainApplicationPreview() {
    DeweyDecimalTheme {
        MainComponent()
    }
}
