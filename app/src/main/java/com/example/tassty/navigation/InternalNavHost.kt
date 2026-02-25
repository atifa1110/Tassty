package com.example.tassty.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tassty.screen.profile.ProfileScreen
import com.example.tassty.screen.cart.CartRoute
import com.example.tassty.screen.chat.ChatRoute
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
                onNavigateToDetail = onNavigateToDetail
            )
        }
        composable(ChatDestination.route){
            ChatRoute()
        }
        composable(ProfileDestination.route) {
            ProfileScreen(
                onNavigateToVoucher = onNavigateToVoucher,
                onNavigateToCollection = onNavigateToCollection,
                onNavigateToFavorite= onNavigateToFavorite,
                onNavigateToAddress= onNavigateToAddress
            )
        }
    }
}