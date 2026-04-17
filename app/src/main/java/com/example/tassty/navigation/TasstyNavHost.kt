package com.example.tassty.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.core.data.model.AuthStatus
import com.example.core.data.model.RegistrationStep
import com.example.tassty.VerificationType
import com.example.tassty.activity.MainViewModel
import com.example.tassty.screen.dashboard.CartDestination

@Composable
fun TasstyNavHost(
    modifier: Modifier = Modifier,
    initialCid: String? = null,
    authStatus: AuthStatus,
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val startDestination = when{
        !authStatus.isBoardingCompleted -> OnBoardingDestination.route
        authStatus.isLoggedIn -> MainGraph.route
        else -> AuthGraph.route
    }

    val startAuthDestination = when {
        authStatus.registrationStep == RegistrationStep.REGISTERED && !authStatus.isVerified ->
            VerifyDestination.createRoute(VerificationType.REGISTRATION,300,60)
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
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToLoginFromRegister = {
                navController.navigate(LoginDestination.route){
                    popUpTo(RegisterDestination.route){
                        inclusive = true
                    }
                }
            },
            onNavigateToLoginFromReset = {
                navController.navigate(LoginDestination.route){
                    popUpTo(EmailInputDestination.route){
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
            onNavigateToEmailInput = {
                navController.navigate(EmailInputDestination.route)
            },
            onNavigateToNewPassword = {
                navController.navigate(ResetPasswordDestination.route)
            },
            onNavigateToVerify = { type, expire, delay->
                navController.navigate(VerifyDestination.createRoute(type,expire,delay))
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
            initialCid = initialCid,
            onNavigateBack = {
                navController.popBackStack()
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
            onNavigateToDetailRest = { id ->
                navController.navigate(DetailRestaurantDestination.createRoute(id))
            },
            onNavigateToDetailMenu = { id->
                navController.navigate(DetailMenuDestination.createRoute(id))
            },
            onNavigateToReview = { id ->
                navController.navigate(ReviewDestination.createRoute(id))
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
            onNavigateToPayment = { id, total->
                navController.navigate(PaymentDestination.createRoute(id,total))
            },
            onNavigateToCard = {
                navController.navigate(CardDestination.route)
            },
            onNavigateToAddCard = {
                navController.navigate(AddCardDestination.route)
            },
            onNavigateToOrder = {
                navController.navigate(OrderDestination.route)
            },
            onNavigateToOrderProcess = {id->
                navController.navigate(OrderProcessDestination.createRoute(id)){
                    // Hapus dari Payment sampai ke Cart
                    popUpTo(CartDestination.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onNavigateFromOrderToOrderProcess = { id->
                navController.navigate(OrderProcessDestination.createRoute(id))
            },
            onNavigateToOrderDetail = { id->
                navController.navigate(OrderDetailDestination.createRoute(id))
            },
            onNavigateToRating = { data ->
                navController.navigate(RatingDestination.createRoute(data))
            },
            onNavigateToMessage = { channelId->
                navController.navigate(MessageDestination.createRoute(channelId))
            },
            onNavigateToLoginFromHome = {
                navController.navigate(LoginDestination.route){
                    popUpTo(MainGraph.route){
                        inclusive = true
                    }
                }
            },
            onNavigateToEditProfile = {
                navController.navigate(EditProfileDestination.route)
            },
            onNavigateToTerm = {
                navController.navigate(TermDestination.route)
            },
            onNavigateToAddAddress = {
                navController.navigate(AddAddressDestination.route)
            },
            onNavigateToDetailLocation = { data ->
                navController.navigate(DetailLocationDestination.createRoute(data))
            }
        )
    }
}