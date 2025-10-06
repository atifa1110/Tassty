package com.example.tassty.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import com.example.tassty.R
import com.example.tassty.carts
import com.example.tassty.model.Cart
import com.example.tassty.model.Menu
import com.example.tassty.model.Restaurant
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink200
import com.example.tassty.ui.theme.Pink500
import kotlin.math.abs
import kotlin.math.roundToInt

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
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = title,
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100,
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
fun HorizontalListOrangeSection(
    modifier: Modifier = Modifier,
    data: Int,
    headerText: String,
    content: LazyListScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TitleListHeader(
            data = data,
            text = headerText
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
fun HorizontalListSection(
    modifier: Modifier = Modifier,
    headerText: String,
    onSeeAllClick: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderText(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = headerText,
            textColor = Neutral100,
            textButton = stringResource(R.string.see_all),
            onButtonClick = onSeeAllClick
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
fun HorizontalSubtitleListSection(
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
        HeaderSubtitleText(
            modifier = Modifier.padding(horizontal = 24.dp),
            title = title,
            subtitle = subtitle,
            titleColor = Neutral100,
            subtitleColor = Neutral70,
            textButton = stringResource(R.string.see_all),
            onButtonClick = onSeeAllClick
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
    restaurantItems: List<Restaurant>
) {
    item {
        TitleListHeader(
            data = itemCount,
            text = headerText,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
    }

    items(
        items = restaurantItems,
        key = { it.id }
    ) { restaurant ->
        RestaurantContentSection(
            restaurant = restaurant,
            status = RestaurantStatus.OPEN
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun RestaurantContentSection(
    restaurant: Restaurant,
    status: RestaurantStatus
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Column (Modifier.padding(horizontal = 24.dp)){
            RestaurantLargeListCard(restaurant = restaurant, status = status)
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(restaurant.menus, key = { it.id }) { menuItem ->
                FoodTinyGridCard(menu = menuItem,status = status)
            }
        }
    }
}

fun LazyListScope.cartVerticalListBlock(
    cart : List<Cart>,
    headerText: String,
    onRemoveCartItem: (Cart) -> Unit,
    onRevealChange: (Int, Boolean) -> Unit
){
    item {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TitleListHeader(
                data = cart.size,
                text = headerText,
            )
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "select all",
                    style = LocalCustomTypography.current.h6Regular,
                    color = Neutral70
                )
                Spacer(Modifier.width(8.dp))
                Checkbox(
                    checked = false,
                    onCheckedChange = {},
                    enabled = true,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Orange500,
                        uncheckedColor = Neutral40
                    ),
                    modifier = Modifier.padding(0.dp).size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }

    itemsIndexed(items= carts){ index, cartItem ->
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
                           .clip(RoundedCornerShape(topStart = 20.dp,
                               bottomStart = 20.dp)).background(Pink500),
                       contentAlignment = Alignment.Center
                   ) {
                       IconButton(onClick = { onRemoveCartItem(cartItem) }) {
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
               CartListCard(cart = cartItem, isChecked = false)
           }
       }
        Spacer(Modifier.height(8.dp))
    }
}

/**
 * Composable for the background content (the delete button).
 * Matches the bright pink/magenta color and trash can icon from the image.
 */
@Composable
fun DismissBackground(
    dismissState: SwipeToDismissBoxState,
    maxBackgroundWidth: Dp = 80.dp
) {
    val dismissDirection = dismissState.dismissDirection
    if (dismissDirection != SwipeToDismissBoxValue.EndToStart) return

    val color = Pink500 // Bright Pink/Magenta
    val offset = dismissState.requireOffset()

    // Calculate the visible width of the background.
    // The offset is negative for EndToStart swipe. abs() makes it positive.
    // We constrain the width to be between 0.dp and maxBackgroundWidth.
    val visibleWidth = max(0.dp, min(maxBackgroundWidth, abs(offset).toDp()))

    // *** KEY CHANGE: Use clipToBounds and fixed width alignment ***
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds() // Prevents content from drawing outside bounds
            .background(Color.Gray), // Use transparent background for the Box container
        contentAlignment = Alignment.CenterEnd
    ) {
        // The inner colored Box with controlled width
        Box(
            modifier = Modifier
                .width(visibleWidth) // Control the width based on swipe progress
                .fillMaxHeight()
                .background(color, shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                .padding(end = 24.dp), // Adjust padding if needed
            contentAlignment = Alignment.CenterEnd
        ) {
            // Icon is only visible when the Box has a positive width
            if (visibleWidth > 0.dp) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Neutral10,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun Float.toDp(): Dp = with(LocalDensity.current) { this@toDp.toDp() }

fun LazyListScope.restaurantVerticalListBlock(
    headerText: String,
    restaurantItems: List<Restaurant>,
    status: RestaurantStatus
) {
    item {
        TitleListHeader(
            data = restaurantItems.size,
            text = headerText,
        )
        Spacer(modifier = Modifier.height(12.dp))
    }

    items(
        items = restaurantItems,
        key = { it.id }
    ) { restaurant ->

        Box(modifier = Modifier.padding(horizontal = 24.dp)) {
            RestaurantLargeListCard(restaurant = restaurant, status = status)
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun GridRow(
    rowItems: List<Menu>,
    status: RestaurantStatus
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        rowItems.forEachIndexed { itemIndexInRow, item ->
            FoodLargeGridCard(
                menu = item,
                status = status,
                isFirstItem = item.id == 1,
                isWishlist = false,
                modifier = Modifier.weight(1f)
            )
        }

        if (rowItems.size == 1) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}