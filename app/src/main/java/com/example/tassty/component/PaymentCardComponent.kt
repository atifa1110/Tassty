package com.example.tassty.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.model.CardColorOption
import com.example.tassty.model.PatternImage
import com.example.tassty.model.patterns
import com.example.tassty.ui.theme.Blue200
import com.example.tassty.ui.theme.Blue50
import com.example.tassty.ui.theme.Green100
import com.example.tassty.ui.theme.Green200
import com.example.tassty.ui.theme.Green50
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange200
import com.example.tassty.ui.theme.Orange50
import com.example.tassty.ui.theme.Pink200
import com.example.tassty.ui.theme.Pink50

@Composable
fun DebitPaymentCard(
    cardNumber: String ="7786567890987654" ,
    cardName: String = "Rafiq Daniel",
    expireDate: String ="10/28",
    @DrawableRes logo: Int = R.drawable.visa,
    @DrawableRes backgroundImage: Int = R.drawable.card_pattern_3,
    borderColor : Color = Green100,
    boxBackground: Color = Green50
) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column (modifier = Modifier
            .fillMaxSize()
            .background(Neutral10)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f).background(boxBackground)
            ) {
                Image(
                    modifier = Modifier.fillMaxWidth().clipToBounds(),
                    painter = painterResource(id = backgroundImage),
                    contentDescription = cardName,
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(borderColor)
                )

                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = logo),
                        contentDescription = "mastercard logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(45.dp,30.dp),
                    )

                    Text(
                        text = cardNumber.chunked(4).joinToString("  "),
                        style = LocalCustomTypography.current.h3Bold,
                        color = Neutral100
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cardName,
                    style = LocalCustomTypography.current.h7Bold,
                    color = Neutral100
                )
                Text(
                    text = expireDate,
                    style = LocalCustomTypography.current.bodySmallMedium,
                    color = Neutral70
                )
            }
        }
    }
}

@Composable
fun DebitSmallPaymentCard(
    cardNumber: String = "1234567890987654",
    cardName: String = "Rafiq Daniel",
    @DrawableRes backgroundImage: Int = R.drawable.card_pattern_3,
    borderColor: Color = Pink200,
    boxBackground: Color = Pink50
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mastercard),
                    contentDescription = "mastercard logo",
                    modifier = Modifier.size(50.dp),
                )
            }

            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxHeight()
                    .background(boxBackground)
            ) {
                Image(
                    painter = painterResource(id = backgroundImage),
                    contentDescription = cardName,
                    modifier = Modifier.fillMaxSize().clipToBounds()
                        .scale(1.5f),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(borderColor)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.End),
                        text = cardNumber.chunked(4).joinToString("  "),
                        color = Neutral100,
                        style = LocalCustomTypography.current.bodyMediumBold
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = cardName,
                        modifier = Modifier.align(Alignment.End),
                        color = Neutral70,
                        style = LocalCustomTypography.current.bodySmallSemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun EWalletSmallPaymentCard(
    cardName: String = "OVO E-Wallet",
    borderColor: Color = Orange200,
    @DrawableRes logo: Int = R.drawable.ewallet_ovo
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = logo),
                    contentDescription = "mastercard logo",
                    modifier = Modifier.size(50.dp),
                )
            }

            VerticalDivider(color = borderColor)

            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxHeight().
                background(Neutral10)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = cardName,
                        modifier = Modifier.align(Alignment.Start),
                        color = Neutral100,
                        style = LocalCustomTypography.current.bodyMediumBold
                    )
                }
            }
        }
    }
}

@Composable
fun CardColorItem(
    option: CardColorOption,
    isSelected: Boolean,
    onClick: (CardColorOption) -> Unit
) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(option.backgroundColor)
            .border(
                width = 1.dp,
                color = if (isSelected) option.borderColor else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick(option) },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = option.borderColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun CardBackgroundItem(
    modifier: Modifier = Modifier,
    patternImage: PatternImage,
    isSelected: Boolean,
    colorOption: CardColorOption,
    onClick: (PatternImage) -> Unit
) {
    Box(
        modifier = modifier
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = if (isSelected) colorOption.borderColor else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick(patternImage) }
    ) {
        Box(modifier = Modifier.size(100.dp,60.dp).background(
            if (isSelected) colorOption.imageBackground else Neutral20)) {
            Image(
                painter = painterResource(id = patternImage.imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().clipToBounds().scale(1.5f),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(
                    if (isSelected) colorOption.backgroundColor else Neutral60
                )
            )
        }

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = colorOption.borderColor,
                modifier = Modifier.align(Alignment.Center).size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentCardPreview() {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DebitPaymentCard(
            logo = R.drawable.mastercard
        )
        DebitPaymentCard(
            backgroundImage = R.drawable.card_pattern_1,
            boxBackground = Orange50,
            borderColor = Orange200
        )
        DebitPaymentCard(
            logo = R.drawable.mastercard,
            boxBackground =Pink50,
            borderColor = Pink200
        )
        DebitPaymentCard(
            backgroundImage = R.drawable.card_pattern_2,
            boxBackground = Blue50,
            borderColor = Blue200
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentSmallCardPreview() {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DebitSmallPaymentCard()
        DebitSmallPaymentCard(
            boxBackground =Blue50,
            borderColor = Blue200
        )
        DebitSmallPaymentCard(
            backgroundImage = R.drawable.card_pattern_2,
            boxBackground =Green50,
            borderColor = Green200
        )
        DebitSmallPaymentCard(
            backgroundImage = R.drawable.card_pattern_1,
            boxBackground =Orange50,
            borderColor = Orange200
        )

        EWalletSmallPaymentCard()
        EWalletSmallPaymentCard(
            cardName = "Dana E-Wallet",
            logo = R.drawable.dana
        )
    }
}
