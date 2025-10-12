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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.carts
import com.example.tassty.model.Cart
import com.example.tassty.model.UserAddress
import com.example.tassty.model.Voucher
import com.example.tassty.toCleanRupiahFormat
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange100
import com.example.tassty.ui.theme.Orange50
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Orange600
import com.example.tassty.ui.theme.Pink200
import com.example.tassty.ui.theme.Pink500

@Composable
fun CartListCard(
    cart: Cart,
    onCheckedChange: (Cart)-> Unit,
    onIncrementQuantity:() -> Unit,
    onDecrementQuantity:() -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(width = 1.dp, if(cart.isChecked) Orange500 else Color.Transparent),
        colors = CardDefaults.cardColors(containerColor = if(cart.isChecked) Orange50 else Neutral20)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CommonImage(
                imageUrl = cart.imageUrl,
                name = "cart item",
                modifier = Modifier.size(100.dp).clip(CircleShape)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = cart.name,
                            style = LocalCustomTypography.current.h5Bold,
                            color = Neutral100
                        )
                        NotesText(notes = cart.note?.joinToString("\n")?:"No Notes")
                    }

                    Checkbox(
                        checked = cart.isChecked,
                        onCheckedChange =  { onCheckedChange(cart) },
                        enabled = true,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Orange500,
                            uncheckedColor = Neutral40
                        ),
                        modifier = Modifier.padding(0.dp).size(24.dp)
                    )
                }

                Spacer(Modifier.height(10.dp))

                FoodPriceText(
                    price = cart.price.toCleanRupiahFormat(),
                    color = Orange500
                )

                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    NotesBoxButton(title = "Notes", onClick = {})
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
    address: UserAddress?= null,
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
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral20)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp),
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
                text = "Select delivery location",
                style = LocalCustomTypography.current.h6Bold,
                color = Neutral100
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "choose location",
                tint = Neutral100,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun SelectPaymentCard(
    voucher: Voucher? = null,
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
    voucher: Voucher,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral20)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp),
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
                    color = Neutral100
                )
                Text(
                    text = voucher.description,
                    style = LocalCustomTypography.current.bodySmallMedium,
                    color = Neutral70
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "choose location",
                tint = Neutral100,
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
        modifier = Modifier.fillMaxWidth().clickable(onClick=onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral20)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp),
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
                text = "Apply promo before order",
                style = LocalCustomTypography.current.h6Bold,
                color = Neutral100
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "choose location",
                tint = Neutral100,
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
                color = if (isTotal) Neutral100 else Neutral70
            )

            // Show if discount type and label is not null
            if (isDiscount && discountLabel != null) {
                Spacer(Modifier.width(4.dp))
                VoucherDiscount(discountLabel)
            }
        }

        // isTotal is available than color and style change
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
        colors = CardDefaults.cardColors(containerColor = Neutral10),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(width = 1.dp, color = Neutral30)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Total Price Line (Subtotal Item)
            SummaryLineItem(
                label = "Total price",
                amount = totalPrice.toCleanRupiahFormat()
            )

            // 2. Delivery & Order fee Line
            SummaryLineItem(
                label = "Delivery & Order fee",
                amount = deliveryFee.toCleanRupiahFormat()
            )

            // Discounts Line (Conditional)
            if (voucherDiscount != 0) {
                // Show label only if voucher have percentage type
                val discountLabel = if (isPercentageDiscount && voucherDiscountPercent != null) {
                    "${voucherDiscountPercent}% Off"
                } else {
                    null
                }

                SummaryLineItem(
                    label = "Discounts",
                    amount = voucherDiscount.toCleanRupiahFormat(),
                    isDiscount = true,
                    discountLabel = discountLabel
                )
            }

            DashedDivider(color = Color(0xFFDEDEDE))

            // Total Order Line (Bold and Orange)
            SummaryLineItem(
                label = "Total Order",
                amount = totalOrder.toCleanRupiahFormat(),
                isTotal = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCartCardComponent() {
    Column (Modifier.fillMaxWidth()){
        CartListCard(
            cart = carts[0],
            onCheckedChange = {},
            onIncrementQuantity = {},
            onDecrementQuantity = {}
        )
        Spacer(Modifier.height(10.dp))
        SelectLocationCard(onClick = {})
        Spacer(Modifier.height(10.dp))
        SelectPaymentCard(onClick = {})
    }
}