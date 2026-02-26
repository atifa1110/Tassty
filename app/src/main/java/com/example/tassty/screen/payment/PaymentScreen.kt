package com.example.tassty.screen.payment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.component.AddTopAppBar
import com.example.tassty.component.DebitPaymentCard
import com.example.tassty.component.Divider32
import com.example.tassty.component.EWalletSmallPaymentCard
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100

@Composable
fun PaymentScreen(
    onNavigateToAddCard: ()-> Unit
) {
    PaymentContent(
        onAddClick = onNavigateToAddCard
    )
}

@Composable
fun PaymentContent(
    onAddClick:()-> Unit
) {
    Scaffold(
        containerColor = Neutral10,
        topBar = {
            AddTopAppBar (
                onAddClick = onAddClick,
                onBackClick = {}
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            item {
                Spacer(Modifier.height(24.dp))
                Text(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    text = "My payment methods.",
                    style = LocalCustomTypography.current.h2Bold,
                    color = Neutral100
                )
                Divider32()
            }

            item { 
                Text(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    text = "Card",
                    style = LocalCustomTypography.current.h5Bold,
                    color = Neutral100
                )
                Spacer(Modifier.height(12.dp))
            }
            items(2){
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                ) {
                    DebitPaymentCard()
                    Spacer(Modifier.height(16.dp))
                }
            }

            item {
                Spacer(Modifier.height(12.dp))
                Text(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    text = "Virtual Account",
                    style = LocalCustomTypography.current.h5Bold,
                    color = Neutral100
                )
                Spacer(Modifier.height(12.dp))
            }

            items(2){
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                ) {
                    EWalletSmallPaymentCard()
                    Spacer(Modifier.height(16.dp))
                }
            }

            item {
                Spacer(Modifier.height(12.dp))
                Text(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    text = "E-Wallet",
                    style = LocalCustomTypography.current.h5Bold,
                    color = Neutral100
                )
                Spacer(Modifier.height(12.dp))
            }

            items(2){
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                ) {
                    EWalletSmallPaymentCard()
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PaymentPreview(
) {
    PaymentContent(
        onAddClick = {}
    )
}