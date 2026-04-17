package com.example.tassty.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.tassty.R
import com.example.tassty.navigation.TasstyNavigationDestination
import com.example.tassty.screen.dashboard.CartDestination
import com.example.tassty.screen.dashboard.ChatDestination
import com.example.tassty.screen.dashboard.HomeDestination
import com.example.tassty.screen.dashboard.ProfileDestination
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Orange500

enum class BottomLevelDestination(
    override val route: String,
    override val destination: String,
    val selectedIcon : ImageVector,
    val unselectedIcon : ImageVector,
    @StringRes val textResourceId: Int
) : TasstyNavigationDestination {

    Home(
        route = HomeDestination.route,
        destination = HomeDestination.destination,
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Filled.Home,
        textResourceId = R.string.home
    ),
    Cart(
        route = CartDestination.route,
        destination = CartDestination.destination,
        selectedIcon = Icons.Default.ShoppingBag,
        unselectedIcon = Icons.Filled.ShoppingBag,
        textResourceId = R.string.cart
    ),
    Chat(
        route = ChatDestination.route,
        destination = ChatDestination.destination,
        selectedIcon = Icons.AutoMirrored.Filled.Chat,
        unselectedIcon = Icons.AutoMirrored.Filled.Chat,
        textResourceId = R.string.chat
    ),
    Profile(
        route = ProfileDestination.route,
        destination = ProfileDestination.destination,
        selectedIcon = Icons.Default.Person,
        unselectedIcon = Icons.Filled.Person,
        textResourceId = R.string.profile
    )
}

@Composable
fun NavigationBottomBar(
    items: Array<BottomLevelDestination>,
    currentDestination: NavDestination?,
    navController: NavHostController,
    badgeFavorite: Int
) {
    NavigationBar(
        containerColor = LocalCustomColors.current.background
    ) {
        items.forEach { item ->
            NavigationBarItem(
                label = {
                    Text(
                        text = stringResource(item.textResourceId),
                        style = LocalCustomTypography.current.bodySmallMedium
                    )
                },
                selected = currentDestination?.hierarchy?.any {
                    it.route == item.route
                } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Orange500,
                    unselectedIconColor = LocalCustomColors.current.text,
                    selectedTextColor = Orange500,
                    unselectedTextColor = LocalCustomColors.current.text,
                    indicatorColor = Color.Transparent
                ),
                icon = {
                    NavigationIcon(
                        item = item,
                        selected = currentDestination?.hierarchy?.any {
                            it.route == item.route
                        } == true,
                        badgeFavorite = badgeFavorite
                    )
                },
            )
        }
    }

}

@Composable
fun NavigationIcon(
    modifier: Modifier = Modifier,
    item: BottomLevelDestination,
    selected: Boolean,
    badgeFavorite : Int,
    maxCount: Int = 99,
) {
    val displayText = if (badgeFavorite > maxCount) "$maxCount+" else badgeFavorite.toString()
    val isSingleChar = displayText.length == 1
    BadgedBox(
        badge = {
            if (item.selectedIcon == Icons.Default.Favorite) {
                if(badgeFavorite>0) {
                    Badge(
                        modifier = if (isSingleChar) {
                            modifier.size(18.dp)
                        } else {
                            modifier.defaultMinSize(minWidth = 18.dp, minHeight = 18.dp)
                        },
                        containerColor = Color.Red,
                        contentColor = Neutral10,
                    ) {
                        Text(
                            text = displayText,
                            textAlign = TextAlign.Center,
                            style = LocalCustomTypography.current.bodyXtraSmallMedium
                        )
                    }
                }
            }
        }
    ) {
        Icon(
            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.name
        )
    }
}