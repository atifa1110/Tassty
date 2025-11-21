package com.example.tassty.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.tassty.screen.login.LoginRoute
import com.example.tassty.screen.register.RegisterRoute
import com.example.tassty.screen.resetpassword.ResetPasswordScreen
import com.example.tassty.screen.setupcompleted.SetUpCompletedScreen
import com.example.tassty.screen.setupcuisine.SetupCuisineRoute
import com.example.tassty.screen.setuplocation.SetupLocationRoute
import com.example.tassty.screen.setuplocation.SetupLocationScreen
import com.example.tassty.screen.verification.VerificationRoute

object AuthGraph : TasstyNavigationDestination {
    override val route = "auth_graph"
    override val destination = "auth_graph"
}

object LoginDestination : TasstyNavigationDestination {
    override val route = "login"
    override val destination = "login"
}

object RegisterDestination : TasstyNavigationDestination {
    override val route = "register"
    override val destination = "register"
}

object VerifyDestination : TasstyNavigationDestination {
    override val route = "verify"
    override val destination = "verify"
}
object ResetPasswordDestination : TasstyNavigationDestination{
    override val route: String = "reset_password"
    override val destination: String = "reset_password"
}

object SetUpCuisineDestination : TasstyNavigationDestination{
    override val route: String = "set_up_cuisine"
    override val destination: String = "set_up_cuisine"
}

object SetUpLocationDestination : TasstyNavigationDestination {
    override val route: String = "set_up_location"
    override val destination: String = "set_up_location"

    // route dengan optional argument
    fun createRoute(cuisines: List<String>): String {
        val cuisinesArg = cuisines.joinToString(",") // encode list jadi string
        return "$route?cuisines=$cuisinesArg"
    }
}

object SetUpCompletedDestination : TasstyNavigationDestination{
    override val route: String = "set_up_complete"
    override val destination: String = "set_up_complete"
}


fun NavGraphBuilder.authGraph(
    startAuthDestination: String,
    onBackButtonClick: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToHome :() -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToResetPassword: () -> Unit,
    onNavigateToVerify: () -> Unit,
    onNavigateToSetUpCuisine:() -> Unit,
    onNavigateToSetUpLocation: (List<String>) -> Unit,
    onNavigateToComplete:() -> Unit
) {
    navigation(
        route = AuthGraph.route,
        startDestination = startAuthDestination,
    ) {
        composable(LoginDestination.route) {
            LoginRoute(
                onNavigateToRegister = onNavigateToRegister,
                onNavigateToHome = onNavigateToHome,
                onNavigateToResetPassword = onNavigateToResetPassword
            )
        }

        composable(RegisterDestination.route) {
            RegisterRoute(
                onNavigateToLogin = onNavigateToLogin,
                onNavigateToVerify = onNavigateToVerify
            )
        }

        composable(
            route = VerifyDestination.destination,
        ) {
            VerificationRoute(
                onNavigateToSetUp = onNavigateToSetUpCuisine
            )
        }

        composable(ResetPasswordDestination.route) {
            ResetPasswordScreen()
        }

        composable(SetUpCuisineDestination.route) {
            SetupCuisineRoute(
                onNavigateToSetUpLocation = onNavigateToSetUpLocation,
                onBackButtonClick = onBackButtonClick,
                onSkipClick = {}
            )
        }

        composable(
            route = "${SetUpLocationDestination.route}?cuisines={cuisines}",
            arguments = listOf(
                navArgument("cuisines") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val cuisinesArg = backStackEntry.arguments?.getString("cuisines") ?: ""
            val selectedCuisines = if (cuisinesArg.isEmpty()) emptyList() else cuisinesArg.split(",")

            SetupLocationRoute (
                selectedCuisines = selectedCuisines,
                onBackClick = {},
                onNavigateToComplete = onNavigateToComplete
            )
        }

        composable(SetUpCompletedDestination.route) {
            SetUpCompletedScreen(
                onNavigateToHome = onNavigateToHome
            )
        }

    }
}