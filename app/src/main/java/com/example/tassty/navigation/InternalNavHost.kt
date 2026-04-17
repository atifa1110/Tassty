package com.example.tassty.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tassty.screen.cart.CartScreen
import com.example.tassty.screen.profile.ProfileScreen
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
    onNavigateToSearch: () -> Unit,
    onNavigateToCategory:(String,String, String) -> Unit,
    onNavigateToRecommended:() -> Unit,
    onNavigateToNearbyRestaurant:() -> Unit,
    onNavigateToCollection:() -> Unit,
    onNavigateToFavorite:() -> Unit,
    onNavigateToDetailRest: (String) -> Unit,
    onNavigateToDetailMenu:(String) -> Unit,
    onNavigateToVoucher:()-> Unit,
    onNavigateToAddress: () -> Unit,
    onNavigateToCard: ()-> Unit,
    onNavigateToOrder: ()-> Unit,
    onNavigateToPayment: (String, String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToMessage:(String) -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToTerm: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable (HomeDestination.route) {

            HomeScreen(
                onNavigateToDetailRest = onNavigateToDetailRest,
                onNavigateToSearch = onNavigateToSearch,
                onNavigateToCategory = onNavigateToCategory,
                onNavigateToRecommended = onNavigateToRecommended,
                onNavigateToNearbyRestaurant = onNavigateToNearbyRestaurant,
                onNavigateToDetailMenu = onNavigateToDetailMenu,
                onNavigateToVoucher = onNavigateToVoucher
            )
        }
        composable(CartDestination.route) {
            CartScreen(
                onNavigateToRecommended = onNavigateToRecommended,
                onNavigateToDetailRest = onNavigateToDetailRest,
                onNavigateToPayment = onNavigateToPayment,
                onNavigateToDetailMenu = onNavigateToDetailMenu,
                onNavigateToAddress = onNavigateToAddress
            )
        }
        composable(ChatDestination.route){
            ChatScreen(
                onNavigateToMessage = onNavigateToMessage
            )
        }
        composable(ProfileDestination.route) {
            ProfileScreen(
                onNavigateToVoucher = onNavigateToVoucher,
                onNavigateToCollection = onNavigateToCollection,
                onNavigateToFavorite= onNavigateToFavorite,
                onNavigateToAddress= onNavigateToAddress,
                onNavigateToCard = onNavigateToCard,
                onNavigateToOrder = onNavigateToOrder,
                onNavigateToLogin = onNavigateToLogin,
                onNavigateToEditProfile = onNavigateToEditProfile,
                onNavigateToTerm = onNavigateToTerm
            )
        }
    }
}