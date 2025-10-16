package com.example.tassty.screen.detailmenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.domain.model.RestaurantStatus
import com.example.tassty.component.CartAddButton
import com.example.tassty.component.CollectionAddContent
import com.example.tassty.component.CollectionContent
import com.example.tassty.component.CollectionSaveContent
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.DashedDivider
import com.example.tassty.component.DetailMenuTopAppBar
import com.example.tassty.component.Divider32
import com.example.tassty.component.FoodPriceBigText
import com.example.tassty.component.FoodPriceLineText
import com.example.tassty.component.MenuStockStatus
import com.example.tassty.component.NotesBarSection
import com.example.tassty.component.OptionCard
import com.example.tassty.component.QuantityButton
import com.example.tassty.component.RestaurantMenuInfoCard
import com.example.tassty.component.StatusItemImage
import com.example.tassty.menuSections
import com.example.tassty.menus
import com.example.tassty.model.MenuChoiceSection
import com.example.tassty.model.MenuItemOption
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500

@Composable
fun DetailMenuScreen(
    viewModel: DetailMenuViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailMenuContent(
        uiState = uiState,
        onOptionToggle = { section,option ->
            viewModel.onEvent(DetailMenuEvent.OnOptionToggle(section,option))},
        onIncrementQuantity = {viewModel.onEvent(DetailMenuEvent.OnQuantityIncrease(it))},
        onDecreaseQuantity = {viewModel.onEvent(DetailMenuEvent.OnQuantityDecrease(it))},
        onNotesChange = {viewModel.onEvent(DetailMenuEvent.OnNotesChange(it))},
        onShowCollectionSheet = {viewModel.onEvent(DetailMenuEvent.OnShowCollectionSheet)},
        onAddToCartClick = {viewModel.onEvent(DetailMenuEvent.OnAddToCartClick)}
    )

    CustomBottomSheet(
        visible = uiState.isCollectionSheetVisible,
        onDismiss = { viewModel.onEvent(DetailMenuEvent.OnDismissCollectionSheet)}
    ) {
        CollectionContent(
            collections = uiState.collections,
            onCollectionSelected = { id -> viewModel.onEvent(DetailMenuEvent.OnCollectionSelected(id))},
            onSaveCollectionClick = { viewModel.onEvent(DetailMenuEvent.OnSaveCollectionClick)},
            onAddCollectionClick = { viewModel.onEvent(DetailMenuEvent.OnShowAddCollectionSheet)}
        )
    }

    CustomBottomSheet(
        visible = uiState.isSuccessSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        CollectionSaveContent(
            title = "Saved to “${uiState.savedCollectionName}” Collection!",
            subtitle = "Lorem ipsum dolor sit amet, consectetur \nadipiscing elit, sed do eiusmod.",
            onCheckCollection = {},
            onConfirmClick = { viewModel.onEvent(DetailMenuEvent.OnDismissSuccessSheet)}
        )
    }

    CustomBottomSheet(
        visible = uiState.isAddCollectionSheet,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        CollectionAddContent(
            onDismissClick = { viewModel.onEvent(DetailMenuEvent.OnDismissAddCollectionSheet)}
        )
    }
}

@Composable
fun DetailMenuContent(
    uiState: DetailMenuUiState,
    onOptionToggle:(MenuChoiceSection,MenuItemOption) -> Unit,
    onDecreaseQuantity: (Int) -> Unit,
    onIncrementQuantity: (Int) -> Unit,
    onNotesChange:(String) -> Unit,
    onShowCollectionSheet: () -> Unit,
    onAddToCartClick: () -> Unit
) {
    Scaffold(
        containerColor = Neutral10,
        bottomBar = {
            ProductAddToCartBottomBar(
                quantity = uiState.quantity,
                enableDecrease = uiState.quantity >= 1,
                totalPrice = uiState.cartTotalPrice,
                onIncreaseQuantity = { onIncrementQuantity(uiState.quantity) },
                onDecreaseQuantity = { onDecreaseQuantity(uiState.quantity) },
                onAddToCartClick = onAddToCartClick
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Box(Modifier.height(370.dp).fillMaxWidth()) {
                    StatusItemImage(
                        imageUrl = uiState.menu.menu.imageUrl,
                        name = uiState.menu.menu.name,
                        status = if(uiState.menu.menu.isAvailable) RestaurantStatus.OPEN else RestaurantStatus.CLOSED,
                        modifier = Modifier.fillMaxSize()
                    )
                    DetailMenuTopAppBar(
                        isFavorite = uiState.menu.isWishlist,
                        onBackClick = {},
                        onShareClick = {},
                        onFavoriteClick = onShowCollectionSheet
                    )

                    if(!uiState.menu.menu.isAvailable) {
                        Box(modifier = Modifier.padding(horizontal = 24.dp)
                            .align(Alignment.BottomCenter)
                            .offset(y=20.dp)
                        ) {
                            MenuStockStatus()
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = if(!uiState.menu.menu.isAvailable) 24.dp else 40.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = uiState.menu.menu.name,
                                style = LocalCustomTypography.current.h3Bold,
                                color = Neutral100
                            )
                            Text(
                                text = uiState.menu.menu.description,
                                style = LocalCustomTypography.current.bodyMediumRegular,
                                color = Neutral70
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Center
                        ) {

                            FoodPriceBigText(
                                price = uiState.menu.formatPrice,
                                color = Orange500
                            )

                            if(uiState.menu.menu.formatDiscountPrice>0) {
                                FoodPriceLineText(
                                    price = uiState.menu.formatOriginalPrice,
                                    color = Neutral60
                                )
                            }
                        }
                    }
                    RestaurantMenuInfoCard(onReviewsClick = {})
                }
            }

            // --- Divider 1 ---
            item {
                Divider32()
            }

            itemsIndexed(uiState.menuChoiceSections) { index, section ->
                ChoiceSection(
                    section = section,
                    onSectionUpdated = onOptionToggle
                )

                // Divider di antara Choice Sections
                if (index < menuSections.lastIndex) {
                    Divider32()
                }
            }

            item {
                Divider32()
            }

            // 5. Notes Section
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Notes",
                            style = LocalCustomTypography.current.h5Bold,
                            color = Neutral100
                        )
                        Text(
                            text = "${uiState.notesValue.length} / 100",
                            style = LocalCustomTypography.current.bodyMediumRegular,
                            color = Neutral70
                        )
                    }
                    NotesBarSection(
                        value = uiState.notesValue,
                        onValueChange = onNotesChange
                    )
                }
            }
        }
    }
}

@Composable
fun ChoiceSection(
    section: MenuChoiceSection,
    onSectionUpdated: (MenuChoiceSection, MenuItemOption) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()
        .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = section.title,
                style = LocalCustomTypography.current.h5Bold,
                color = Neutral100
            )
            Text(
                text = if(section.maxSelection > 1) "pick 2" else section.subtitle,
                style = LocalCustomTypography.current.bodyMediumRegular,
                color = Color.Gray
            )
        }

        Spacer(Modifier.height(8.dp))

        // List of options
        section.options.forEachIndexed { index, option ->
            OptionCard(
                option = option,
                section = section,
                isSelected = section.selectedOptions.contains(option),
                onOptionToggled = {onSectionUpdated(section,it)}
            )

            if (index < section.options.lastIndex) {
                DashedDivider(
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ProductAddToCartBottomBar(
    quantity : Int,
    enableDecrease: Boolean,
    totalPrice : Int,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onAddToCartClick: () -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxWidth().background(Neutral10)
            .padding(start= 24.dp, end = 24.dp,top = 24.dp, bottom = 36.dp),
    ){
        // 1. Quantity Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Quantity",
                style = LocalCustomTypography.current.h5Bold,
                color = Neutral100,
                modifier = Modifier.weight(1f)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Decrease Button
                QuantityButton(
                    onClick = onDecreaseQuantity,
                    enabled = enableDecrease,
                    icons = Icons.Filled.Remove,
                    contentDescription = "Decrease Quantity"
                )

                // Quantity Text
                Text(
                    text = quantity.toString(),
                    style = LocalCustomTypography.current.h5Bold,
                    color = Neutral100,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                // Increase Button
                QuantityButton(
                    onClick = onIncreaseQuantity,
                    enabled = true,
                    icons = Icons.Filled.Add,
                    contentDescription = "Increase Quantity"
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        CartAddButton(totalPrice = totalPrice, onClick = onAddToCartClick)
    }
}

@Preview(showBackground = true)
@Composable
fun DetailMenuPreview() {
    DetailMenuContent(
        uiState = DetailMenuUiState(
            menu = menus[0],
            menuChoiceSections = menuSections
        ),
        onOptionToggle = {_,_->},
        onIncrementQuantity = {},
        onDecreaseQuantity = {},
        onNotesChange = {},
        onAddToCartClick = {},
        onShowCollectionSheet = {}
    )
}
