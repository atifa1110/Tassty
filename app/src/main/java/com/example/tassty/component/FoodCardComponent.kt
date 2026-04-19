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
import com.example.core.ui.model.CollectionMenuUiModel
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.OptionUiModel
import com.example.tassty.R
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500
import com.example.tassty.util.CollectionData
import com.example.tassty.util.MenuPreviewData

@Composable
fun FoodTinyGridCard(
    menu : MenuUiModel
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
                menu = menu,
                modifier = Modifier.size(80.dp)
            )

            FoodTinyGridCardContent(menu = menu)
        }
    }
}

@Composable
fun FoodGridCard(
    menu: MenuUiModel,
    onFavoriteClick: (MenuUiModel) -> Unit,
    onAddToCart:() -> Unit
) {
    Card(
        modifier = Modifier.width(140.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.cardBackground),
    ) {
        Column (modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            FoodCircleImageWithOverlays(
                menu = menu,
                onFavoriteClick = { onFavoriteClick(menu) },
                modifier = Modifier.height(120.dp)
            )

            FoodGridCardContent(menu = menu,onAddToCart = onAddToCart)
        }
    }
}


@Composable
fun FoodGridSoldCard(
    menu: MenuUiModel,
    onFavoriteClick: (MenuUiModel) -> Unit,
    onAddToCart:() -> Unit
) {
    Card(
        modifier = Modifier.width(140.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.cardBackground),
    ) {
        Column (modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            FoodCircleImageWithOverlays(
                menu = menu,
                onFavoriteClick = { onFavoriteClick(menu) },
                modifier = Modifier.height(120.dp)
            )

            FoodGridSoldCardContent(menu = menu,onAddToCart = onAddToCart)
        }
    }
}

@Composable
fun FoodNameGridCard(
    menu: MenuUiModel
) {
    Card(
        modifier = Modifier.width(140.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.cardBackground),
    ) {
        Column (modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            FoodCircleImageWithOverlays(
                menu = menu,
                onFavoriteClick = {},
                modifier = Modifier.height(120.dp)
            )

            FoodNameGridCardContent(menu = menu)
        }
    }
}

@Composable
fun FoodLargeGridCard(
    modifier: Modifier = Modifier,
    isDetail: Boolean = false,
    menu: MenuUiModel,
    onFavoriteClick: () -> Unit,
    onAddToCart: () -> Unit
){
    Card(
        modifier = modifier.width(157.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalCustomColors.current.cardBackground
        ),
    ) {
        Column (modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            FoodCircleImageWithOverlays(
                menu = menu,
                onFavoriteClick = onFavoriteClick,
                modifier = Modifier.height(137.dp)
            )

            FoodGridCardContent(isDetail = isDetail, menu = menu, onAddToCart = onAddToCart)
        }
    }
}

@Composable
fun FoodListCard(
    menu: MenuUiModel,
    onFavoriteClick: () -> Unit,
    onAddToCart:() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.cardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FoodRoundImageWithOverlays(
                menu = menu,
                modifier = Modifier.size(100.dp)
            )

            FoodListCardContent(
                menu = menu,
                onFavoriteClick = onFavoriteClick,
                onAddToCart = onAddToCart
            )
        }
    }
}

@Composable
fun FoodWideListCard(
    menu: MenuUiModel,
    onFavoriteClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.cardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FoodRoundImageWithOverlays(
                menu = menu,
                modifier = Modifier.size(120.dp)
            )

            FoodWideListCardContent(
                menu = menu,
                onFavoriteClick = onFavoriteClick,
                onAddToCart = onAddToCart
            )
        }
    }
}

@Composable
fun FoodCollectionCard(
    collection: CollectionMenuUiModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.cardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FoodRoundImageWithOverlays(
                collection = collection,
                modifier = Modifier.size(100.dp)
            )

            FoodListCollectionCardContent(
                collection = collection
            )
        }
    }
}

@Composable
fun OptionCard(
    maxPick: Int,
    enabled: Boolean,
    option: OptionUiModel,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) {
                onClick()
            }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = option.name,
            style = LocalCustomTypography.current.h6Regular,
            color = if(option.isAvailable) LocalCustomColors.current.headerText else LocalCustomColors.current.text,
            modifier = Modifier.weight(1f)
        )

        if(option.isAvailable) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = option.extraPriceText,
                    style = LocalCustomTypography.current.h6Regular,
                    color = LocalCustomColors.current.text
                )
                Spacer(Modifier.width(8.dp))
                if (maxPick > 1) {
                    // Checkbox: if maxSelection > 1
                    Checkbox(
                        checked = option.isSelected,
                        onCheckedChange = null,
                        enabled = enabled,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Orange500,
                            uncheckedColor = Neutral40
                        ),
                        modifier = Modifier
                            .padding(0.dp)
                            .size(24.dp)
                    )
                } else {
                    RadioButton(
                        selected = option.isSelected,
                        onClick = null,
                        enabled = enabled,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Orange500,
                            unselectedColor = Neutral40
                        ),
                        modifier = Modifier
                            .padding(0.dp)
                            .size(24.dp)
                    )
                }
            }
        }else{
            Text(
                text = "Out of stock",
                style = LocalCustomTypography.current.h6Regular,
                color = Pink500
            )
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
                color = Neutral10,
                style = LocalCustomTypography.current.bodySmallSemiBold
            )
            Spacer(Modifier.width(2.dp))
            Text(
                text = stringResource(R.string.this_menu_is_out_stock),
                color = Neutral10,
                style = LocalCustomTypography.current.bodySmallMedium
            )
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun FoodGridCardPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FoodTinyGridCard(menu = MenuPreviewData.menuUiList[0])
            FoodGridCard(menu = MenuPreviewData.menuUiList[0], onFavoriteClick = {}, onAddToCart = {})
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FoodLargeGridCard(menu = MenuPreviewData.menuUiList[0],onFavoriteClick = {}, onAddToCart = {})
            FoodGridSoldCard(menu = MenuPreviewData.menuUiList[0], onFavoriteClick = {}, onAddToCart = {})
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun FoodListCardPreview() {
    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FoodListCard(menu = MenuPreviewData.menuUiList[0], onFavoriteClick = {}, onAddToCart = {})
        FoodWideListCard(menu = MenuPreviewData.menuUiList[0], onFavoriteClick = {}, onAddToCart = {})
        FoodCollectionCard(collection = CollectionData.collectionMenuUiModel[0], onClick = {})
    }
}
