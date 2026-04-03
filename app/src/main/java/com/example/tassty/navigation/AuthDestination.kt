package com.example.tassty.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.tassty.VerificationType
import com.example.tassty.VerifyArgs
import com.example.tassty.navigation.PaymentDestination.idArg
import com.example.tassty.navigation.PaymentDestination.totalArg
import com.example.tassty.screen.emailinput.EmailInputScreen
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
    override val route: String = "verify"
    override val destination: String = "verify"

    const val typeArg = "type"
    const val expiresArg = "expiresIn"
    const val resendArg = "resendDelay"

    private const val TypeNullMessage = "Verification type is null."

    val routeWithArgs = "${route}/{${typeArg}}?${expiresArg}={${expiresArg}}&${resendArg}={${resendArg}}"

    fun createRoute(
        type: VerificationType,
        expiresIn: Int = 300,
        resendDelay: Int = 60
    ): String {
        return "${route}/${type.name}?${expiresArg}=$expiresIn&${resendArg}=$resendDelay"
    }

    fun getArgs(savedStateHandle: SavedStateHandle): VerifyArgs {
        val typeString: String = checkNotNull(savedStateHandle[typeArg]) { TypeNullMessage }
        return VerifyArgs(
            type = VerificationType.valueOf(typeString),
            expiresIn = savedStateHandle[expiresArg] ?: 300,
            resendDelay = savedStateHandle[resendArg] ?: 60
        )
    }
}


object EmailInputDestination : TasstyNavigationDestination{
    override val route: String = "email_input"
    override val destination: String = "email_input"
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
    onNavigateBack: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToHome :() -> Unit,
    onNavigateToLoginFromRegister: () -> Unit,
    onNavigateToLoginFromReset:()-> Unit,
    onNavigateToEmailInput: () -> Unit,
    onNavigateToNewPassword: () -> Unit,
    onNavigateToVerify: (VerificationType, Int, Int) -> Unit,
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
                onNavigateToEmailInput = onNavigateToEmailInput
            )
        }

        composable(RegisterDestination.route) {
            RegisterRoute(
                onNavigateToLogin = onNavigateToLoginFromRegister,
                onNavigateToVerify = onNavigateToVerify
            )
        }

        composable(EmailInputDestination.route) {
            EmailInputScreen (
                onNavigateToVerify = onNavigateToVerify,
                onNavigateBack = onNavigateBack
            )
        }

        composable(
            route = VerifyDestination.routeWithArgs,
            arguments = listOf(
                navArgument(VerifyDestination.typeArg) { type = NavType.StringType },
                navArgument(VerifyDestination.expiresArg) {
                    type = NavType.IntType
                    defaultValue = 300
                },
                navArgument(VerifyDestination.resendArg) {
                    type = NavType.IntType
                    defaultValue = 60
                }
            )
        ) {
            VerificationRoute(
                onNavigateToSetUp = onNavigateToSetUpCuisine,
                onNavigateToNewPassword = onNavigateToNewPassword
            )
        }

        composable(ResetPasswordDestination.route) {
            ResetPasswordScreen(
                onNavigateBack = onNavigateBack,
                onNavigateLogin = onNavigateToLoginFromReset
            )
        }

        composable(SetUpCuisineDestination.route) {
            SetupCuisineRoute(
                onNavigateToSetUpLocation = onNavigateToSetUpLocation,
                onBackButtonClick = onNavigateBack,
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