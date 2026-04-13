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
import androidx.compose.ui.res.stringResource


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
                title = stringResource(R.string.terms_of_service_license_warranty_agreement)
            )

            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)) {
                Text(
                    text = stringResource(R.string.last_update),
                    style = LocalCustomTypography.current.bodyMediumRegular,
                    color = LocalCustomColors.current.text
                )
                Text(
                    text = stringResource(R.string.mar_2026),
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
                    title = stringResource(R.string.your_agreement),
                    text = stringResource(R.string.your_agreement_detail)
                )

                TermsText(
                    title = stringResource(R.string.privacy_data),
                    text = stringResource(R.string.privacy_data_detail)
                )

                TermsText(
                    title = stringResource(R.string.orders_payments),
                    text = stringResource(R.string.order_payment_detail)
                )

                TermsText(
                    title = stringResource(R.string.delivery_services),
                    text = stringResource(R.string.delivery_services_detail)
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
@Composable
fun TermsLightPreview() {
    TasstyTheme{
        TermsContent {  }
    }
}

//@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun TermsDarkPreview() {
    TasstyTheme(darkTheme = true){
        TermsContent {  }
    }
}