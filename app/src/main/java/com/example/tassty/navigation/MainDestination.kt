package com.example.tassty.navigation

import android.net.Uri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.tassty.screen.category.CategoryScreen
import com.example.tassty.screen.detailrestaurant.DetailRestaurantScreen
import com.example.tassty.screen.main.MainRoute
import com.example.tassty.screen.recommended.RecommendedScreen
import com.example.tassty.screen.search.SearchRoute

// this is container for all Bottom Navigation
object MainGraph : TasstyNavigationDestination {
    override val route = "main_graph"
    override val destination = "main_graph_destination"
}

// MainDestination adalah Composable (Screen) yang akan menampung Bottom Navigation
object MainDestination : TasstyNavigationDestination {
    override val route: String = "main_route"
    override val destination: String = "main_destination"
}

object DetailRestaurantDestination : TasstyNavigationDestination {
    override val route: String = "detail_restaurant"
    override val destination: String = "detail_restaurant"
}

object SearchDestination : TasstyNavigationDestination {
    override val route: String = "search"
    override val destination: String = "search"
}

object RecommendedDestination : TasstyNavigationDestination {
    override val route: String = "recommended"
    override val destination: String = "recommended"
}

object CategoryDestination : TasstyNavigationDestination {
    override val route: String = "category/{categoryName}?imageUrl={imageUrl}"
    override val destination: String = "category"

    fun createRoute(categoryName: String, imageUrl: String) =
        "category/$categoryName?imageUrl=${Uri.encode(imageUrl)}"
}

object NearbyDestination : TasstyNavigationDestination {
    override val route: String = "nearby"
    override val destination: String = "nearby"
}

fun NavGraphBuilder.mainGraph(
    onNavigateToDetail: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCategory:(String, String) -> Unit,
    onNavigateToRecommended:() -> Unit,
    onNavigateToNearbyRestaurant:() -> Unit
) {
    navigation(
        route = MainGraph.route,
        startDestination = MainDestination.route
    ) {
        composable(route = MainDestination.route) {
            MainRoute(
                onNavigateToDetail = onNavigateToDetail,
                onNavigateToSearch = onNavigateToSearch,
                onNavigateToCategory = onNavigateToCategory,
                onNavigateToRecommended = onNavigateToRecommended,
                onNavigateToNearbyRestaurant = onNavigateToNearbyRestaurant
            )
        }

        composable(route = SearchDestination.route) {
            SearchRoute()
        }

        composable(route = DetailRestaurantDestination.route) {
            DetailRestaurantScreen()
        }

        composable(route = RecommendedDestination.route) {
            RecommendedScreen()
        }

        composable(
            route = CategoryDestination.route,
            arguments = listOf(
                navArgument("categoryName") { type = NavType.StringType },
                navArgument("imageUrl") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: "Unknown Category"
            val imageUrl = backStackEntry.arguments?.getString("imageUrl")

                CategoryScreen(
                    categoryName = categoryName,
                    imageUrl = imageUrl?:""
                )
        }
    }
}