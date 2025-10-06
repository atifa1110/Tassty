package com.example.tassty.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.model.Cart
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
    isChecked: Boolean,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(width = 1.dp, if(isChecked) Orange500 else Color.Transparent),
        colors = CardDefaults.cardColors(containerColor = if(isChecked) Orange50 else Neutral20)
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
                modifier = Modifier.height(100.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = cart.name,
                            style = LocalCustomTypography.current.h5Bold,
                            color = Neutral100
                        )
                        NotesText(notes = cart.note ?: "")
                    }
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = {},
                        // Disallow checking if max limit is reached and current option is NOT selected
                        enabled = true,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Orange500,
                            uncheckedColor = Neutral40
                        ),
                        modifier = Modifier.padding(0.dp).size(24.dp)
                    )
                }

                FoodPriceText(
                    price = cart.price.toCleanRupiahFormat(),
                    color = Orange500
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    NotesBoxButton(title = "Notes", onClick = {})
                    QuantityCartContent(
                        itemCount = cart.quantity,
                        enabled = cart.quantity > 1,
                        onIncrement = {},
                        onDecrement = {}
                    )
                }
            }
        }
    }
}

@Composable
fun SelectLocationCard(
){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral20)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ){

            CartIcon(
                icon = R.drawable.location,
                boxColor = Pink200,
                iconColor = Pink500
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
){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral20)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ){

            CartIcon(
                icon = R.drawable.promo,
                boxColor = Orange100,
                iconColor = Orange600
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
fun SummaryPriceText(
    modifier: Modifier = Modifier,
    price: String,
) {
    val parts = price.split(".")
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(style = LocalCustomTypography.current.h6Bold.toSpanStyle().copy(color = Neutral70)) {
                append(parts[0])
            }
            withStyle(style = LocalCustomTypography.current.h8Regular.toSpanStyle().copy(color = Neutral70)) {
                append(".${parts[1]}")
            }
        },
    )
}

@Composable
fun OrderSummaryCard(
    totalPrice: Int = 0,
    deliveryFee: Int = 0,
    totalOrder: Int = 0
) {
    // Composable for a single line item (e.g., Total price)
    @Composable
    fun SummaryLineItem(label: String, amount: String, isTotal: Boolean = false) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = if(isTotal) LocalCustomTypography.current.h5Bold else LocalCustomTypography.current.bodySmallRegular,
                color = if(isTotal) Neutral100 else Neutral70
            )

            if(isTotal) FoodPriceText(price = amount, color = Orange500) else SummaryPriceText(price = amount)
        }
    }

    Card(modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Neutral10),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(width = 1.dp, color = Neutral30)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Total Price Line
            SummaryLineItem(
                label = "Total price",
                amount = totalPrice.toCleanRupiahFormat()
            )

            // Delivery & Order fee Line
            SummaryLineItem(
                label = "Delivery & Order fee",
                amount = deliveryFee.toCleanRupiahFormat()
            )

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
fun PreviewOrderSummaryCard() {
    OrderSummaryCard(
        totalPrice = 20000,
        deliveryFee = 20000,
        totalOrder = 40000
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCartCardComponent() {
    Column (Modifier.fillMaxWidth()){
        CartListCard(
            cart = Cart(
                id = "1",
                name = "Fresh Salad",
                imageUrl = "",
                price = 28000,
                quantity = 4,
                note = "Cheese"
            ),
            isChecked = true
        )
        Spacer(Modifier.height(10.dp))
        SelectLocationCard()
        Spacer(Modifier.height(10.dp))
        SelectPaymentCard()
    }
}