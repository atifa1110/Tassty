package com.example.tassty.navigation

import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.tassty.navigation.CategoryDestination.idArg
import com.example.tassty.navigation.CategoryDestination.imageArg
import com.example.tassty.navigation.CategoryDestination.nameArg
import com.example.tassty.screen.addcard.AddCardScreen
import com.example.tassty.screen.address.AddressScreen
import com.example.tassty.screen.bestseller.BestSellerScreen
import com.example.tassty.screen.card.CardScreen
import com.example.tassty.screen.category.CategoryScreen
import com.example.tassty.screen.detailcollection.CollectionDetailScreen
import com.example.tassty.screen.collection.CollectionScreen
import com.example.tassty.screen.dashboard.DashboardScreen
import com.example.tassty.screen.detailmenu.DetailMenuScreen
import com.example.tassty.screen.detailorder.DetailOrderScreen
import com.example.tassty.screen.detailrestaurant.DetailRestaurantScreen
import com.example.tassty.screen.favorite.FavoriteScreen
import com.example.tassty.screen.nearby.NearbyRestaurantScreen
import com.example.tassty.screen.orders.OrderScreen
import com.example.tassty.screen.payment.PaymentScreen
import com.example.tassty.screen.recommended.RecommendedRestaurantScreen
import com.example.tassty.screen.search.SearchRoute
import com.example.tassty.screen.voucher.VoucherScreen

object MainGraph : TasstyNavigationDestination {
    override val route = "main_graph"
    override val destination = "main_graph_destination"
}

object MainDestination : TasstyNavigationDestination {
    override val route: String = "main_route"
    override val destination: String = "main_destination"
}

object DetailRestaurantDestination : TasstyNavigationDestination {
    override val route: String = "detail_restaurant"
    override val destination: String = "detail_restaurant"

    const val idArg = "id"
    private const val IdNullMessage = "Id is null."

    val routeWithArgs = "$route/{$idArg}"

    fun createRoute(id: String): String {
        return "$route/$id"
    }

    fun getId(savedStateHandle: SavedStateHandle): String {
        return checkNotNull(savedStateHandle[idArg]) { IdNullMessage }
    }
}

object BestSellerDestination : TasstyNavigationDestination {
    override val route: String = "best_seller"
    override val destination: String = "best_seller"

    const val idArg = "id"
    private const val IdNullMessage = "Id is null."

    val routeWithArgs = "${route}/{$idArg}"

    fun createRoute(id: String): String {
        return "${route}/$id"
    }

    fun getId(savedStateHandle: SavedStateHandle): String {
        return checkNotNull(savedStateHandle[idArg]) { IdNullMessage }
    }
}

object DetailMenuDestination : TasstyNavigationDestination {
    override val route: String = "detail_menu"
    override val destination: String = "detail_menu"

    const val idArg = "id"
    private const val IdNullMessage = "Id is null."

    val routeWithArgs = "$route/{$idArg}"

    fun createRoute(id: String): String {
        return "$route/$id"
    }

    fun getId(savedStateHandle: SavedStateHandle): String {
        return checkNotNull(savedStateHandle[idArg]) { IdNullMessage }
    }
}

object SearchDestination : TasstyNavigationDestination {
    override val route: String = "search"
    override val destination: String = "search"
}

object RecommendedDestination : TasstyNavigationDestination {
    override val route: String = "recommended"
    override val destination: String = "recommended"
}

object CollectionDestination : TasstyNavigationDestination {
    override val route: String = "collection"
    override val destination: String = "collection"
}

object CollectionDetailDestination : TasstyNavigationDestination {
    override val route: String = "collection_detail"
    override val destination: String = "collection_detail"

    const val idArg = "id"
    const val nameArg = "name"
    const val imageArg = "image"

    private const val IdNullMessage = "Id is null."

    val routeWithArgs = "$route/{$idArg}?$nameArg={$nameArg}&$imageArg={$imageArg}"

    fun createRoute(id: String, name: String, image: String): String {
        return "$route/$id?$nameArg=${Uri.encode(name)}&$imageArg=${Uri.encode(image)}"
    }

    fun getId(savedStateHandle: SavedStateHandle): String {
        return checkNotNull(savedStateHandle[CategoryDestination.idArg]) { IdNullMessage }
    }

    fun getName(savedStateHandle: SavedStateHandle): String {
        return savedStateHandle[nameArg] ?: ""
    }

    fun getImage(savedStateHandle: SavedStateHandle): String {
        return savedStateHandle[imageArg] ?: ""
    }
}

object FavoriteDestination : TasstyNavigationDestination {
    override val route: String = "favorite"
    override val destination: String = "favorite"
}

object VoucherDestination : TasstyNavigationDestination {
    override val route: String = "voucher"
    override val destination: String = "voucher"
}

object CategoryDestination : TasstyNavigationDestination {
    override val route = "category"
    override val destination: String = "category"

    const val idArg = "id"
    const val nameArg = "name"
    const val imageArg = "image"

    private const val IdNullMessage = "Id is null."

    val routeWithArgs = "$route/{$idArg}?$nameArg={$nameArg}&$imageArg={$imageArg}"

    fun createRoute(id: String, name: String, image: String): String {
        return "$route/$id?$nameArg=${Uri.encode(name)}&$imageArg=${Uri.encode(image)}"
    }

    fun getId(savedStateHandle: SavedStateHandle): String {
        return checkNotNull(savedStateHandle[idArg]) { IdNullMessage }
    }
}

object NearbyDestination : TasstyNavigationDestination {
    override val route: String = "nearby"
    override val destination: String = "nearby"
}

object AddressDestination : TasstyNavigationDestination {
    override val route: String = "address"
    override val destination: String = "address"
}
object PaymentDestination : TasstyNavigationDestination {
    override val route: String = "payment"
    override val destination: String = "payment"

    const val idArg = "id"
    const val totalArg = "total"
    private const val IdNullMessage = "Id is null."

    val routeWithArgs = "${route}/{${idArg}}?${totalArg}={${totalArg}}"

    fun createRoute(id: String, total: String): String {
        return "${route}/$id?${totalArg}=$total"
    }

    fun getId(savedStateHandle: SavedStateHandle): String {
        return checkNotNull(savedStateHandle[idArg]) { IdNullMessage }
    }
}

object CardDestination : TasstyNavigationDestination {
    override val route: String = "card"
    override val destination: String = "card"
}
object AddCardDestination : TasstyNavigationDestination {
    override val route: String = "add_card"
    override val destination: String = "add_card"
}

object OrderDestination : TasstyNavigationDestination {
    override val route: String = "order"
    override val destination: String = "order"
}

object DetailOrderDestination : TasstyNavigationDestination {
    override val route: String = "detail_order"
    override val destination: String = "detail_order"

    const val idArg = "id"
    private const val IdNullMessage = "Id is null."

    val routeWithArgs = "${route}/{${idArg}}"

    fun createRoute(id: String): String {
        return "${route}/$id"
    }

    fun getId(savedStateHandle: SavedStateHandle): String {
        return checkNotNull(savedStateHandle[idArg]) { IdNullMessage }
    }
}


fun NavGraphBuilder.mainGraph(
    onNavigateBack:() -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCategory:(String,String, String) -> Unit,
    onNavigateToRecommended:() -> Unit,
    onNavigateToNearbyRestaurant:() -> Unit,
    onNavigateToCollection: () -> Unit,
    onNavigateToDetailCollection: (String,String, String) -> Unit,
    onAddCartSuccess:(String,String) -> Unit,
    onDeleteSuccess:(String) -> Unit,
    onNavigateToFavorite:() -> Unit,
    onNavigateToDetailMenu:(String) -> Unit,
    onNavigateToBestSeller:(String) -> Unit,
    onNavigateToVoucher:()-> Unit,
    onNavigateToAddress: () -> Unit,
    onNavigateToPayment: (String, String)-> Unit,
    onNavigateToCard:() -> Unit,
    onNavigateToAddCard: ()-> Unit,
    onNavigateToOrder: ()-> Unit,
    onNavigateToDetailOrder:(String) -> Unit
) {
    navigation(
        route = MainGraph.route,
        startDestination = MainDestination.route
    ) {
        composable(route = MainDestination.route) {
            DashboardScreen(
                onNavigateToDetail = onNavigateToDetail,
                onNavigateToSearch = onNavigateToSearch,
                onNavigateToCategory = onNavigateToCategory,
                onNavigateToRecommended = onNavigateToRecommended,
                onNavigateToNearbyRestaurant = onNavigateToNearbyRestaurant,
                onNavigateToCollection = onNavigateToCollection,
                onNavigateToFavorite = onNavigateToFavorite,
                onNavigateToDetailMenu = onNavigateToDetailMenu,
                onNavigateToVoucher = onNavigateToVoucher,
                onNavigateToAddress = onNavigateToAddress,
                onNavigateToCard = onNavigateToCard,
                onNavigateToOrder = onNavigateToOrder,
                onNavigateToPayment = onNavigateToPayment
            )
        }

        composable(route = SearchDestination.route) {
            SearchRoute(
                onNavigateToDetail = onNavigateToDetail
            )
        }

        composable(route = NearbyDestination.route) {
            NearbyRestaurantScreen()
        }

        composable(
            route = DetailRestaurantDestination.routeWithArgs,
            arguments = listOf(
                navArgument(DetailRestaurantDestination.idArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val snackHostState = remember { SnackbarHostState() }

            // Use collectAsState for better Compose integration than observeAsState (livedata)
            val message by backStackEntry.savedStateHandle
                .getStateFlow<String?>("snack_message", null)
                .collectAsStateWithLifecycle()

            LaunchedEffect(message) {
                message?.let { msg ->
                    snackHostState.showSnackbar(msg)
                    backStackEntry.savedStateHandle["snack_message"] = null
                }
            }

            DetailRestaurantScreen(
                snackHostState = snackHostState,
                onNavigateToDetailMenu = onNavigateToDetailMenu,
                onNavigateToBestSeller=onNavigateToBestSeller
            )
        }

        composable(
            route = DetailMenuDestination.routeWithArgs,
            arguments = listOf(
                navArgument(DetailMenuDestination.idArg) { type = NavType.StringType }
            )
        ) {
            DetailMenuScreen(
                onNavigateBack = onNavigateBack,
                onAddCartSuccess = onAddCartSuccess
            )
        }

        composable(
            route = BestSellerDestination.routeWithArgs,
            arguments = listOf(
                navArgument(idArg) { type = NavType.StringType })
        ){ backStackEntry ->
            BestSellerScreen()
        }

        composable(
            route = RecommendedDestination.route
        ){
            RecommendedRestaurantScreen(
                onNavigateToDetail = onNavigateToDetail
            )
        }

        composable(
            route = CategoryDestination.routeWithArgs,
            arguments = listOf(
                navArgument(idArg) { type = NavType.StringType },
                navArgument(nameArg) { type = NavType.StringType; nullable = true },
                navArgument(imageArg) { type = NavType.StringType; nullable = true }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString(nameArg) ?: ""
            val image = backStackEntry.arguments?.getString(imageArg) ?: ""

            CategoryScreen(name = name,image = image,onNavigateToDetail=onNavigateToDetail)
        }

        composable(route = CollectionDestination.route) { backStackEntry ->
            val snackHostState = remember { SnackbarHostState() }

            // Use collectAsState for better Compose integration than observeAsState (livedata)
            val message by backStackEntry.savedStateHandle
                .getStateFlow<String?>("snack_message", null)
                .collectAsStateWithLifecycle()

            LaunchedEffect(message) {
                message?.let { msg ->
                    snackHostState.showSnackbar(msg)
                    backStackEntry.savedStateHandle["snack_message"] = null
                }
            }

            CollectionScreen(
                snackHostState = snackHostState,
                onNavigateBack = onNavigateBack,
                onNavigateToDetailCollection = onNavigateToDetailCollection,
            )
        }

        composable(
            route = CollectionDetailDestination.routeWithArgs,
            arguments = listOf(
                navArgument(idArg) { type = NavType.StringType },
                navArgument(nameArg) { type = NavType.StringType; nullable = true },
                navArgument(imageArg) { type = NavType.StringType; nullable = true }
            )
        ) {
            CollectionDetailScreen(
                onNavigateBack = onNavigateBack,
                onDeleteSuccess = onDeleteSuccess
            )
        }

        composable(
            route = FavoriteDestination.route,
        ) {
            FavoriteScreen(
                onNavigateBack = onNavigateBack
            )
        }

        composable(
            route = VoucherDestination.route,
        ) {
            VoucherScreen()
        }

        composable(
            route = AddressDestination.route,
        ) {
            AddressScreen()
        }

        composable(
            route = PaymentDestination.routeWithArgs,
            arguments = listOf(
                navArgument(PaymentDestination.totalArg) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val total = backStackEntry.arguments?.getString(PaymentDestination.totalArg) ?: ""
            PaymentScreen(
                total = total,
                onNavigateToOrderDetail = onNavigateToDetailOrder
            )
        }

        composable(
            route = CardDestination.route,
        ) {
            CardScreen(
                onNavigateToAddCard = onNavigateToAddCard
            )
        }

        composable(
            route = AddCardDestination.route,
        ) {
            AddCardScreen(
                onNavigateBack = onNavigateBack
            )
        }

        composable(
            route = OrderDestination.route,
        ) {
            OrderScreen(
                onNavigateToPayment = onNavigateToPayment,
                onNavigateToDetailOrder = onNavigateToDetailOrder
            )
        }

        composable(
            route = DetailOrderDestination.routeWithArgs,
        ) {
            DetailOrderScreen()
        }

    }
}