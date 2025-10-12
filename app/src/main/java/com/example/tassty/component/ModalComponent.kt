package com.example.tassty.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.cuisineOptions
import com.example.tassty.discountOptions
import com.example.tassty.model.Cart
import com.example.tassty.model.CollectionUiItem
import com.example.tassty.model.OperationalDay
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.model.UserAddress
import com.example.tassty.model.Voucher
import com.example.tassty.modesOptions
import com.example.tassty.restoRatingsOptions
import com.example.tassty.rupiahPriceRanges
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500
import kotlin.collections.take

@Composable
fun CustomBottomSheet(
    visible: Boolean,
    dismissOnClickOutside: Boolean = true,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val transition = updateTransition(targetState = visible,
        label = "BottomSheetTransition")

    // Animation for black background
    val scrimAlpha by transition.animateFloat(
        transitionSpec = { tween(300) }, label = "ScrimAlpha"
    ) { if (it) 0.5f else 0f }

    // Animation slide from bottom
    val offsetY by transition.animateDp(
        transitionSpec = { tween(300) }, label = "OffsetY"
    ) { show ->
        if (show) 0.dp else screenHeight
    }

    if (scrimAlpha > 0f || visible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = scrimAlpha))
                .clickable(
                    enabled = true, // Selalu aktif untuk menangkap klik
                    indication = null, // Tidak ada efek visual saat diklik
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    // Jika dismissOnClickOutside TRUE, panggil onDismiss
                    if (dismissOnClickOutside) {
                        onDismiss()
                    }
                    // Jika FALSE, klik tetap ditangkap di sini dan TIDAK diteruskan ke bawah.
                },
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(Modifier
                .offset(y = offsetY)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 24.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                content()

                Box(
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .size(width = 60.dp, height = 10.dp)
                        .clip(RoundedCornerShape(99.dp))
                        .background(Neutral10)
                        .shadow(
                            elevation = 4.dp,
                        )
                        .align(Alignment.TopCenter),
                    contentAlignment = Alignment.Center
                ) {
                    Spacer(
                        modifier = Modifier
                            .size(width = 25.dp, height = 1.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(Neutral70)
                    )
                }
            }
        }
    }
}

@Composable
fun CollectionContent(
    collections: List<CollectionUiItem>,
    onCollectionSelected: (String) -> Unit,
    onSaveCollectionClick:() -> Unit,
    onAddCollectionClick: () -> Unit
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(
            RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            )
        )
        .background(Neutral20)
        .padding(top = 24.dp, bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)){
                Text(
                    text = stringResource(R.string.save_to_collection),
                    style = LocalCustomTypography.current.h3Bold,
                    color = Neutral100
                )
                Text(
                    text = stringResource(R.string.save_your_favorite),
                    style = LocalCustomTypography.current.bodySmallMedium,
                    color = Neutral70
                )
            }

            TopBarButton(icon = Icons.Default.Add,
                boxColor = Orange500, iconColor = Neutral10
            ) { onAddCollectionClick() }
        }

        HorizontalDivider(Modifier.padding(vertical = 32.dp))
        HeaderListItemCountTitle(
            itemCount = collections.size,
            title = "Collection",
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(Modifier.height(12.dp))

        Column(Modifier.padding(horizontal = 24.dp)) {
            collections.take(2).forEach { collection ->
                CollectionCard(
                    collection = collection,
                    onCheckedChange = {onCollectionSelected(collection.collectionId)}
                )
                Spacer(Modifier.height(8.dp))
            }
            Spacer(Modifier.height(24.dp))
            ButtonComponent(
                enabled = collections.any { it.isSelected==true },
                labelResId = R.string.save,
                onClick = onSaveCollectionClick
            )
        }
    }
}

@Composable
fun CollectionAddContent(
    onDismissClick:() -> Unit
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(
            RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            )
        )
        .background(Neutral20)
        .padding(top = 24.dp, bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text= "Create collection",
                style = LocalCustomTypography.current.h3Bold,
                color = Neutral100
            )

            TopBarButton(icon = R.drawable.arrow_left,
                boxColor = Neutral10, iconColor = Neutral100
            ) { onDismissClick() }
        }

        HorizontalDivider(Modifier.padding(vertical = 32.dp))

        Column(Modifier.padding(horizontal = 24.dp)) {
            Text(
                text= "Collection name",
                style = LocalCustomTypography.current.h5Bold,
                color = Neutral100
            )
            Spacer(Modifier.height(16.dp))
            NotesBarSection(
                value="",
                onValueChange = {},
                icon = R.drawable.icon,
                placeholder = stringResource(R.string.add_collection_name)
            )
            Spacer(Modifier.height(24.dp))
            ButtonComponent(
                enabled = false,
                labelResId = R.string.create,
                onClick = {}
            )
        }
    }
}

@Composable
fun CollectionSaveContent(
    title: String,
    subtitle: String,
    onCheckCollection: () -> Unit,
    onConfirmClick:() -> Unit,
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Color.White)
            .padding(top = 24.dp, bottom = 46.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        ImageIcon(
            image = R.drawable.success,
            contentDescription = "Success Icon",
            modifier = Modifier.size(64.dp)
        )

        StatusTextHeader(title = title,subtitle = subtitle)

        HorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(text = "Check collection", onClick = onCheckCollection, textColor = Orange500, modifier = Modifier.weight(0.5f))
            TextButton(text = "Confirm", onClick = onConfirmClick, textColor = Orange500, modifier = Modifier.weight(0.5f))
        }
    }
}

@Composable
fun ModalStatusContent(
    title: String,
    subtitle: String,
    buttonTitle: String,
    onClick:() -> Unit,
    imageContent : @Composable () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Color.White)
            .padding(top = 24.dp, bottom = 46.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        imageContent()

        StatusTextHeader(title = title,subtitle = subtitle)

        HorizontalDivider()

        // Button
        TextButton(text = buttonTitle, textColor = Orange500, onClick = onClick)
    }
}

@Composable
fun DetailScheduleContent(
    operationalHours: List<OperationalDay>
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Neutral20)
            .padding(top = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ItemImage(
                    imageUrl = "",
                    status = RestaurantStatus.OPEN,
                    name = "Restaurant Modal Popup",
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stringResource(R.string.dummy_restaurant_name),
                        style = LocalCustomTypography.current.h3Bold,
                        color = Neutral100
                    )
                    Text(
                        text = stringResource(R.string.dummy_location),
                        style = LocalCustomTypography.current.bodySmallMedium,
                        color = Neutral70
                    )
                }
            }

            TopBarButton(icon = R.drawable.phone,
                boxColor = Orange500, iconColor = Neutral10
            ) {  }
        }

        HorizontalDivider()

        Column(
            modifier = Modifier.padding(horizontal=24.dp),
        ) {
            Text(
                text = "Operational Hour",
                style = LocalCustomTypography.current.h5Bold,
                color = Neutral100
            )
            Spacer(Modifier.height(12.dp))
            operationalHours.forEach { day ->
                RestaurantOperationalCard(day)
            }
        }

    }
}

@Composable
fun SortContent(){
    var selectedOption by remember { mutableStateOf("Nearest") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Neutral20)
            .padding(top = 24.dp, bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.sort_restos_by),
                style = LocalCustomTypography.current.h3Bold,
                color = Neutral100,
                modifier = Modifier.weight(1f)
            )
            ResetButton(onClick={})
        }

        HorizontalDivider(Modifier.padding(vertical = 32.dp))

        Column (Modifier.padding(horizontal = 24.dp)){
            SortOptionItemStandard(
                options = listOf("Recommended", "Nearest", "Popularity"),
                selected = selectedOption,
                onSelect = { selectedOption = it }
            )

            Spacer(Modifier.height(24.dp))

            ButtonComponent(
                enabled = true,
                labelResId = R.string.apply,
                onClick = {})
        }
    }
}

@Composable
fun FilterContent(){
    var selectedCuisineKey by remember { mutableStateOf(cuisineOptions.first().key) }
    var selectedPriceKey by remember { mutableStateOf(rupiahPriceRanges.first().key) }
    var selectedRating by remember { mutableStateOf(setOf("Rated 4.0+")) }
    var selectedDiscount by remember { mutableStateOf(setOf("All type promo")) }
    var selectedMode by remember { mutableStateOf(setOf("Delivery")) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Neutral20)
            .padding(top = 24.dp, bottom = 24.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.filter_search),
                    style = LocalCustomTypography.current.h3Bold,
                    color = Neutral100,
                    modifier = Modifier.weight(1f)
                )
                ResetButton(onClick = {})
            }
        }

        // Divider
        item { HorizontalDivider(Modifier.padding(vertical = 32.dp)) }

        // Price range
        item {
            RadioFilterSection(
                title = "Price range",
                options = rupiahPriceRanges,
                selectedKey = selectedPriceKey
            ) { selectedPriceKey = it }
        }

        item { HorizontalDivider(Modifier.padding(vertical = 32.dp)) }

        // Restaurant Rating
        item {
            ChipFilterSection(
                title = stringResource(R.string.resto_ratings),
                options = restoRatingsOptions,
                selectedKeys = selectedRating
            ) { key ->
                selectedRating = if (selectedRating.contains(key)) {
                    selectedRating - key
                } else {
                    selectedRating + key
                }
            }
        }

        item { HorizontalDivider(Modifier.padding(vertical = 32.dp)) }

        // Discounts
        item {
            ChipFilterSection(
                title = "Discounts",
                options = discountOptions,
                selectedKeys = selectedDiscount
            ) { key ->
                selectedDiscount = if (selectedDiscount.contains(key)) {
                    selectedDiscount - key
                } else {
                    selectedDiscount + key
                }
            }
        }

        item { HorizontalDivider(Modifier.padding(vertical = 32.dp)) }

        // Modes
        item {
            ChipFilterSection(
                title = "Modes",
                options = modesOptions,
                selectedKeys = selectedMode
            ) { key ->
                selectedMode = if (selectedMode.contains(key)) {
                    selectedMode - key
                } else {
                    selectedMode + key
                }
            }
        }

        item { HorizontalDivider(Modifier.padding(vertical = 32.dp)) }

        // Cuisines type
        item {
            RadioFilterSection(
                title = "Cuisines type",
                options = cuisineOptions,
                selectedKey = selectedPriceKey
            ) { selectedCuisineKey = it }
        }
    }
}

@Composable
fun CartRemoveMenuContent(
    cart:Cart?=null,
    onRemoveCartItem: (Cart) -> Unit,
    onDismiss: () -> Unit
){
    if(cart!=null){
        Column(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Neutral20)
            .padding(top = 24.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            OverlapImage(imageUrl = cart.imageUrl)

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Remove this menu",
                        style = LocalCustomTypography.current.h2Bold,
                        color = Neutral100
                    )
                    Text(
                        text = "?",
                        style = LocalCustomTypography.current.h2Bold,
                        color = Pink500
                    )
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "You can add this menu back from the \n" +
                            "restaurant details page",
                    style = LocalCustomTypography.current.bodyMediumRegular,
                    color = Neutral70,
                    textAlign = TextAlign.Center
                )
            }

            HorizontalDivider(color = Neutral40)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    text = "Cancel",
                    textColor = Pink500,
                    onClick = onDismiss,
                    modifier = Modifier.weight(0.5f)
                )

                TextButton(
                    text = "Remove",
                    textColor = Neutral70,
                    onClick = {onRemoveCartItem(cart)},
                    modifier = Modifier.weight(0.5f)
                )
            }
        }
    }
}

@Composable
fun CartEditContent(){
    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        .background(Neutral20)
        .padding(top = 24.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text= "Edit Notes",
                style = LocalCustomTypography.current.h3Bold,
                color = Neutral100
            )

            TopBarButton(icon = Icons.Default.Clear,
                boxColor = Neutral10, iconColor = Neutral100
            ) {  }
        }

        HorizontalDivider(color= Neutral30)

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ){
            Text(
                text= "Notes",
                style = LocalCustomTypography.current.h5Bold,
                color = Neutral100
            )
            Spacer(Modifier.height(16.dp))
            NotesBarSection(
                value = "",
                onValueChange = {}
            )
            Spacer(Modifier.height(24.dp))
            ButtonComponent(
                enabled = true,
                labelResId = R.string.update
            ) { }
        }
    }
}

@Composable
fun CartDeliveryLocationContent(
    address: List<UserAddress>,
    onAddressChange: (String) -> Unit,
    onSetLocationClicked:() -> Unit,
    onDismiss: () -> Unit
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        .background(Neutral20)
        .padding(top = 24.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Choose delivery location",
                    style = LocalCustomTypography.current.h3Bold,
                    color = Neutral100
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = LocalCustomTypography.current.bodySmallMedium.toSpanStyle().copy(color = Neutral70)) {
                            append("You can add a new delivery address on the \n")
                        }
                        withStyle(style = LocalCustomTypography.current.bodySmallMedium.toSpanStyle().copy(color = Neutral100)) {
                            append("My Addresses ")
                        }
                        withStyle(style = LocalCustomTypography.current.bodySmallMedium.toSpanStyle().copy(color = Neutral70)) {
                            append("page")
                        }
                    },
                )
            }

            TopBarButton(icon = Icons.Default.Clear,
                boxColor = Neutral10, iconColor = Neutral100
            ) { onDismiss() }
        }

        HorizontalDivider(color= Neutral30)

        Column(Modifier.padding(horizontal = 24.dp)
        ) {
            HeaderListItemCountTitleButton(
                title = "Addresses",
                itemCount = address.size,
                textButton = "Go to My Addresses"
            ) { }
            Spacer(Modifier.height(12.dp))
            address.forEach { address ->
                LocationSelectorCard(
                    address = address,
                    onCheckedChange = { onAddressChange(address.id) }
                )
                Spacer(Modifier.height(8.dp))
            }
            Spacer(Modifier.height(24.dp))
            ButtonComponent(
                enabled = true,
                labelResId = R.string.confirm
            ) { onSetLocationClicked() }
        }
    }
}

@Composable
fun CartPromoContent(
    voucher: List<Voucher>,
    onVoucherSelectionChanged: (String) -> Unit,
    onApplyVoucherClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Neutral20)
            .padding(top = 24.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Choose promo",
                style = LocalCustomTypography.current.h3Bold,
                color = Neutral100
            )

            TopBarButton(
                icon = Icons.Default.Clear,
                boxColor = Neutral10, iconColor = Neutral100
            ) { onDismiss() }
        }

        HorizontalDivider(color = Neutral40)

        VerticalTitleItemCountSection(
            itemCount = voucher.size,
            headerText = "Vouchers"
        ) {
            items(items=voucher, key = {it.id}) { item ->
                VoucherSelectorCard (
                    voucher = item,
                    onCheckedChange = { onVoucherSelectionChanged(item.id) }
                )
            }
        }

        Column(Modifier.padding(horizontal = 24.dp)) {
            ButtonComponent(
                enabled = true,
                labelResId = R.string.confirm
            ) { onApplyVoucherClicked() }
        }
    }
}

@Composable
fun CartDoubleCheckContent(
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Neutral20)
            .padding(top = 24.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        DoubleCheckContent()
        HorizontalDivider(color = Neutral40)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(text = "Recheck", textColor = Neutral70,
                onClick = {},
                modifier = Modifier.weight(1f))
            TextButton(text = "Continue Payment", textColor = Orange500,
                onClick = {}, modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun RestaurantCloseModal(
    onDismiss: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Neutral20)
            .padding(top = 12.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StatusContent(
            icon = R.drawable.sorry,
            title = "Sorry, we’re closed.",
            subtitle = "You can check out other restaurant options \nfor an amazing dining experience!"
        )

        Divider24()

        TextButton(text = "I understand", textColor = Orange500, onClick = onDismiss)
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewModalDialog() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CartDoubleCheckContent()
    }
}