package xyz.katiedotson.deweydecimal.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import xyz.katiedotson.deweydecimal.NavItem
import xyz.katiedotson.deweydecimal.ui.theme.GreenFive
import xyz.katiedotson.deweydecimal.ui.theme.GreenTwo
import kotlin.math.roundToInt

@Composable
fun BottomNavBar(
    selectedNavItem: NavItem,
    onAddClicked: () -> Unit,
    onSearchClicked: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .background(GreenFive)
    ) {
        val derivedDimension = this.maxWidth
        val pxToMove = with(LocalDensity.current) {
            derivedDimension.toPx().roundToInt() / 2
        }

        val offset by animateIntOffsetAsState(
            targetValue = when (selectedNavItem) {
                is NavItem.Search -> {
                    IntOffset(x = pxToMove, y = 0)
                }

                else -> {
                    IntOffset.Zero
                }
            },
            animationSpec = spring(
                stiffness = Spring.StiffnessMediumLow,
                dampingRatio = Spring.DampingRatioLowBouncy
            ),
            label = "menu_indicator"
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                NavButton(
                    onClick = onAddClicked,
                    isSelected = selectedNavItem == NavItem.Add,
                    imageVector = Icons.Filled.Add,
                    text = "Add"
                )
                NavButton(
                    onClick = onSearchClicked,
                    isSelected = selectedNavItem == NavItem.Search,
                    imageVector = Icons.Filled.Search,
                    text = "Search"
                )
            }
            Box(
                modifier = Modifier
                    .height(7.dp)
                    .offset { offset }
                    .background(GreenTwo)
                    .fillMaxWidth(fraction = 0.5f)
            )
        }
    }
}

@Composable
fun NavButton(
    onClick: () -> Unit,
    isSelected: Boolean,
    imageVector: ImageVector,
    text: String
) {
    val color by animateColorAsState(
        targetValue = if (isSelected) GreenTwo else Color.White,
        animationSpec = tween(durationMillis = 600),
        label = "selected_color"
    )
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = GreenFive,
            contentColor = color
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null
            )
            Text(
                text = text,
                fontWeight = if (isSelected) FontWeight.Bold else null
            )
        }
    }
}
