package com.example.tassty.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.core.domain.utils.toCleanRupiahFormat
import com.example.core.ui.model.CartItemUiModel
import com.example.core.ui.model.UserAddressUiModel
import com.example.core.ui.model.VoucherUiModel
import com.example.tassty.R
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange100
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Orange600
import com.example.tassty.ui.theme.Pink200
import com.example.tassty.ui.theme.Pink500

@Composable
fun CartListCard(
    cart: CartItemUiModel,
    onCheckedChange: (String)-> Unit,
    onIncrementQuantity:() -> Unit,
    onDecrementQuantity:() -> Unit,
    onCartNotesClick:()-> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(width = 1.dp, if(cart.isSelected) Orange500 else Color.Transparent),
        colors = CardDefaults.cardColors(containerColor = if(cart.isSelected) LocalCustomColors.current.selectedOrangeBackground else LocalCustomColors.current.cardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CommonImage(
                imageUrl = cart.imageUrl,
                name = "cart item",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = cart.name,
                            style = LocalCustomTypography.current.h5Bold,
                            color = LocalCustomColors.current.headerText
                        )
                        NotesText(notes = cart.formatOptions)
                    }

                    Checkbox(
                        checked = cart.isSelected,
                        onCheckedChange =  { onCheckedChange(cart.cartId) },
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

                Spacer(Modifier.height(8.dp))

                FoodPriceText(
                    price = cart.price.toCleanRupiahFormat(),
                    color = Orange500
                )

                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    EditButton(title = stringResource(R.string.notes), onClick = onCartNotesClick)
                    QuantityCartContent(
                        itemCount = cart.quantity,
                        enabled = cart.quantity >= 1,
                        onIncrement = onIncrementQuantity,
                        onDecrement = onDecrementQuantity
                    )
                }
            }
        }
    }
}


@Composable
fun SelectLocationCard(
    address: UserAddressUiModel?= null,
    onClick: () -> Unit
){
    if(address!= null){
        LocationCard(address)
    }else{
        EmptyLocationCard(onClick = onClick)
    }
}

@Composable
fun EmptyLocationCard(
    onClick:() -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.cardBackground)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ){

            CircleImageIcon(
                boxColor = Pink200,
                contentDescription = "cart icon",
                icon = R.drawable.location,
                iconColor = Pink500,
                iconSize = 16.dp,
                modifier = Modifier.size(32.dp)
            )

            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.select_delivery_location),
                style = LocalCustomTypography.current.h6Bold,
                color = LocalCustomColors.current.headerText
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "choose location",
                tint = LocalCustomColors.current.headerText,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun SelectVoucherCard(
    voucher: VoucherUiModel? = null,
    onClick: () -> Unit
){
    if(voucher!=null){
        VoucherApplyCard(voucher,onClick=onClick)
    }else{
        EmptyVoucherCard(onClick=onClick)
    }
}

@Composable
fun VoucherApplyCard(
    voucher: VoucherUiModel,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.cardBackground)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ){
            CircleImageIcon(
                boxColor = Orange100,
                contentDescription = "cart icon",
                icon = R.drawable.promo,
                iconColor = Orange600,
                iconSize = 16.dp,
                modifier = Modifier.size(32.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = voucher.title,
                    style = LocalCustomTypography.current.h6Bold,
                    color = LocalCustomColors.current.headerText
                )
                Text(
                    text = voucher.description,
                    style = LocalCustomTypography.current.bodySmallMedium,
                    color = LocalCustomColors.current.text
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "choose location",
                tint = LocalCustomColors.current.background,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun EmptyVoucherCard(
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.cardBackground)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ){
            CircleImageIcon(
                boxColor = Orange100,
                contentDescription = "cart icon",
                icon = R.drawable.promo,
                iconColor = Orange600,
                iconSize = 16.dp,
                modifier = Modifier.size(32.dp)
            )

            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.apply_promo_before_order),
                style = LocalCustomTypography.current.h6Bold,
                color = LocalCustomColors.current.headerText
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "choose location",
                tint = LocalCustomColors.current.headerText,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun SummaryLineItem(
    label: String,
    amount: String,
    modifier: Modifier = Modifier,
    isTotal: Boolean = false,
    isDiscount: Boolean = false,
    discountLabel: String? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                style = if (isTotal) LocalCustomTypography.current.h5Bold else LocalCustomTypography.current.bodySmallRegular,
                color = if (isTotal) LocalCustomColors.current.headerText else LocalCustomColors.current.text
            )

            if (isDiscount && discountLabel != null) {
                Spacer(Modifier.width(4.dp))
                VoucherDiscount(discountLabel)
            }
        }

        if (isTotal) {
            FoodPriceText(price = amount, color = Orange500)
        } else {
            SummaryPriceText(isDiscount = isDiscount, price = amount)
        }
    }
}

@Composable
fun OrderSummaryCard(
    isPercentageDiscount: Boolean,
    totalPrice: Int = 0,
    deliveryFee: Int = 0,
    voucherDiscount: Int = 0,
    voucherDiscountPercent: Int? = null,
    totalOrder: Int = 0
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.background),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(width = 1.dp, color = LocalCustomColors.current.border)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SummaryLineItem(
                label = stringResource(R.string.total_price),
                amount = totalPrice.toCleanRupiahFormat()
            )

            SummaryLineItem(
                label = stringResource(R.string.delivery_order_fee),
                amount = deliveryFee.toCleanRupiahFormat()
            )

            if (voucherDiscount != 0) {
                val discountLabel = if (isPercentageDiscount && voucherDiscountPercent != null) {
                    "${voucherDiscountPercent}% Off"
                } else {
                    null
                }

                SummaryLineItem(
                    label = stringResource(R.string.discounts),
                    amount = voucherDiscount.toCleanRupiahFormat(),
                    isDiscount = true,
                    discountLabel = discountLabel
                )
            }

            DashedDivider(color = Color(0xFFDEDEDE))

            SummaryLineItem(
                label = stringResource(R.string.total_order),
                amount = totalOrder.toCleanRupiahFormat(),
                isTotal = true
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewCartCardComponent() {
//    Column (Modifier.fillMaxWidth()){
//        CartListCard(
//            cart = cartUiModel.menus[0],
//            onCheckedChange = {},
//            onIncrementQuantity = {},
//            onDecrementQuantity = {},
//            onCartNotesClick = {}
//        )
//        Spacer(Modifier.height(10.dp))
//        SelectLocationCard(onClick = {})
//        Spacer(Modifier.height(10.dp))
//        SelectVoucherCard(onClick = {})
//    }
//}