package com.example.tassty.screen.detailVoucher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.core.domain.model.RestaurantStatus
import com.example.tassty.R
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.CommonImage
import com.example.tassty.component.DetailVoucherTopAppBar
import com.example.tassty.component.Divider
import com.example.tassty.component.Divider32
import com.example.tassty.component.HeaderListBlackTitle
import com.example.tassty.component.PromoIcon
import com.example.tassty.screen.rating.HeaderIconText
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500
import com.example.tassty.ui.theme.TasstyTheme

@Composable
fun DetailVoucherScreen() {

}

@Composable
fun DetailVoucherContent() {
    Scaffold(
        containerColor = LocalCustomColors.current.background,
        bottomBar = {
            Column(Modifier.background(LocalCustomColors.current.modalBackgroundFrame)) {
                ButtonComponent(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    labelResId = R.string.use_voucher,
                    onClick = {}
                )
            }
        }
    ) { padding ->
        BoxWithConstraints(
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            val screenHeight = maxHeight

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                item(key = "header"){
                    HeaderContent(
                        imageUrl = "https://www.byblos.com/wp-content/uploads/Restaurant-IL-Giardino_Hotel-Byblos_Saint-Tropez-©Stephan-Julliard-7-1600x1000.jpg",
                        fixedHeight = screenHeight
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item(key = "description_content"){
                    VoucherDescription()
                    Divider32()
                    HowToUseVoucher()
                }
            }

            DetailVoucherTopAppBar(
                onBackClick = {},
                onCalendarCLick = {}
            )
        }
    }
}

@Composable
fun HeaderContent(
    fixedHeight: Dp,
    name: String = "Discount Food 50%+",
    imageUrl: String
){
    val imageHeight = fixedHeight * 0.4f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        CommonImage(
            imageUrl = imageUrl,
            name = "detail header image",
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        )

        Column(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomEnd)
                .background(LocalCustomColors.current.modalBackgroundFrame),
            verticalArrangement = Arrangement.Center
        ) {
            HeaderVoucherName(name = name)
            Divider(modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp), color = Neutral40)
            HeaderVoucherInfo()
        }
    }
}

@Composable
fun HeaderVoucherName(
    name: String
){
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 24.dp, start = 24.dp, end = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            style = LocalCustomTypography.current.h3Bold,
            color = LocalCustomColors.current.headerText
        )
        PromoIcon(RestaurantStatus.OPEN)
    }
}

@Composable
fun HeaderVoucherInfo(
    date: String = "12 Oct 24",
    minTransaction: String = "15.000",
    payment: String = "E-Wallet"
){
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 24.dp,
            end = 24.dp, bottom = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HeaderIconText(
            text = date,
            icon = R.drawable.calendar,
            iconColor = Green500
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.currency),
                contentDescription = "Date",
                tint = Pink500,
                modifier = Modifier.size(16.dp).align(Alignment.CenterVertically)
            )
            Text(
                text = "min",
                style = LocalCustomTypography.current.bodySmallMedium,
                color = LocalCustomColors.current.text
            )
            Text(
                text = minTransaction,
                style = LocalCustomTypography.current.h6Bold,
                color = LocalCustomColors.current.headerText
            )
        }
        HeaderIconText(
            text = payment,
            icon = R.drawable.credit_card,
            iconColor = Blue500
        )
    }
}

@Composable
fun VoucherDescription(){
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderListBlackTitle(
            title = "About this voucher"
        )
        InstructionItem(
            content = buildAnnotatedString {
                append("Enjoy special discounts with our exclusive voucher! Shop now with a minimum purchase of ")
                withStyle(
                    style = LocalCustomTypography.current.bodyMediumSemiBold.toSpanStyle().copy(
                        color = LocalCustomColors.current.headerText
                    )
                ) {
                    append("$15")
                }
                append(" and get a ")
                withStyle(
                    style = LocalCustomTypography.current.bodyMediumSemiBold.toSpanStyle().copy(
                        color = LocalCustomColors.current.headerText
                    )
                ) {
                    append("50% OFF")
                }
                append(" up to ")
                withStyle(
                    style = LocalCustomTypography.current.bodyMediumSemiBold.toSpanStyle().copy(
                        color = LocalCustomColors.current.headerText
                    )
                ) {
                    append("$25")
                }
                append(" at checkout.")
            }
        )
        Spacer(Modifier.height(4.dp))
        InstructionItem(
            content = buildAnnotatedString {
                append("Enhance your shopping experience with this opportunity. Save more and seize the best deals by using our voucher")
                withStyle(style = SpanStyle(color = LocalCustomColors.current.headerText)) {
                    append("!")
                }
            }
        )
    }
}

@Composable
fun HowToUseVoucher(){
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderListBlackTitle(
            title = "How to use this voucher?"
        )
        InstructionItem(
            content = buildAnnotatedString {
                append("1. Make sure you have already updated your ")
                withStyle(
                    style = LocalCustomTypography.current.bodyMediumSemiBold.toSpanStyle().copy(
                        color = Orange500
                    )
                ) {
                    append("Tassty!")
                }
                append(" apps to the latest version")
            }
        )

        InstructionItem(
            content = AnnotatedString("2. Go to the \"Profile\" and find \"Promo & Vouchers\"")
        )

        InstructionItem(
            content = AnnotatedString("3. Choose voucher to be used")
        )
        InstructionItem(
            content = AnnotatedString("4. Click \"Use voucher\" and you will land to recommendation foods page")
        )
    }
}

@Composable
fun InstructionItem(
    content: AnnotatedString
) {
    Text(
        text = content,
        color = LocalCustomColors.current.text,
        style = LocalCustomTypography.current.bodyMediumRegular,
        modifier = Modifier.fillMaxWidth()
    )
}

//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun DetailVoucherLightPreview() {
//    TasstyTheme {
//        DetailVoucherContent()
//    }
//}

//@Preview(showBackground = true, name = "Dark Mode")
//@Composable
//fun DetailVoucherDarkPreview() {
//    TasstyTheme(darkTheme = true) {
//        DetailVoucherContent()
//    }
//}