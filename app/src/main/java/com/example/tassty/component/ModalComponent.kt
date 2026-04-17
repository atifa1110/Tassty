package com.example.tassty.component

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.data.source.remote.network.Resource
import com.example.core.domain.model.MenuStatus
import com.example.core.ui.mapper.FilterCategory
import com.example.core.ui.model.CartItemUiModel
import com.example.core.ui.model.ChatUiModel
import com.example.core.ui.model.CollectionUiModel
import com.example.core.ui.model.DetailMenuUiModel
import com.example.core.ui.model.DetailRestaurantUiModel
import com.example.core.ui.model.FilterOptionUi
import com.example.core.ui.model.MessageUiModel
import com.example.core.ui.model.UserAddressUiModel
import com.example.core.ui.model.VoucherUiModel
import com.example.tassty.R
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.TasstyTheme
import kotlinx.collections.immutable.ImmutableList
import org.threeten.bp.LocalDate

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
    ) { if (it) 0.7f else 0f }

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
                .background(LocalCustomColors.current.modalBackground.copy(alpha = scrimAlpha))
                .clickable(
                    enabled = true,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    if (dismissOnClickOutside) {
                        onDismiss()
                    }
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
                        .background(LocalCustomColors.current.cardBackground)
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
                            .background(LocalCustomColors.current.text)
                    )
                }
            }
        }
    }
}

@Composable
fun CollectionContent(
    items: ImmutableList<CollectionUiModel>?,
    onCollectionSelected: (String, Boolean) -> Unit,
    onSaveCollectionClick:() -> Unit,
    onAddCollectionClick: () -> Unit
){
    if (items == null) {
        return
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(
            RoundedCornerShape(
                topStart = 24.dp, topEnd = 24.dp
            )
        )
        .background(LocalCustomColors.current.cardBackground)
        .padding(top = 24.dp, bottom = 24.dp)
    ) {
        HeaderModalTitleSubtitle(
            modifier = Modifier,
            title = R.string.save_to_collection,
            subtitle = R.string.save_your_favorite
        ){
            TopBarButton(
                icon = Icons.Default.Add,
                boxColor = Orange500,
                iconColor = Neutral10,
                onClick = onAddCollectionClick
            )
        }
        Divider32()
        LazyColumn(
            modifier = Modifier
                .weight(1f, fill = false)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                HeaderListItemCountTitle(
                    itemCount = items.size,
                    title = "Collection",
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            if(items.isEmpty()){
                item {
                    EmptyCollectionSmallContent()
                }
            } else {
                items(
                    items = items,
                    key = { it.id }
                ) { collection ->
                    CollectionCard(
                        collection = collection,
                        onCheckedChange = { onCollectionSelected(collection.id, it) }
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        ButtonComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            enabled = items.isNotEmpty(),
            labelResId = R.string.save,
            onClick = onSaveCollectionClick
        )
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
        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        .background(LocalCustomColors.current.cardBackground)
        .padding(top = 24.dp, bottom = 24.dp).imePadding()
    ) {
        HeaderModalTitle(
            title = R.string.create_collection
        ) {
            TopBarButton(
                icon = R.drawable.arrow_left,
                boxColor = LocalCustomColors.current.background,
                iconColor = LocalCustomColors.current.iconFocused,
                onClick = onDismissClick
            )
        }

        Divider32()
        Column(Modifier.padding(horizontal = 24.dp)) {
            TextSection(
                label = stringResource(R.string.collection_name),
                placeholder = stringResource(R.string.enter_collection_name),
                leadingIcon = R.drawable.collection,
                text = collectionName,
                onTextChanged = onValueName
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
        .background(LocalCustomColors.current.cardBackground)
        .padding(top = 24.dp, bottom = 24.dp).imePadding()
    ) {
        HeaderModalTitle(
            title = R.string.edit_collection
        ) {
            TopBarButton(
                icon = R.drawable.x,
                boxColor = LocalCustomColors.current.background,
                iconColor = LocalCustomColors.current.iconFocused,
                onClick = onDismissClick
            )
        }

        Divider32()

        Column(Modifier.padding(horizontal = 24.dp)) {
            TextSection(
                label = stringResource(R.string.collection_name),
                placeholder = stringResource(R.string.enter_collection_name),
                leadingIcon = R.drawable.collection,
                text = collectionName,
                onTextChanged = onValueName
            )
            Spacer(Modifier.height(24.dp))
            ButtonComponent(
                modifier = Modifier.fillMaxWidth(),
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
        .background(LocalCustomColors.current.cardBackground)
        .padding(top = 24.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OverlapImage(imageUrl = collectionImage)

        StatusTwoColorModalContent(
            title = stringResource(R.string.sure_want_delete_this_collection)
        )

        Divider32()

        BottomModalButton(
            noText = stringResource(R.string.cancel),
            yesText = stringResource(R.string.delete),
            onNoClick = onDismissClick,
            onYesClick = onDeleteCollection
        )
    }
}


@Composable
fun CollectionSaveContent(
    title: String,
    onCheckCollection: () -> Unit,
    onConfirmClick:() -> Unit,
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        .background(LocalCustomColors.current.cardBackground)
        .padding(top = 24.dp, bottom = 46.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SuccessIcon()
        Spacer(Modifier.height(24.dp))
        StatusTwoColorModalContent(
            title = title,
            highlight = stringResource(R.string.exclamation),
            highlightColor = Orange500,
            subtitle = stringResource(R.string.menu_collection_updated)
        )
        Divider32()
        BottomModalButton(
            noText = stringResource(R.string.check_collection),
            noTextColor = Orange500,
            yesText = stringResource(R.string.confirm),
            yesTextColor = Orange500,
            onNoClick = onCheckCollection,
            onYesClick = onConfirmClick
        )
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
            .background(LocalCustomColors.current.cardBackground)
            .padding(top = 24.dp, bottom = 46.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        imageContent()
        Spacer(Modifier.height(24.dp))
        StatusModalContent(title = title,subtitle = subtitle)
        Divider32()
        TextButton(text = buttonTitle, textColor = Orange500, onClick = onClick)
    }
}

@Composable
fun DetailScheduleContent(
    restaurant: DetailRestaurantUiModel?
) {
    if (restaurant == null) return

    val operational = restaurant.operationalDay
    val status = restaurant.statusResult.status
    val colors = LocalCustomColors.current
    val typography = LocalCustomTypography.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(colors.cardBackground)
            .padding(vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatusItemImage(
                imageUrl = restaurant.imageUrl,
                status = status,
                name = "Restaurant Modal Popup",
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = restaurant.name,
                    style = typography.h3Bold,
                    color = colors.headerText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = restaurant.fullAddress,
                    style = typography.bodySmallMedium,
                    color = colors.text,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            TopBarButton(
                icon = R.drawable.phone,
                boxColor = Orange500,
                iconColor = Neutral10,
                onClick = {}
            )
        }

        Divider32()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.operational_hour),
                style = typography.h5Bold,
                color = colors.headerText
            )

            operational.forEach { day ->
                RestaurantOperationalCard(day)
            }
        }
    }
}

@Composable
fun SortContent(
    sortList: List<FilterOptionUi<FilterCategory>>,
    onUpdateDraftFilter: (FilterCategory, String) -> Unit,
    onApplySort: () -> Unit,
    onResetSort: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(LocalCustomColors.current.cardBackground)
            .padding(vertical = 24.dp)
    ) {
        HeaderModalTitle(
            title = R.string.sort_restos_by
        ) {
            ResetButton(onClick=onResetSort)
        }
        Divider32()
        RadioFilterTitleSection(
            isTitleShown = false,
            options = sortList,
            onOptionSelected = { key -> onUpdateDraftFilter(FilterCategory.SORT,key) }
        )
        ButtonComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 24.dp,
                    start = 24.dp, end = 24.dp
                ),
            enabled = true,
            labelResId = R.string.apply,
            onClick = onApplySort
        )
    }
}

@Composable
fun FilterContent(
    rupiahPriceRanges: List<FilterOptionUi<FilterCategory>>,
    restoRatingsOptions: List<FilterOptionUi<FilterCategory>>,
    modesOptions: List<FilterOptionUi<FilterCategory>>,
    cuisineOption: List<FilterOptionUi<FilterCategory>>,
    onUpdateDraftFilter: (FilterCategory, String) -> Unit,
    onApplyFilter:() -> Unit,
    onResetFilter:() -> Unit
){
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(LocalCustomColors.current.cardBackground)
            .padding(vertical = 24.dp)
    ) {
        item(key = "filter_header") {
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
            Divider32()
        }

        item(key= "price_filter"){
            RadioFilterTitleSection(
                title = stringResource(R.string.price_range),
                options = rupiahPriceRanges,
            ) { key -> onUpdateDraftFilter(FilterCategory.PRICE,key) }
            Divider32()
        }

        item(key= "rating_filter") {
            ChipFilterSection(
                title = stringResource(R.string.resto_ratings),
                options = restoRatingsOptions,
            ) { key -> onUpdateDraftFilter(FilterCategory.RATING,key) }
            Divider32()
        }

        item(key= "mode_filter") {
            ChipFilterSection(
                title = stringResource(R.string.modes),
                options = modesOptions,
            ) { key ->  onUpdateDraftFilter(FilterCategory.MODE,key)}
            Divider32()
        }

        item(key= "cuisine_filter") {
            RadioFilterTitleSection(
                title = stringResource(R.string.cuisines_type),
                options = cuisineOption,
            ) { key->  onUpdateDraftFilter(FilterCategory.CUISINE,key)}
            Spacer(Modifier.height(24.dp))
        }

        item (key= "button_content"){
            ButtonComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                enabled = true,
                labelResId = R.string.apply,
                onClick = onApplyFilter
            )
        }
    }
}

@Composable
fun CartRemoveMenuContent(
    cart: CartItemUiModel?,
    onRemoveCartItem: (String) -> Unit,
    onDismiss: () -> Unit
){
    if(cart!= null){
        Column(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(LocalCustomColors.current.cardBackground)
            .padding(top = 24.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OverlapImage(imageUrl = cart.imageUrl)
            Spacer(Modifier.height(24.dp))
            StatusTwoColorModalContent(
                title = stringResource(R.string.remove_this_menu),
                subtitle = stringResource(R.string.add_menu_back_from_details)
            )
            Divider32()
            BottomModalButton(
                noText = stringResource(R.string.cancel),
                yesText = stringResource(R.string.remove),
                onNoClick = onDismiss,
                onYesClick = { onRemoveCartItem(cart.cartId) }
            )
        }
    }
}

@Composable
fun CartEditContent(
    cartId: String,
    text: String,
    onTextChange:(String)-> Unit,
    onUpdateCart: (String, String) -> Unit,
    onDismiss: () -> Unit
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        .background(LocalCustomColors.current.cardBackground)
        .padding(top = 24.dp, bottom = 32.dp).imePadding()
    ) {
        HeaderModalTitle(
            title = R.string.edit_notes
        ) {
            TopBarButton(
                icon = Icons.Default.Clear,
                boxColor = LocalCustomColors.current.background,
                iconColor = LocalCustomColors.current.iconFocused,
                onClick = onDismiss
            )
        }

        Divider32()

        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
        ){
            TextSection(
                text = text,
                singleLine = false,
                onTextChanged = onTextChange,
                label = stringResource(R.string.notes),
                placeholder = stringResource(R.string.write_your_notes_here),
                leadingIcon = R.drawable.note,
            )
            Spacer(Modifier.height(24.dp))
            ButtonComponent(
                modifier = Modifier.fillMaxWidth(),
                enabled = true,
                labelResId = R.string.update,
                onClick = {
                    onUpdateCart(cartId, text)
                }
            )
        }
    }
}

@Composable
fun CartDeliveryLocationContent(
    selectedId: String?,
    resource: Resource<ImmutableList<UserAddressUiModel>>,
    onAddressChange: (String) -> Unit,
    onSetLocationClicked:() -> Unit,
    onDismiss: () -> Unit,
    onNavigateToAddress:() -> Unit
){
    val items = resource.data.orEmpty()

    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        .background(LocalCustomColors.current.cardBackground)
        .padding(top = 24.dp, bottom = 32.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(R.string.choose_delivery_location),
                    style = LocalCustomTypography.current.h3Bold,
                    color = LocalCustomColors.current.headerText
                )
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.you_can_add_a_new_delivery_address_on_the))

                        withStyle(
                            style = SpanStyle(
                                color = LocalCustomColors.current.headerText
                            )
                        ) {
                            append("${stringResource(R.string.my_addresses)} ")
                        }

                        append(stringResource(R.string.page))
                    },
                    style = LocalCustomTypography.current.bodySmallMedium,
                    color = LocalCustomColors.current.text
                )
            }

            TopBarButton(
                icon = Icons.Default.Clear,
                boxColor = LocalCustomColors.current.background,
                iconColor = LocalCustomColors.current.iconFocused,
                onClick = onDismiss
            )
        }

        Divider32()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .padding(horizontal = 24.dp)
        ) {
            item {
                HeaderListItemCountTitleButton(
                    title = stringResource(R.string.addresses),
                    itemCount = items.size,
                    textButton = stringResource(R.string.go_to_my_addresses),
                    onClick = onNavigateToAddress
                ) 
                Spacer(Modifier.height(12.dp))
            }

            if (items.isEmpty()) {
                item {
                    Spacer(Modifier.height(12.dp))
                    EmptyAddressContent()
                }
            } else {
                items(items, key = { it.id }) { address ->
                    LocationSelectorCard(
                        address = address,
                        onCheckedChange = { onAddressChange(address.id) },
                        isSelected = address.id == selectedId
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        ButtonComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            enabled = items.isNotEmpty(),
            labelResId = R.string.confirm,
            onClick = onSetLocationClicked
        )
    }
}

@Composable
fun CartPromoContent(
    selectedId: String?,
    resource: Resource<ImmutableList<VoucherUiModel>>,
    onVoucherSelectionChanged: (String) -> Unit,
    onApplyVoucherClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    val items = resource.data.orEmpty()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(LocalCustomColors.current.cardBackground)
            .padding(top = 24.dp)
    ) {
        HeaderModalTitle(
            title = R.string.choose_promo
        ) {
            TopBarButton(
                icon = Icons.Default.Clear,
                boxColor = LocalCustomColors.current.background,
                iconColor = LocalCustomColors.current.iconFocused,
                onClick = onDismiss
            )
        }

        Divider32()

        Box(
            modifier = Modifier.weight(1f, fill = false)
        ) {
            VerticalTitleItemCountSection(
                itemCount = items.size,
                headerText = stringResource(R.string.vouchers)
            ) {
                if (items.isEmpty()) {
                    item(key = "empty_content") {
                        EmptyVoucherContent()
                    }
                } else {
                    items(items = items, key = { it.id }) { item ->
                        VoucherSelectorCard(
                            voucher = item,
                            onCheckedChange = { onVoucherSelectionChanged(item.id) },
                            isSelected = item.id == selectedId
                        )
                    }
                }
            }
        }

        ButtonComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            enabled = items.isNotEmpty(),
            labelResId = R.string.confirm,
            onClick = onApplyVoucherClicked
        )
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
            .background(LocalCustomColors.current.cardBackground)
            .padding(top = 24.dp, bottom = 32.dp)
    ) {
        StatusContent(
            title = stringResource(R.string.please_double_check_your_order),
            subtitle = stringResource(R.string.we_cant_cancel_your_order),
            icon = R.drawable.empty_search
        )
        Divider32()
        BottomModalButton(
            noText = stringResource(R.string.recheck),
            onNoClick = onRecheck,
            noTextColor = Neutral70,
            yesText = stringResource(R.string.continue_payment),
            yesTextColor = Orange500,
            onYesClick = onContinueToPayment
        )
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
            .background(LocalCustomColors.current.cardBackground)
            .padding(top = 12.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StatusContent(
            icon = R.drawable.sorry,
            title = stringResource(R.string.sorry_we_re_closed),
            subtitle = stringResource(R.string.you_can_check_out_other_restaurant)
        )
        Divider32()
        TextButton(
            text = stringResource(R.string.i_understand),
            textColor = Orange500,
            onClick = onDismiss
        )
    }
}

@Composable
fun DatePickerContent(
    currentMonth: LocalDate,
    startDate : LocalDate?,
    endDate: LocalDate?,
    onMonthChange: (LocalDate) -> Unit,
    onDateClick: (LocalDate) -> Unit,
    onSelectClick: () -> Unit,
    onCancelClick: () -> Unit
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        .background(LocalCustomColors.current.background)
        .padding(top = 24.dp)
    ) {
        HeaderModalTitle(
            title = R.string.select_date
        ) {
            TopBarButton(
                icon = Icons.Default.Clear,
                boxColor = LocalCustomColors.current.cardBackground,
                iconColor = LocalCustomColors.current.iconFocused,
                onClick = onCancelClick
            )
        }
        
        Divider32()

        DatePicker(
            currentMonth = currentMonth,
            startDate = startDate,
            endDate = endDate,
            onMonthChange = onMonthChange,
            onDateClick = onDateClick
        )

        ButtonComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            enabled = true,
            labelResId = R.string.select,
            onClick = onSelectClick
        )
    }
}

@Composable
fun ChatRemoveContent(
    chat: ChatUiModel?,
    onRemoveChat: () -> Unit,
    onDismiss: () -> Unit
) {
    if (chat != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(LocalCustomColors.current.cardBackground)
                .padding(top = 24.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OverlapImage(imageUrl = chat.profileImage)
            Spacer(Modifier.height(24.dp))
            StatusTwoColorModalContent(
                title = stringResource(R.string.sure_want_to_delete_message)
            )
            Divider32()
            BottomModalButton(
                noText = stringResource(R.string.cancel),
                onNoClick = onDismiss,
                yesText = stringResource(R.string.delete),
                onYesClick = onRemoveChat
            )
        }
    }
}

@Composable
fun LogoutContent(
    onDismissClick:() -> Unit,
    onLogout: () -> Unit
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        .background(LocalCustomColors.current.cardBackground)
        .padding(top = 24.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DangerIcon()
        Spacer(Modifier.height(24.dp))
        StatusTwoColorModalContent(
            title = stringResource(R.string.sure_want_to_logout)
        )
        Divider32(color = Neutral40)
        BottomModalButton(
            noText = stringResource(R.string.cancel),
            onNoClick = onDismissClick,
            yesText = stringResource(R.string.logout),
            onYesClick = onLogout
        )
    }
}

@Composable
fun MessageHeader(
    senderName: String,
    senderImage: String,
    time: String,
    status: String,
    onClick: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MessageProfile(
            imageUrl = senderImage,
            name = senderName
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)) {

            Text(
                text = senderName,
                style = LocalCustomTypography.current.bodySmallBold,
                color = LocalCustomColors.current.headerText
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = time,
                    style = LocalCustomTypography.current.bodyXtraSmallMedium,
                    color = LocalCustomColors.current.text
                )

                if(status == "Seen") {
                    Text(
                        text = " • ",
                        style = LocalCustomTypography.current.bodyXtraSmallRegular,
                        color = LocalCustomColors.current.text
                    )

                    Text(
                        text = status,
                        style = LocalCustomTypography.current.bodyXtraSmallSemiBold,
                        color = Orange500
                    )
                }
            }
        }

        TopBarButton(
            icon = Icons.Default.Download,
            boxColor = LocalCustomColors.current.background,
            iconColor = LocalCustomColors.current.iconFocused,
            onClick = onClick
        )
    }
}

@Composable
fun MessageImageContent(
    message: MessageUiModel?,
    status: String
){
    if(message!=null){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(LocalCustomColors.current.cardBackground)
                .padding(top = 24.dp, bottom = 32.dp, start = 24.dp, end = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            MessageHeader(
                senderName = message.senderName,
                senderImage = message.senderImage,
                time = message.time,
                status = status,
                onClick = {}
            )

            CommonImage(
                imageUrl = message.imageAttachment,
                name = "attachment",
                modifier = Modifier
                    .height(400.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
        }
    }
}

@Composable
fun DeleteNotificationContent(
    onDismissClick:() -> Unit,
    onDelete: () -> Unit
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        .background(LocalCustomColors.current.cardBackground)
        .padding(top = 24.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DangerIcon()
        Spacer(Modifier.height(24.dp))
        StatusTwoColorModalContent(
            title = stringResource(R.string.delete_all_your_notifications),
        )
        Divider32()
        BottomModalButton(
            noText = stringResource(R.string.cancel),
            onNoClick = onDismissClick,
            yesText = stringResource(R.string.delete),
            onYesClick = onDelete
        )
    }
}

@Composable
fun CompletedContent(
    time: Int,
    onDismissClick:() -> Unit,
    onRatingDriver: () -> Unit
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        .background(LocalCustomColors.current.cardBackground)
        .padding(top = 24.dp, bottom = 32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.eating),
                contentDescription = "",
                modifier = Modifier.size(250.dp,200.dp)
            )
            Spacer(Modifier.height(24.dp))

            CustomTwoColorText(
                fullText = stringResource(R.string.your_order_has_arrived),
                textColor = LocalCustomColors.current.headerText,
                highlightText = stringResource(R.string.exclamation),
                normalStyle = LocalCustomTypography.current.h2Bold,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.right_on_time_we_delivered_your_order_in))
                    withStyle(LocalCustomTypography.current.bodyMediumBold.toSpanStyle()
                        .copy(
                            color = Orange500
                        )
                    ) {
                        append(stringResource(R.string.min, time))
                    }
                },
                style = LocalCustomTypography.current.bodyMediumRegular,
                color = LocalCustomColors.current.text,
                textAlign = TextAlign.Center
            )
        }
        Divider32()
        BottomModalButton(
            noText = stringResource(R.string.exit),
            noTextColor = Neutral70,
            onNoClick = onDismissClick,
            yesText = stringResource(R.string.rating_driver),
            yesTextColor = Orange500,
            onYesClick = onRatingDriver
        )
    }
}

@Composable
fun DeleteAllCartContent(
    onDismissClick:() -> Unit,
    onDelete: () -> Unit
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        .background(LocalCustomColors.current.cardBackground)
        .padding(top = 24.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DangerIcon()
        Spacer(Modifier.height(24.dp))
        StatusTwoColorModalContent(
            title = stringResource(R.string.sure_want_to_delete_all_carts),
        )
        Divider32()
        BottomModalButton(
            noText = stringResource(R.string.cancel),
            onNoClick = onDismissClick,
            yesText = stringResource(R.string.delete),
            onYesClick = onDelete
        )
    }
}

//@Preview(showBackground = true)
@Composable
fun ModalPreview() {
    TasstyTheme{
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {

        }
    }
}