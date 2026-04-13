package com.example.tassty.navigation

import android.net.Uri
import android.os.Parcelable
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
import com.example.core.ui.model.RestaurantLocationArgs
import com.example.tassty.RatingType
import com.example.tassty.navigation.CategoryDestination.idArg
import com.example.tassty.navigation.CategoryDestination.imageArg
import com.example.tassty.navigation.CategoryDestination.nameArg
import com.example.tassty.navigation.RatingDestination.DataNullMessage
import com.example.tassty.navigation.RatingDestination.ratingDataArg
import com.example.tassty.screen.addaddress.AddAddressScreen
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
import com.example.tassty.screen.detailroute.DetailLocationScreen
import com.example.tassty.screen.editprofile.EditProfileScreen
import com.example.tassty.screen.favorite.FavoriteScreen
import com.example.tassty.screen.message.MessageScreen
import com.example.tassty.screen.nearby.NearbyRestaurantScreen
import com.example.tassty.screen.orderprocess.OrderProcessScreen
import com.example.tassty.screen.orders.OrderScreen
import com.example.tassty.screen.payment.PaymentScreen
import com.example.tassty.screen.rating.RatingScreen
import com.example.tassty.screen.recommended.RecommendedRestaurantScreen
import com.example.tassty.screen.review.ReviewScreen
import com.example.tassty.screen.search.SearchScreen
import com.example.tassty.screen.terms.TermsScreen
import com.example.tassty.screen.voucher.VoucherScreen
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

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

object DetailLocationDestination : TasstyNavigationDestination {
    const val RESTAURANT_DATA_ARG = "data"
    override val route: String = "detail_location/{$RESTAURANT_DATA_ARG}"
    override val destination: String = "detail_location"

    fun createRoute(args: RestaurantLocationArgs): String {
        val argJson = Gson().toJson(args)
        val encodedJson = Uri.encode(argJson)
        return "$destination/$encodedJson"
    }

    fun getData(savedStateHandle: SavedStateHandle): RestaurantLocationArgs {
        val jsonString: String = checkNotNull(savedStateHandle[RESTAURANT_DATA_ARG]) {
            "Data is not found"
        }
        val decodedJson = Uri.decode(jsonString)
        return Gson().fromJson(decodedJson, RestaurantLocationArgs::class.java)
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

object ReviewDestination : TasstyNavigationDestination {
    override val route = "review"
    override val destination = "review"

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

object TermDestination : TasstyNavigationDestination {
    override val route: String = "term"
    override val destination: String = "term"
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

object OrderProcessDestination : TasstyNavigationDestination {
    override val route: String = "order_process"
    override val destination: String = "order_process"

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

object OrderDetailDestination : TasstyNavigationDestination {
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

@Parcelize
data class RatingNavArg(
    val orderId: String,
    val orderNumber: String,
    val createdAt: String,
    val menuId: String,
    val name: String,
    val imageUrl: String,
    val ratingType: RatingType
) : Parcelable

object RatingDestination : TasstyNavigationDestination {
    override val route = "rating"
    override val destination = "rating"

    const val ratingDataArg = "ratingData"
    private const val DataNullMessage = "Rating data is null."

    val routeWithArgs = "${route}/{${ratingDataArg}}"

    fun createRoute(arg: RatingNavArg): String {
        val argJson = Gson().toJson(arg)
        val encodedJson = Uri.encode(argJson)
        return "${route}/$encodedJson"
    }

    fun getRatingData(savedStateHandle: SavedStateHandle): RatingNavArg {
        val jsonString: String = checkNotNull(savedStateHandle[ratingDataArg]) { DataNullMessage }
        return Gson().fromJson(jsonString, RatingNavArg::class.java)
    }
}
object MessageDestination : TasstyNavigationDestination {
    override val route: String = "message"
    override val destination: String = "message"

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

object EditProfileDestination : TasstyNavigationDestination {
    override val route = "edit_profile"
    override val destination = "edit_profile"
}

object AddAddressDestination : TasstyNavigationDestination {
    override val route: String = "add_address"
    override val destination: String = "add_address"
}

fun NavGraphBuilder.mainGraph(
    onNavigateBack:() -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCategory:(String,String, String) -> Unit,
    onNavigateToRecommended:() -> Unit,
    onNavigateToNearbyRestaurant:() -> Unit,
    onNavigateToCollection: () -> Unit,
    onNavigateToDetailCollection: (String,String, String) -> Unit,
    onAddCartSuccess:(String,String) -> Unit,
    onDeleteSuccess:(String) -> Unit,
    onNavigateToFavorite:() -> Unit,
    onNavigateToDetailRest: (String) -> Unit,
    onNavigateToDetailMenu:(String) -> Unit,
    onNavigateToReview:(String) -> Unit,
    onNavigateToBestSeller:(String) -> Unit,
    onNavigateToVoucher:()-> Unit,
    onNavigateToAddress: () -> Unit,
    onNavigateToPayment: (String, String)-> Unit,
    onNavigateToCard:() -> Unit,
    onNavigateToAddCard: ()-> Unit,
    onNavigateToOrder:() -> Unit,
    onNavigateToOrderProcess: (String)-> Unit,
    onNavigateFromOrderToOrderProcess: (String)-> Unit,
    onNavigateToOrderDetail: (String)-> Unit,
    onNavigateToRating: (RatingNavArg) -> Unit,
    onNavigateToMessage:(String) -> Unit,
    onNavigateToLoginFromHome: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToTerm: () -> Unit,
    onNavigateToAddAddress: () -> Unit,
    onNavigateToDetailLocation: (RestaurantLocationArgs) -> Unit
) {
    navigation(
        route = MainGraph.route,
        startDestination = MainDestination.route
    ) {
        composable(route = MainDestination.route) {
            DashboardScreen(
                onNavigateToDetailRest = onNavigateToDetailRest,
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
                onNavigateToPayment = onNavigateToPayment,
                onNavigateToLogin = onNavigateToLoginFromHome,
                onNavigateToMessage = onNavigateToMessage,
                onNavigateToEditProfile = onNavigateToEditProfile,
                onNavigateToTerm = onNavigateToTerm
            )
        }

        composable(route = SearchDestination.route) {
            SearchScreen(
                onNavigateBack = onNavigateBack,
                onNavigateToDetail = onNavigateToDetailRest
            )
        }

        composable(route = NearbyDestination.route) {
            NearbyRestaurantScreen(
                onNavigateBack = onNavigateBack
            )
        }

        composable(
            route = DetailRestaurantDestination.routeWithArgs,
            arguments = listOf(
                navArgument(DetailRestaurantDestination.idArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val snackHostState = remember { SnackbarHostState() }

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
                onNavigateBack = onNavigateBack,
                onNavigateToDetailMenu = onNavigateToDetailMenu,
                onNavigateToBestSeller=onNavigateToBestSeller,
                onNavigateToReview = onNavigateToReview,
                onNavigateToVoucher = onNavigateToVoucher,
                onNavigateToDetailLocation = onNavigateToDetailLocation
            )
        }

        composable(
            route = DetailLocationDestination.route,
            arguments = listOf(
                navArgument(DetailLocationDestination.RESTAURANT_DATA_ARG) {
                    type = NavType.StringType
                }
            )
        ) {
            DetailLocationScreen(
                onNavigateBack = onNavigateBack
            )
        }

        composable(
            route = ReviewDestination.routeWithArgs,
            arguments = listOf(
                navArgument(ReviewDestination.idArg) { type = NavType.StringType }
            )
        ){
            ReviewScreen(
                onNavigateBack = onNavigateBack
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
            BestSellerScreen(
                onNavigateBack = onNavigateBack,
                onNavigateToDetailMenu = onNavigateToDetailMenu
            )
        }

        composable(
            route = RecommendedDestination.route
        ){
            RecommendedRestaurantScreen(
                onNavigateBack = onNavigateBack,
                onNavigateToDetailRest = onNavigateToDetailRest
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

            CategoryScreen(
                name = name,
                image = image,
                onNavigateToDetail = onNavigateToDetailRest,
                onNavigateBack = onNavigateBack
            )
        }

        composable(route = CollectionDestination.route) { backStackEntry ->
            val snackHostState = remember { SnackbarHostState() }

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
                onDeleteSuccess = onDeleteSuccess,
                onNavigateToDetailRest = onNavigateToDetailRest
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
            VoucherScreen(
                onNavigateBack = onNavigateBack
            )
        }

        composable(
            route = AddressDestination.route,
        ) {
            AddressScreen(
                onNavigateBack = onNavigateBack,
                onNavigateToAddAddress = onNavigateToAddAddress
            )
        }

        composable(
            route = AddAddressDestination.route
        ) {
            AddAddressScreen(
                onNavigateBack = onNavigateBack
            )
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
                onNavigateBack = onNavigateBack,
                onNavigateToOrderProcess = onNavigateToOrderProcess
            )
        }

        composable(
            route = CardDestination.route,
        ) {
            CardScreen(
                onNavigateBack = onNavigateBack,
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
                onNavigateBack = onNavigateBack,
                onNavigateToPayment = onNavigateToPayment,
                onNavigateToOrderProcess = onNavigateFromOrderToOrderProcess,
                onNavigateToOrderDetail = onNavigateToOrderDetail
            )
        }

        composable(
            route = OrderProcessDestination.routeWithArgs,
        ) {
            OrderProcessScreen(
                onNavigateBack = onNavigateBack,
                onNavigateToMessage = onNavigateToMessage
            )
        }

        composable(
            route = OrderDetailDestination.routeWithArgs,
        ) {
            DetailOrderScreen(
                onNavigateToRating = onNavigateToRating,
                onNavigateBack = onNavigateBack
            )
        }

        composable(
            route = RatingDestination.routeWithArgs,
            arguments = listOf(
                navArgument(ratingDataArg) { type = NavType.StringType }
            )
        ) {
            RatingScreen(
                onNavigateBack = onNavigateBack
            )
        }


        composable(
            route = MessageDestination.routeWithArgs,
        ) {
            MessageScreen(
                onNavigateBack = onNavigateBack
            )
        }

        composable(
            route = EditProfileDestination.route,
        ) {
            EditProfileScreen(
                onNavigateBack = onNavigateBack
            )
        }

        composable(
            route = TermDestination.route,
        ) {
            TermsScreen(
                onNavigateBack = onNavigateBack
            )
        }
    }
}