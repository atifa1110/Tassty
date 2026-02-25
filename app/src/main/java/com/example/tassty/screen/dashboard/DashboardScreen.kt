package com.example.tassty.screen.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tassty.component.BottomLevelDestination
import com.example.tassty.component.NavigationBottomBar
import com.example.tassty.navigation.InternalNavHost
import com.example.tassty.navigation.TasstyNavigationDestination

object HomeDestination : TasstyNavigationDestination {
    override val route: String = "home_destination"
    override val destination: String = "home_destination"
}

object CartDestination : TasstyNavigationDestination {
    override val route: String = "cart_destination"
    override val destination: String = "cart_destination"
}

object ChatDestination : TasstyNavigationDestination {
    override val route: String = "chat_destination"
    override val destination: String = "chat_destination"
}

object ProfileDestination : TasstyNavigationDestination {
    override val route: String = "profile_route"
    override val destination: String = "profile_destination"
}


@Composable
fun DashboardScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCategory:(String,String, String) -> Unit,
    onNavigateToRecommended:() -> Unit,
    onNavigateToNearbyRestaurant:() -> Unit,
    onNavigateToCollection:() -> Unit,
    onNavigateToFavorite:() -> Unit,
    onNavigateToDetailMenu:(String) -> Unit,
    onNavigateToVoucher:()-> Unit,
    onNavigateToAddress: () -> Unit,
){

    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val topLevelDestinations = BottomLevelDestination.entries.toTypedArray()
    val startDestination = BottomLevelDestination.Home.route

    Scaffold (
        bottomBar = {
            NavigationBottomBar(
                items =topLevelDestinations,
                currentDestination = currentDestination,
                navController = bottomNavController,
                badgeFavorite = 0
            )
        }
    ) { padding ->
        InternalNavHost(
            modifier = Modifier.padding(padding),
            navController = bottomNavController,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToCategory = onNavigateToCategory,
            onNavigateToRecommended = onNavigateToRecommended,
            onNavigateToNearbyRestaurant = onNavigateToNearbyRestaurant,
            onNavigateToCollection = onNavigateToCollection,
            onNavigateToFavorite = onNavigateToFavorite,
            onNavigateToDetailMenu = onNavigateToDetailMenu,
            onNavigateToVoucher = onNavigateToVoucher,
            onNavigateToAddress = onNavigateToAddress
        )
    }
}