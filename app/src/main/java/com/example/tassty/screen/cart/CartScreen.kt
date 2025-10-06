package com.example.tassty.screen.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.CartTopAppBar
import com.example.tassty.component.Divider32
import com.example.tassty.component.FoodRatingAndCityRow
import com.example.tassty.component.HeaderText
import com.example.tassty.component.OrderSummaryCard
import com.example.tassty.component.SelectLocationCard
import com.example.tassty.component.SelectPaymentCard
import com.example.tassty.component.TextButton
import com.example.tassty.component.cartVerticalListBlock
import com.example.tassty.model.Cart
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100

@Composable
fun CartScreen() {
    //dummy data
    val cartsDummy = remember {
        mutableStateListOf(
            Cart(id = "1", name = "Fresh Salad", price = 28000, quantity = 1,
                imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/ad2ab90b-ecd0-46f7-b172-48be7b70f922_Combo-Asik-Berdua.jpg?auto=format"
            ),
            Cart(id = "2", name= "Ramen Tomyum",price = 12000 ,quantity = 1,
                imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/ad2ab90b-ecd0-46f7-b172-48be7b70f922_Combo-Asik-Berdua.jpg?auto=format"),
        )
    }

    Scaffold(
        containerColor = Neutral10,
        topBar = {
            CartTopAppBar()
        },
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth().padding(24.dp)){
                ButtonComponent(
                    enabled = true,
                    labelResId = R.string.continue_payment,
                ) { }
            }
        }
    ) { padding ->
        LazyColumn(modifier =
            Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Neutral10)
                .padding(vertical = 24.dp)
        ){
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text(
                        text = "Indah Cafe",
                        style = LocalCustomTypography.current.h2Bold,
                        color = Neutral100
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FoodRatingAndCityRow(
                            city = "Praya",
                            rating = 4.4,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(text = "Add more+", onClick = {})
                    }
                }
                Divider32()
            }

            cartVerticalListBlock(
                cart = cartsDummy,
                headerText = "Menus",
                onRemoveCartItem = { cart ->
                    cartsDummy.remove(cart)
                },
                onRevealChange = { index, isRevealed ->
                    cartsDummy[index] = cartsDummy[index].copy(isSwipeActionVisible = isRevealed)
                }
            )

            item {
                Divider32()
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    HeaderText(
                        text = "Delivery location",
                        textColor = Neutral100,
                        textButton = "Change location",
                        onButtonClick = {}
                    )
                    Spacer(Modifier.height(12.dp))
                    SelectLocationCard()
                }
            }

            item {
                Divider32()
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text(
                        text = "Payment detail",
                        color = Neutral100,
                        style = LocalCustomTypography.current.h5Bold
                    )
                    Spacer(Modifier.height(12.dp))
                    SelectPaymentCard()
                }
            }

            item{
                Divider32()
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    OrderSummaryCard(
                        totalPrice = 20000,
                        deliveryFee = 20000,
                        totalOrder = 40000
                    )
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCartScreen() {
    CartScreen()
}