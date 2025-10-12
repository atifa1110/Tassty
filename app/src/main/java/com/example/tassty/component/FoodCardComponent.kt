package com.example.tassty.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.menuItem
import com.example.tassty.menuSections
import com.example.tassty.model.Menu
import com.example.tassty.model.MenuChoiceSection
import com.example.tassty.model.MenuItemOption
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.toCleanRupiahFormat
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodTinyGridCard(
    menu : Menu,
    status : RestaurantStatus
) {
    Card(
        modifier = Modifier.width(96.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column (modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            FoodImageWithFloating(
                menu = menu, status = status,
                modifier = Modifier.size(80.dp)
            )

            FoodTinyGridCardContent(menu = menu)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodGridCard(
    menu: Menu,
    status: RestaurantStatus,
    isFirstItem: Boolean,
    onFavoriteClick: (String) -> Unit,
    onAddToCart:(Menu) -> Unit
) {
    Card(
        modifier = Modifier.width(140.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral20),
    ) {
        Column (modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            FoodCircleImageWithOverlays(
                menu = menu,
                status = status,
                isFirstItem = isFirstItem,
                onFavoriteClick = { onFavoriteClick(menu.id) },
                modifier = Modifier.height(120.dp)
            )

            FoodGridCardContent(menu = menu,onAddToCart = {onAddToCart(menu)})
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodNameGridCard(
    menu: Menu,
    status: RestaurantStatus,
    isFirstItem: Boolean,
    isWishlist: Boolean,
) {
    Card(
        modifier = Modifier.width(140.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral20),
    ) {
        Column (modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            FoodCircleImageWithOverlays(
                menu = menu,
                status = status,
                isFirstItem = isFirstItem,
                onFavoriteClick = {},
                modifier = Modifier.height(120.dp)
            )

            FoodNameGridCardContent(menu = menu)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodLargeGridCard(
    modifier: Modifier = Modifier,
    menu: Menu,
    status: RestaurantStatus,
    isFirstItem: Boolean,
    onFavoriteClick: () -> Unit
){
    Card(
        modifier = modifier.width(157.dp), // Adjust width as needed
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Neutral20
        ),
    ) {
        Column (modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            FoodCircleImageWithOverlays(
                menu = menu,
                status = status,
                isFirstItem = isFirstItem,
                onFavoriteClick = onFavoriteClick,
                modifier = Modifier.height(137.dp)
            )

            FoodGridCardContent(menu = menu, onAddToCart = {})
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodListCard(
    menu: Menu,
    status: RestaurantStatus,
    isFirstItem: Boolean,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral20)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FoodRoundImageWithOverlays(
                menu = menu,
                status = status,
                isFirstItem = isFirstItem,
                modifier = Modifier.size(100.dp, 84.dp)
            )

            FoodListCardContent(
                menu = menu,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodWideListCard(
    menu: Menu,
    status: RestaurantStatus,
    isFirstItem: Boolean,
    isWishlist: Boolean,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral20)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FoodRoundImageWithOverlays(
                menu = menu,
                status = status,
                isFirstItem = isFirstItem,
                modifier = Modifier.size(100.dp)
            )

            FoodWideListCardContent(menu = menu,
                isWishlist = isWishlist, onFavoriteClick = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderFoodListCard(
    menu: Menu
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral20)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FoodRoundImageWithOverlays(
                menu= menu,
                status = RestaurantStatus.OPEN,
                isFirstItem = false,
                modifier = Modifier.size(84.dp)
            )

            FoodOrderListCardContent(menu = menu)
        }
    }
}

@Composable
fun OptionCard(
    option: MenuItemOption,
    section: MenuChoiceSection,
    isSelected: Boolean,
    onOptionToggled: (MenuItemOption) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth().clickable { onOptionToggled(option) }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = option.name,
            style = LocalCustomTypography.current.h6Regular,
            color = Neutral100,
            modifier = Modifier.weight(1f)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "+${option.priceAddon.toCleanRupiahFormat()}",
                style = LocalCustomTypography.current.h6Regular,
                color = Neutral70
            )
            Spacer(Modifier.width(8.dp))
            if (section.maxSelection>1) {
                // Checkbox: if maxSelection > 1
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onOptionToggled(option) },
                    //enabled = isSelected || section.selectedOptions.size < section.maxSelection,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Orange500,
                        uncheckedColor = Neutral40
                    ),
                    modifier = Modifier.padding(0.dp).size(24.dp)
                )
            } else {
                RadioButton(
                    selected = isSelected,
                    onClick = { onOptionToggled(option) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Orange500,
                        unselectedColor = Neutral40
                    ),
                    modifier = Modifier.padding(0.dp).size(24.dp)
                )
            }
        }
    }
}

@Composable
fun MenuStockStatus(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .background(
                Pink500,
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.sad),
                tint = Neutral10,
                contentDescription = "menu stock is empty"
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.sorry),
                color = Color.White,
                style = LocalCustomTypography.current.bodySmallSemiBold
            )
            Spacer(Modifier.width(2.dp))
            Text(
                text = "This menu is out stock",
                color = Color.White,
                style = LocalCustomTypography.current.bodySmallMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodGridCardPreview() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FoodTinyGridCard(menu = menuItem, status = RestaurantStatus.OPEN)
            FoodNameGridCard(menu = menuItem, isFirstItem = true, status = RestaurantStatus.OPEN,isWishlist = false)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FoodGridCard(menu = menuItem, isFirstItem = true, status = RestaurantStatus.OPEN, onFavoriteClick = {}, onAddToCart = {})
            FoodGridCard(menu = menuItem, isFirstItem = false, status = RestaurantStatus.OPEN, onFavoriteClick = {}, onAddToCart = {})
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FoodLargeGridCard(menu = menuItem, isFirstItem = true,status = RestaurantStatus.OPEN,onFavoriteClick = {})
            FoodLargeGridCard(menu = menuItem, isFirstItem = false,status = RestaurantStatus.OPEN,onFavoriteClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodListCardPreview() {
    Column (modifier = Modifier.fillMaxWidth().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FoodListCard(menu = menuItem, isFirstItem = true, status = RestaurantStatus.OPEN, onFavoriteClick = {})
        FoodListCard(menu = menuItem, isFirstItem = false, status = RestaurantStatus.OPEN, onFavoriteClick = {})
        OrderFoodListCard(menu = menuItem)
    }
}

@Preview(showBackground = true)
@Composable
fun OptionCardPreview() {
    Column (verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OptionCard(
            option = MenuItemOption("1","Rare",4000),
            section = menuSections[0],
            isSelected = false,
            onOptionToggled = {}
        )
    }
}