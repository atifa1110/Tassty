package com.example.tassty.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tassty.screen.profile.ProfileScreen
import com.example.tassty.screen.cart.CartRoute
import com.example.tassty.screen.chat.ChatScreen
import com.example.tassty.screen.home.HomeScreen
import com.example.tassty.screen.dashboard.CartDestination
import com.example.tassty.screen.dashboard.ChatDestination
import com.example.tassty.screen.dashboard.HomeDestination
import com.example.tassty.screen.dashboard.ProfileDestination

@Composable
fun InternalNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
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
    onNavigateToCard: ()-> Unit,
    onNavigateToOrder: ()-> Unit,
    onNavigateToPayment: (String, String) -> Unit,
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
                onNavigateToNearbyRestaurant = onNavigateToNearbyRestaurant,
                onNavigateToDetailMenu = onNavigateToDetailMenu,
                onNavigateToVoucher = onNavigateToVoucher
            )
        }
        composable(CartDestination.route) {
            CartRoute(
                onNavigateToDetail = onNavigateToDetail,
                onNavigateToPayment = onNavigateToPayment
            )
        }
        composable(ChatDestination.route){
            ChatScreen()
        }
        composable(ProfileDestination.route) {
            ProfileScreen(
                onNavigateToVoucher = onNavigateToVoucher,
                onNavigateToCollection = onNavigateToCollection,
                onNavigateToFavorite= onNavigateToFavorite,
                onNavigateToAddress= onNavigateToAddress,
                onNavigateToCard = onNavigateToCard,
                onNavigateToOrder = onNavigateToOrder
            )
        }
    }
}