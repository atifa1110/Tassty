package com.example.tassty.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.mapper.FilterCategory
import com.example.core.ui.model.CartItemUiModel
import com.example.core.ui.model.CategoryUiModel
import com.example.core.ui.model.CollectionMenuUiModel
import com.example.core.ui.model.CollectionRestaurantUiModel
import com.example.core.ui.model.CollectionRestaurantWithMenuUiModel
import com.example.core.ui.model.FilterOptionUi
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.RestaurantMenuUiModel
import com.example.core.ui.model.RestaurantUiModel
import com.example.core.ui.model.UserAddressUiModel
import com.example.core.ui.model.VoucherUiModel
import com.example.tassty.categories
import com.example.tassty.menusItem
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500
import kotlin.collections.orEmpty

@Composable
fun HorizontalTitleSection(
    modifier: Modifier = Modifier,
    title : String,
    content: LazyListScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderListBlackTitle(
            modifier = Modifier.padding(horizontal = 24.dp),
            title = title,
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
    }
}

@Composable
fun HorizontalTitleButtonSection(
    title : String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderListTitleButton(
            modifier = Modifier.padding(horizontal = 24.dp),
            title = title,
            titleColor = Neutral100,
            onClick = onClick
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
    }
}

@Composable
fun HorizontalTitleItemCountSection(
    modifier: Modifier = Modifier,
    itemCount: Int,
    headerText: String,
    content: LazyListScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderListItemCountTitle(
            itemCount = itemCount,
            title = headerText,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
    }
}

@Composable
fun HorizontalTitleItemCountButtonSection(
    modifier: Modifier = Modifier,
    itemCount: Int,
    title: String,
    onClick: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderListItemCountTitleButton(
            itemCount = itemCount,
            title = title,
            onClick = onClick,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
    }
}

@Composable
fun VerticalTitleItemCountSection(
    modifier: Modifier = Modifier,
    itemCount: Int,
    headerText: String,
    content: LazyListScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderListItemCountTitle(
            itemCount = itemCount,
            title = headerText,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
    }
}


@Composable
fun HorizontalTitleSubtitleSection(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    onSeeAllClick: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderListTitleSubtitleButton(
            title = title,
            subtitle = subtitle,
            onClick = onSeeAllClick,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
    }
}

fun LazyListScope.restaurantMenuListBlock(
    itemCount: Int,
    headerText: String,
    restaurantItems: List<RestaurantMenuUiModel>,
    onNavigateToDetail: (String) -> Unit
) {
    item {
        HeaderListItemCountTitle(
            itemCount = itemCount,
            title = headerText,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(Modifier.height(12.dp))
    }

    items(
        items = restaurantItems,
        key = { it.restaurant.id }
    ) { restaurant ->
        RestaurantContentSection(
            restaurant = restaurant.restaurant,
            menus = restaurant.menus,
            onNavigateToDetail = onNavigateToDetail
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

fun LazyListScope.collectionList(
    items: List<CollectionRestaurantWithMenuUiModel>
){
    val totalMenus = items.sumOf { it.menus.size }
    item {
        HeaderListItemCountTitle(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            itemCount = totalMenus,
            title = " Menus"
        )
    }

    items.forEachIndexed { index, section ->
        collectionMenuWithRestaurant(
            restaurant = section.restaurant,
            items = section.menus,
            showDivider = index != 0
        )
    }
}

fun LazyListScope.collectionMenuWithRestaurant(
    restaurant: CollectionRestaurantUiModel,
    items: List<CollectionMenuUiModel>,
    showDivider : Boolean = false
) {
    item {
        if (showDivider) {
            DashedDivider(modifier = Modifier.padding(24.dp))
        }
        HeaderRestaurantCollection(
            modifier = Modifier.padding(horizontal = 24.dp),
            restaurantName = restaurant.name,
            rating = restaurant.ratingText,
            city = restaurant.city
        )
        Spacer(Modifier.height(12.dp))
    }

    items(
        items = items,
        key = {  menu ->"${restaurant.id}-${menu.id}" }
    ) { item ->
        Column (modifier = Modifier.padding(horizontal = 24.dp)){
            FoodCollectionCard(item)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun RestaurantContentSection(
    restaurant: RestaurantUiModel,
    menus: List<MenuUiModel>,
    onNavigateToDetail: (String) -> Unit
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Column (Modifier.padding(horizontal = 24.dp)){
            RestaurantLargeListCard(restaurant = restaurant,
                onClick = {onNavigateToDetail(restaurant.id)})
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(menus, key = { it.id }) { menuItem ->
                FoodTinyGridCard(menu = menuItem)
            }
        }
    }
}

fun LazyListScope.cartVerticalListBlock(
    cart : List<CartItemUiModel>,
    selectAll: Boolean,
    headerText: String,
    onSelectAllClicked:(Boolean) -> Unit,
    onCartSelectionChange:(String) -> Unit,
    onIncrementQuantity:(String) -> Unit,
    onDecrementQuantity:(String) -> Unit,
    onRemoveItemClicked: (String) -> Unit,
    onRevealChange: (Int, Boolean) -> Unit
){
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ItemCountTitleText(itemCount = cart.size, title = headerText)
            Row(
                verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "select all",
                    style = LocalCustomTypography.current.h6Regular,
                    color = Neutral70
                )
                Spacer(Modifier.width(8.dp))
                Checkbox(
                    checked = selectAll,
                    onCheckedChange = onSelectAllClicked,
                    enabled = true,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Orange500,
                        uncheckedColor = Neutral40
                    ),
                    modifier = Modifier
                        .padding(0.dp)
                        .size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }

    itemsIndexed(items= cart){ index, cartItem ->
       Column(Modifier.padding(horizontal = 24.dp)) {
           SwipeableItemWithActions(
               isRevealed = cartItem.isSwipeActionVisible,
               onExpanded = {
                   onRevealChange(index, true)
               },
               onCollapsed = {
                   onRevealChange(index, false)
               },
               actions = {
                   Box(
                       modifier = Modifier
                           .fillMaxHeight()
                           .width(72.dp)
                           .clip(
                               RoundedCornerShape(
                                   topStart = 20.dp,
                                   bottomStart = 20.dp
                               )
                           )
                           .background(Pink500),
                       contentAlignment = Alignment.Center
                   ) {
                       IconButton(onClick = {onRemoveItemClicked(cartItem.cartId)}) {
                           Icon(
                               imageVector = Icons.Default.Delete,
                               contentDescription = "Delete",
                               tint = Color.White,
                               modifier = Modifier.size(28.dp)
                           )
                       }
                   }
               }
           ) {
               CartListCard(
                   cart = cartItem,
                   onCheckedChange = onCartSelectionChange,
                   onIncrementQuantity = { onIncrementQuantity(cartItem.cartId) },
                   onDecrementQuantity = { onDecrementQuantity(cartItem.cartId) }
               )
           }
       }
        Spacer(Modifier.height(8.dp))
    }
}

fun LazyListScope.restaurantRecommendedSection(
    resource: Resource<List<RestaurantUiModel>>,
    onNavigateToDetail: (String) -> Unit
){
    val items = resource.data.orEmpty()
    when{
        resource.isLoading ->{
            item{
                LoadingRowState()
            }
        }
        resource.errorMessage!=null  ->{
            item {
                ErrorScreen()
            }
        }
        else -> {
            restaurantVerticalListBlock(
                headerText = "Recommended Restaurants",
                restaurantItems = items,
                onNavigateToDetail = onNavigateToDetail
            )
        }
    }
}

fun LazyListScope.restaurantVerticalListBlock(
    headerText: String,
    restaurantItems: List<RestaurantUiModel>,
    onNavigateToDetail: (String) -> Unit
) {
    item {
        HeaderListItemCountTitle(
            itemCount = restaurantItems.size,
            title = headerText,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
    }

    items(
        items = restaurantItems,
        key = { it.id }
    ) { restaurant ->
        Box(modifier = Modifier.padding(horizontal = 24.dp)) {
            RestaurantLargeListCard(
                restaurant = restaurant,
                onClick= { onNavigateToDetail(restaurant.id) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

fun LazyListScope.voucherVerticalListBlock(
    headerText: String,
    voucherItems: List<VoucherUiModel>,
    onNavigateToDetail: (String) -> Unit
) {
    item {
        HeaderListItemCountTitle(
            itemCount = voucherItems.size,
            title = headerText,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
    }

    items(
        items = voucherItems,
        key = { it.id }
    ) { voucher ->
        Box(modifier = Modifier.padding(horizontal = 24.dp)) {
            VoucherExtraLargeCard(voucher = voucher)
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

fun LazyListScope.addressVerticalListBlock(
    headerText: String,
    addressItems: List<UserAddressUiModel>,
) {
    item {
        HeaderListItemCountTitle(
            itemCount = addressItems.size,
            title = headerText,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
    }

    items(
        items = addressItems,
        key = { it.id }
    ) { address ->
        Box(modifier = Modifier.padding(horizontal = 24.dp)) {
            LocationLardCard(address)
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}


fun LazyListScope.menuItemCountVerticalListBlock(
    headerText: String,
    menus : List<MenuUiModel>,
    onFavoriteClick: (MenuUiModel) -> Unit,
    onAddToCart: (MenuUiModel) -> Unit,
    onNavigateToDetailMenu:(String) -> Unit
) {
    item {
        HeaderListItemCountTitle(
            itemCount = menus.size,
            title = headerText,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(Modifier.height(12.dp))
    }

    items(
        items = menus,
        key = { it.id }
    ) { menu ->
        Box(modifier = Modifier.padding(horizontal = 24.dp)) {
            FoodListCard(
                menu = menu,
                onFavoriteClick = { onFavoriteClick(menu) },
                onAddToCart ={
                    if(menu.customizable) onNavigateToDetailMenu(menu.id)
                    else onAddToCart(menu)
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun GridMenuListSection(
    isDetail: Boolean = false,
    title: String,
    menuItems: List<MenuUiModel>,
    onFavoriteClick: (MenuUiModel) -> Unit,
    onAddToCartClick:(MenuUiModel)-> Unit,
    onNavigateToDetailMenu:(String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderListBlackTitle(
            title = title,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            maxItemsInEachRow = 2
        ) {
            menuItems.forEach { item ->
                FoodLargeGridCard(
                    isDetail = isDetail,
                    menu = item,
                    onFavoriteClick = { onFavoriteClick(item) },
                    onAddToCart = {
                        if(item.customizable) onNavigateToDetailMenu(item.id) else onAddToCartClick(item)
                    },
                    modifier = Modifier
                        .weight(1f, fill = true)
                )
            }
        }
    }
}

@Composable
fun FoodCategoryGrid (
    searchQuery: String,
    filteredCategories: List<CategoryUiModel>,
    selectedCategoryIds: List<String>,
    onSelectedCategory: (String) -> Unit,
    modifier : Modifier = Modifier
){
    Column (
        modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        CategoryFoundHeader(
            searchQuery = searchQuery,
            filteredCategories = filteredCategories,
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredCategories) { category ->
                val isSelected = selectedCategoryIds.contains(category.id)
                FoodCategoryCard(
                    isSelected = isSelected,
                    category = category,
                    onCardClick = { onSelectedCategory(category.id) }
                )
            }
        }
    }
}

@Composable
fun FilterSection(
    option : List<FilterOptionUi>,
    onSortClick: () -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(option) { option ->
            CustomFilterChip(
                option = option,
                onClick = {
                    if(option.category == FilterCategory.SORT){
                        onSortClick()
                    }
                }
            )
        }
    }
}

fun LazyListScope.filterListSection(
    resource: Resource<List<RestaurantMenuUiModel>>,
    onNavigateToDetail: (String) -> Unit
){
    val items = resource.data.orEmpty()
    when{
        resource.isLoading ->{
            item{
                LoadingRowState()
            }
        }
        resource.errorMessage!=null || items.isEmpty()->{
            item {
                ErrorScreen()
            }
        }
        else -> {
            restaurantMenuListBlock(
                itemCount = items.size,
                headerText = "Search founds",
                restaurantItems = items,
                onNavigateToDetail = onNavigateToDetail
            )
        }
    }
}


@Preview(showBackground =true)
@Composable
fun PreviewList(){
    Column (Modifier.fillMaxSize()){
        GridMenuListSection(
            title = "Suggested menu for you!",
            menuItems = menusItem,
            onFavoriteClick = {},
            onAddToCartClick = {},
            onNavigateToDetailMenu = {}
        )

        FoodCategoryGrid(
            searchQuery = "",
            filteredCategories = categories,
            selectedCategoryIds = arrayListOf("1","2","3"),
            onSelectedCategory = {}
        )
    }
}