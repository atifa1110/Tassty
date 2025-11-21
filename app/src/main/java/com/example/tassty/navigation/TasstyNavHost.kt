package com.example.tassty.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.core.data.model.RegistrationStep
import com.example.tassty.AuthViewModel

@Composable
fun TasstyNavHost(
    navController: NavHostController,
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val authStatus by viewModel.authState.collectAsStateWithLifecycle()

    val startDestination = if (authStatus.isLoggedIn) {
        MainGraph.route
    } else {
        AuthGraph.route
    }

    val startAuthDestination = when {
        authStatus.registrationStep == RegistrationStep.REGISTERED && !authStatus.isVerified ->
            VerifyDestination.route
        authStatus.isVerified && !authStatus.hasCompletedSetup ->
            SetUpCuisineDestination.route
        authStatus.isVerified && authStatus.hasCompletedSetup ->
            SetUpCompletedDestination.route
        authStatus.isLoggedIn && authStatus.hasCompletedSetup ->
            MainGraph.route
        else -> LoginDestination.route
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        boardingGraph (
            onNavigateToAuth = {
                navController.navigate(AuthGraph.route){
                    popUpTo(OnBoardingDestination.route){
                        inclusive = true
                    }
                }
            }
        )
        authGraph(
            startAuthDestination = startAuthDestination,
            onBackButtonClick = onBackButtonClick,
            onNavigateToLogin = {
                navController.navigate(LoginDestination.route){
                    popUpTo(RegisterDestination.route){
                        inclusive = true
                    }
                }
            },
            onNavigateToHome = {
                navController.navigate(MainGraph.route)
            },
            onNavigateToRegister = {
                navController.navigate(RegisterDestination.route){
                    popUpTo(LoginDestination.route){
                        inclusive = true
                    }
                }
            },
            onNavigateToResetPassword = {
                navController.navigate(ResetPasswordDestination.route)
            },
            onNavigateToVerify = {
                navController.navigate(VerifyDestination.route)
            },
            onNavigateToSetUpCuisine = {
                navController.navigate(SetUpCuisineDestination.route)
            },
            onNavigateToSetUpLocation = { it ->
                navController.navigate(SetUpLocationDestination.createRoute(it))
            },
            onNavigateToComplete = {
                navController.navigate(SetUpCompletedDestination.route)
            }
        )
        mainGraph(
            onNavigateToDetail = {
                navController.navigate(DetailRestaurantDestination.route)
            },
            onNavigateToSearch = {
                navController.navigate(SearchDestination.route)
            },
            onNavigateToCategory = {name,image->
                navController.navigate(CategoryDestination.createRoute(name,image))
            },
            onNavigateToRecommended = {
                navController.navigate(RecommendedDestination.route)
            },
            onNavigateToNearbyRestaurant = {
                navController.navigate(NearbyDestination.route)
            }
        )
    }
}