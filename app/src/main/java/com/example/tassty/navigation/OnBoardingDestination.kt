package com.example.tassty.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.tassty.screen.onboarding.OnBoardingScreen

object OnBoardingDestination : TasstyNavigationDestination {
    override val route: String = "boarding"
    override val destination: String = "boarding"
}

fun NavGraphBuilder.boardingGraph(
    onNavigateToAuth : () -> Unit,
) = composable(OnBoardingDestination.route){
    OnBoardingScreen (onNavigateToAuth= onNavigateToAuth)
}