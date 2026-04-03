package com.example.tassty.screen.terms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.component.BackTopAppBar
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.Divider32
import com.example.tassty.component.HeaderListBlackTitle
import com.example.tassty.component.HeaderTitleScreen
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.TasstyTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


@Composable
fun TermsScreen(
    onNavigateBack:() -> Unit
) {
    TermsContent (onNavigateBack = onNavigateBack)
}

@Composable
fun TermsContent(
    onNavigateBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = LocalCustomColors.current.background,
        topBar = {
            BackTopAppBar(onBackClick = onNavigateBack)
        },
        bottomBar = {
            Column(Modifier.background(LocalCustomColors.current.modalBackgroundFrame)) {
                ButtonComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    labelResId = R.string.accept,
                    onClick = {}
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 24.dp)
        ) {
            Spacer(Modifier.height(24.dp))
            HeaderTitleScreen(
                modifier = Modifier.padding(horizontal = 24.dp),
                title = "Terms of Service,\nLicense & Warranty\nAgreement."
            )

            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                Text(
                    text = "Last update : ",
                    style = LocalCustomTypography.current.bodyMediumRegular,
                    color = LocalCustomColors.current.text
                )
                Text(
                    text = "15 Mar 2026",
                    style = LocalCustomTypography.current.bodyMediumBold,
                    color = Orange500
                )
            }

            Divider32()

            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                TermsText(
                    title = "1. Your Agreement",
                    text = "By accessing or using the Tassty! app, you agree to be bound by these terms. We provide a platform that connects you with local restaurants and delivery partners. You are responsible for maintaining the confidentiality of your account and for all activities that occur under your password."
                )

                TermsText(
                    title = "2. Privacy & Data",
                    text = "Your privacy is our priority. Tassty! collects location data to provide accurate delivery tracking and personalized restaurant recommendations. We use secure encryption to protect your payment information and will never share your personal data with third parties without your explicit consent, except as required to fulfill your orders."
                )

                TermsText(
                    title = "3. Orders & Payments",
                    text = "All orders placed through the app are subject to restaurant availability. Once an order is confirmed, the payment will be processed via your selected method. Cancellations and refunds are handled according to our specific refund policy, which varies depending on whether the restaurant has started preparing your meal."
                )

                TermsText(
                    title = "4. Delivery Services",
                    text = "Delivery times provided are estimates. While we strive to deliver your food as quickly as possible, factors like traffic, weather, and restaurant preparation time may affect the final delivery window. You agree to provide a safe and accessible delivery location for our partners."
                )
            }
        }
    }
}

@Composable
fun TermsText(
    title: String,
    text: String,
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HeaderListBlackTitle(
            title = title
        )
        Text(
            text = text,
            style = LocalCustomTypography.current.bodyMediumRegular,
            color = LocalCustomColors.current.text
        )
    }
}

//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun TermsLightPreview() {
//    TasstyTheme{
//        TermsContent {  }
//    }
//}
//
//@Preview(showBackground = true, name = "Dark Mode")
//@Composable
//fun TermsDarkPreview() {
//    TasstyTheme(darkTheme = true){
//        TermsContent {  }
//    }
//}