package com.example.tassty.screen

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.component.CartAddButton
import com.example.tassty.component.DashedDivider
import com.example.tassty.component.DetailMenuTopAppBar
import com.example.tassty.component.FoodPriceBigText
import com.example.tassty.component.FoodPriceLineText
import com.example.tassty.component.ItemImage
import com.example.tassty.component.MenuStockStatus
import com.example.tassty.component.NotesBarSection
import com.example.tassty.component.OptionCard
import com.example.tassty.component.QuantityButton
import com.example.tassty.component.RestaurantMenuInfoCard
import com.example.tassty.handleSelectionChange
import com.example.tassty.menuItem
import com.example.tassty.menuSections
import com.example.tassty.model.MenuChoiceSection
import com.example.tassty.model.MenuItemOption
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500

@Composable
fun DetailMenuScreen() {
    var menu by remember { mutableStateOf(menuItem) }
    var notesValue by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(1) }
    var menuSectionsList by remember {
        mutableStateOf(menuSections)
    }

    Scaffold(
        containerColor = Neutral10,
        bottomBar = {
            ProductAddToCartBottomBar(
                quantity = quantity,
                enableDecrease = quantity > 1,
                onIncreaseQuantity = { quantity++ },
                onDecreaseQuantity = { if (quantity > 1) quantity-- }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            //1. Gambar & Top AppBar (Bagian atas yang tingginya 370.dp)
            item {
                Box(Modifier.height(370.dp).fillMaxWidth()) {
                    ItemImage(
                        imageUrl = menu.imageUrl,
                        name = menu.name,
                        status = RestaurantStatus.OPEN,
                        modifier = Modifier.fillMaxSize()
                    )
                    DetailMenuTopAppBar(
                        onBackClick = {},
                        onShareClick = {},
                        onFavoriteClick = {}
                    )

                    if(menu.stock==0) {
                        Box(modifier = Modifier.padding(horizontal = 24.dp)
                            .align(Alignment.BottomCenter)
                            .offset(y=20.dp)
                        ) {
                            MenuStockStatus()
                        }
                    }
                }
            }

            //2. Deskripsi Menu, Harga, dan Info Card
            item {
                Column(
                    // Padding horizontal dan top padding untuk konten
                    Modifier.padding(start = 24.dp, end = 24.dp, top = if(menu.stock>0) 24.dp else 40.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = menu.name,
                                style = LocalCustomTypography.current.h3Bold,
                                color = Neutral100
                            )
                            Text(
                                text = menu.description,
                                style = LocalCustomTypography.current.bodyMediumRegular,
                                color = Neutral70
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Center
                        ) {
                            FoodPriceBigText(
                                price = menu.price,
                                color = Orange500
                            )
                            FoodPriceLineText(
                                price = "5.000",
                                color = Neutral60
                            )
                        }
                    }
                    RestaurantMenuInfoCard(onReviewsClick = {})
                }
            }

            // --- Divider 1 ---
            item {
                HorizontalDivider(Modifier.padding(vertical = 32.dp))
            }

            itemsIndexed(menuSectionsList) { index, section ->
                ChoiceSection(
                    section = section,
                    onSectionUpdated = { updatedSection ->
                    // --- Logika Update State ---
                        // 1. Cari index dari section yang diperbarui
                        val sectionIndex = menuSectionsList.indexOf(section)

                        // 2. Buat list baru (immutable update)
                        val newList = menuSectionsList.toMutableList()

                        // 3. Ganti section lama dengan updatedSection
                        if (sectionIndex != -1) {
                            newList[sectionIndex] = updatedSection
                        }

                        // 4. Perbarui state
                        menuSections = newList
                    }
                )

                // Divider di antara Choice Sections
                if (index < menuSections.lastIndex) {
                    HorizontalDivider(Modifier.padding(vertical = 32.dp))
                }
            }

            item {
                HorizontalDivider(Modifier.padding(vertical = 32.dp))
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
                            text = "${notesValue.length} / 100",
                            style = LocalCustomTypography.current.bodyMediumRegular,
                            color = Neutral70
                        )
                    }
                    NotesBarSection(
                        value = notesValue,
                        onValueChange = { notesValue = it }
                    )
                }
            }
        }
    }
}

@Composable
fun ChoiceSection(
    section: MenuChoiceSection,
    onSectionUpdated: (MenuChoiceSection) -> Unit
) {
    // Helper function untuk memicu update state
    val onOptionToggled: (MenuItemOption) -> Unit = { toggledOption ->
        handleSelectionChange(section, toggledOption, onSectionUpdated)
    }

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
                onOptionToggled = onOptionToggled
            )

            // Tambahkan Garis Putus-putus Kustom setelah setiap item, kecuali yang terakhir
            if (index < section.options.lastIndex) {
                DashedDivider(
                    // Memberikan jarak vertikal yang bagus antara teks dan garis
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
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit
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
        CartAddButton(totalPrice = 28000)
    }
}

@Preview(showBackground = true)
@Composable
fun DetailMenuPreview() {
    DetailMenuScreen()
}
