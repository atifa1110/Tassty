package com.example.tassty.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.core.data.model.RegistrationStep
import com.example.tassty.MainViewModel

@Composable
fun TasstyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
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
            onBackButtonClick = {
                navController.popBackStack()
            },
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
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToDetail = { id ->
                navController.navigate(DetailRestaurantDestination.createRoute(id))
            },
            onNavigateToSearch = {
                navController.navigate(SearchDestination.route)
            },
            onNavigateToCategory = { id, name, image ->
                navController.navigate(CategoryDestination.createRoute(id,name,image))
            },
            onNavigateToRecommended = {
                navController.navigate(RecommendedDestination.route)
            },
            onNavigateToNearbyRestaurant = {
                navController.navigate(NearbyDestination.route)
            },
            onNavigateToCollection = {
                navController.navigate(CollectionDestination.route)
            },
            onNavigateToDetailCollection = { id, name, image ->
                navController.navigate(CollectionDetailDestination.createRoute(id,name,image))
            },
            onAddCartSuccess = { _, message ->
                viewModel.showSnackbar(message)
            },
            onDeleteSuccess = { message ->
                navController.getBackStackEntry(CollectionDestination.route)
                    .savedStateHandle["snack_message"] = message
            },
            onNavigateToFavorite = {
                navController.navigate(FavoriteDestination.route)
            },
            onNavigateToDetailMenu = { id->
                navController.navigate(DetailMenuDestination.createRoute(id))
            },
            onNavigateToBestSeller = { id->
                navController.navigate(BestSellerDestination.createRoute(id))
            },
            onNavigateToVoucher = {
                navController.navigate(VoucherDestination.route)
            },
            onNavigateToAddress = {
                navController.navigate(AddressDestination.route)
            },
            onNavigateToPayment = {
                navController.navigate(PaymentDestination.route)
            },
            onNavigateToAddCard = {
                navController.navigate(AddCardDestination.route)
            }
        )
    }
}