package com.example.tassty.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tassty.screen.profile.ProfileScreen
import com.example.tassty.screen.cart.CartRoute
import com.example.tassty.screen.chat.ChatRoute
import com.example.tassty.screen.home.HomeScreen
import com.example.tassty.screen.main.CartDestination
import com.example.tassty.screen.main.ChatDestination
import com.example.tassty.screen.main.HomeDestination
import com.example.tassty.screen.main.ProfileDestination

@Composable
fun InternalNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onNavigateToDetail: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCategory:(String, String) -> Unit,
    onNavigateToRecommended:() -> Unit,
    onNavigateToNearbyRestaurant:() -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable (HomeDestination.route) {
            HomeScreen(
                onNavigateToDetail = onNavigateToDetail,
                onNavigateToSearch = onNavigateToSearch,
                onNavigateToCategory = onNavigateToCategory,
                onNavigateToRecommended = onNavigateToRecommended,
                onNavigateToNearbyRestaurant = onNavigateToNearbyRestaurant
            )
        }
        // Tab 2: Cart
        composable(CartDestination.route) {
            CartRoute()
        }
        composable(ChatDestination.route){
            ChatRoute()
        }
        composable(ProfileDestination.route) {
            ProfileScreen()
        }
    }
}