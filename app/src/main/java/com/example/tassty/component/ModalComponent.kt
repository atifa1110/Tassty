package com.example.tassty.component

import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.data.source.remote.network.Resource
import com.example.core.domain.model.MenuStatus
import com.example.core.domain.model.Restaurant
import com.example.core.domain.model.RestaurantStatus
import com.example.core.ui.mapper.FilterCategory
import com.example.core.ui.model.CartItemUiModel
import com.example.core.ui.model.CollectionUiModel
import com.example.core.ui.model.DetailMenuUiModel
import com.example.core.ui.model.DetailRestaurantUiModel
import com.example.core.ui.model.FilterOptionUi
import com.example.core.ui.model.UserAddressUiModel
import com.example.core.ui.model.VoucherUiModel
import com.example.tassty.R
import com.example.tassty.menuDetailItem
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500

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
    resource: Resource<List<CollectionUiModel>>,
    onCollectionSelected: (String, Boolean) -> Unit,
    onSaveCollectionClick:() -> Unit,
    onAddCollectionClick: () -> Unit
){
    val items = resource.data.orEmpty()
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
            itemCount = items.size,
            title = "Collection",
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(Modifier.height(12.dp))

        Column(Modifier.padding(horizontal = 24.dp)) {
            when{
                resource.isLoading ->{
                    LoadingRowState()
                }

                resource.errorMessage!=null || items.isEmpty()-> {
                    ErrorListState("") { }
                }
                else -> {
                    items.forEach { collection ->
                        CollectionCard(
                            collection = collection,
                            onCheckedChange = {onCollectionSelected(collection.id,it)}
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            ButtonComponent(
                modifier = Modifier.fillMaxWidth(),
                enabled = items.isNotEmpty(),
                labelResId = R.string.save,
                onClick = onSaveCollectionClick
            )
        }
    }
}

@Composable
fun CollectionAddContent(
    collectionName: String,
    onValueName: (String) -> Unit,
    onDismissClick:() -> Unit,
    onAddCollection: () -> Unit
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
            CollectionNameEditText(
                value = collectionName,
                onValueChange = onValueName
            )
            Spacer(Modifier.height(24.dp))
            ButtonComponent(
                modifier = Modifier.fillMaxWidth(),
                enabled = collectionName.isNotEmpty(),
                labelResId = R.string.create,
                onClick = onAddCollection
            )
        }
    }
}

@Composable
fun CollectionEditContent(
    collectionName: String,
    onValueName: (String) -> Unit,
    onDismissClick:() -> Unit,
    onUpdateCollection: () -> Unit
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
                text= "Edit collection",
                style = LocalCustomTypography.current.h3Bold,
                color = Neutral100
            )

            TopBarButton(icon = R.drawable.x,
                boxColor = Neutral10, iconColor = Neutral100
            ) { onDismissClick() }
        }

        HorizontalDivider(Modifier.padding(vertical = 32.dp))

        Column(Modifier.padding(horizontal = 24.dp)) {
            Text(
                text= "Collection Name",
                style = LocalCustomTypography.current.h5Bold,
                color = Neutral100
            )
            Spacer(Modifier.height(16.dp))
            CollectionNameEditText(
                value = collectionName,
                onValueChange = onValueName
            )
            Spacer(Modifier.height(24.dp))
            ButtonComponent(
                enabled = collectionName.isNotEmpty(),
                labelResId = R.string.update,
                onClick = onUpdateCollection
            )
        }
    }
}

@Composable
fun CollectionDeleteContent(
    collectionImage: String,
    onDismissClick:() -> Unit,
    onDeleteCollection: () -> Unit
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        .background(Neutral20)
        .padding(top = 24.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        OverlapImage(imageUrl = collectionImage)

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = LocalCustomTypography.current.h2Bold.toSpanStyle().copy(color = Neutral100)) {
                        append("Are you sure want to \ndelete this collection")
                    }
                    withStyle(style = LocalCustomTypography.current.h2Bold.toSpanStyle().copy(color = Pink500)) {
                        append("?")
                    }
                },
                style = LocalCustomTypography.current.h2Bold
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "You can’t undo this action.",
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
                onClick = onDismissClick,
                modifier = Modifier.weight(0.5f)
            )

            TextButton(
                text = "Delete",
                textColor = Neutral70,
                onClick = onDeleteCollection,
                modifier = Modifier.weight(0.5f)
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
    restaurant: DetailRestaurantUiModel?
){
    val operational = restaurant?.operationalDay.orEmpty()
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
                StatusItemImage(
                    imageUrl = restaurant?.imageUrl?:"",
                    status = restaurant?.statusResult?.status?: RestaurantStatus.CLOSED,
                    name = "Restaurant Modal Popup",
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = restaurant?.name?:"Unknown",
                        style = LocalCustomTypography.current.h3Bold,
                        color = Neutral100
                    )
                    Text(
                        text =restaurant?.fullAddress?:"No Address",
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
            operational.forEach { day ->
                RestaurantOperationalCard(day)
            }
        }

    }
}

@Composable
fun SortContent(
    sortList: List<FilterOptionUi>,
    onUpdateDraftFilter: (FilterCategory, String) -> Unit,
    onApplySort: () -> Unit,
    onResetSort: () -> Unit
){
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
            ResetButton(onClick=onResetSort)
        }

        HorizontalDivider(Modifier.padding(vertical = 32.dp))

        RadioFilterTitleSection(
            title = "",
            isTitleShown = false,
            options = sortList,
        ) { key -> onUpdateDraftFilter(FilterCategory.SORT,key) }

        Column (Modifier.padding(horizontal = 24.dp)){
            Spacer(Modifier.height(24.dp))
            ButtonComponent(
                modifier = Modifier.fillMaxWidth(),
                enabled = true,
                labelResId = R.string.apply,
                onClick = onApplySort
            )
        }
    }
}

@Composable
fun FilterContent(
    rupiahPriceRanges: List<FilterOptionUi>,
    restoRatingsOptions: List<FilterOptionUi>,
    modesOptions: List<FilterOptionUi>,
    cuisineOption: List<FilterOptionUi>,
    onUpdateDraftFilter: (FilterCategory, String) -> Unit,
    onApplyFilter:() -> Unit,
    onResetFilter:() -> Unit
){
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Neutral20)
            .padding(top = 24.dp)
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
                ResetButton(onClick = onResetFilter)
            }
        }

        // Divider
        item { HorizontalDivider(Modifier.padding(vertical = 32.dp)) }

        // Price range
        item {
            RadioFilterTitleSection(
                title = "Price range",
                options = rupiahPriceRanges,
            ) { key -> onUpdateDraftFilter(FilterCategory.PRICE,key) }
        }

        item { HorizontalDivider(Modifier.padding(vertical = 32.dp)) }

        // Restaurant Rating
        item {
            ChipFilterSection(
                title = stringResource(R.string.resto_ratings),
                options = restoRatingsOptions,
            ) { key -> onUpdateDraftFilter(FilterCategory.RATING,key) }
        }

        item { HorizontalDivider(Modifier.padding(vertical = 32.dp)) }

        // Modes
        item {
            ChipFilterSection(
                title = "Modes",
                options = modesOptions,
            ) { key ->  onUpdateDraftFilter(FilterCategory.MODE,key)}
        }

        item { HorizontalDivider(Modifier.padding(vertical = 32.dp)) }

        // Cuisines type
        item {
            RadioFilterTitleSection(
                title = "Cuisines type",
                options = cuisineOption,
            ) { key->  onUpdateDraftFilter(FilterCategory.CUISINE,key)}
        }

        item{
            Column (Modifier.padding(horizontal = 24.dp, vertical = 16.dp)){
                ButtonComponent(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true,
                    labelResId = R.string.apply
                ) { onApplyFilter() }
            }
        }
    }
}

@Composable
fun CartRemoveMenuContent(
    cart: CartItemUiModel?=null,
    onRemoveCartItem: (String) -> Unit,
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
                    onClick = {onRemoveCartItem(cart.cartId)},
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
            CartNotesEditText(value = "") { }
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
    resource: Resource<List<UserAddressUiModel>>,
    onAddressChange: (String) -> Unit,
    onSetLocationClicked:() -> Unit,
    onDismiss: () -> Unit
){
    val items = resource.data.orEmpty()
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
                itemCount = items.size,
                textButton = "Go to My Addresses"
            ) { }
            Spacer(Modifier.height(12.dp))
            items.forEach { address ->
                LocationSelectorCard(
                    address = address,
                    onCheckedChange = { onAddressChange(address.id) }
                )
                Spacer(Modifier.height(8.dp))
            }
            Spacer(Modifier.height(24.dp))
            ButtonComponent(
                modifier = Modifier.fillMaxWidth(),
                enabled = true,
                labelResId = R.string.confirm
            ) { onSetLocationClicked() }
        }
    }
}

@Composable
fun CartPromoContent(
    resource: Resource<List<VoucherUiModel>>,
    onVoucherSelectionChanged: (String) -> Unit,
    onApplyVoucherClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    val items = resource.data.orEmpty()
    Log.d("ModalComponent",items.size.toString())
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
            itemCount = items.size,
            headerText = "Vouchers"
        ) {
            items(items = items, key = { it.id }) { item ->
                VoucherSelectorCard(
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
    onRecheck: () -> Unit,
    onContinueToPayment:() -> Unit
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
                onClick = onRecheck,
                modifier = Modifier.weight(1f))
            TextButton(text = "Continue Payment", textColor = Orange500,
                onClick = onContinueToPayment, modifier = Modifier.weight(1f)
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

@Composable
fun MenuAddToCartContent(
    isEditMode: Boolean,
    quantity : Int,
    resource: Resource<DetailMenuUiModel>,
    onIncreaseQuantity:() -> Unit,
    onDecreaseQuantity:() -> Unit,
    onAddToCart:(DetailMenuUiModel)-> Unit
){
    val menu = resource.data?:return
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Neutral20).padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(16.dp)
        ) {
            StatusItemImage(
                imageUrl = menu.imageUrl,
                name = menu.name,
                status = menu.menuStatus,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
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
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    FoodPriceBigText(
                        price = menu.formatPriceDiscount,
                        color = Orange500
                    )

                    if (menu.promo) {
                        FoodPriceLineText(
                            price = menu.formatPrice,
                            color = Neutral60
                        )
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)){
                QuantityTextButton(
                    quantity = quantity,
                    onIncreaseQuantity = onIncreaseQuantity,
                    onDecreaseQuantity = onDecreaseQuantity
                )
                ButtonComponent(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = menu.menuStatus == MenuStatus.AVAILABLE,
                    labelResId = if(isEditMode) R.string.update_cart else R.string.add_to_cart
                ) { onAddToCart(menu) }
            }
        }
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
        MenuAddToCartContent(
            isEditMode = false,
            quantity = 1,
            resource = Resource(menuDetailItem),
            onIncreaseQuantity = {},
            onDecreaseQuantity = {},
            onAddToCart = {}
        )
    }
}