package com.example.tassty.component

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.model.CardUserUiModel
import com.example.core.ui.model.PaymentChannelUiModel
import com.example.tassty.getPaymentIcon
import com.example.tassty.model.CardColorOption
import com.example.tassty.model.PatternImage
import com.example.tassty.model.toCardColor
import com.example.tassty.model.toLogoRes
import com.example.tassty.model.toPatternRes
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.util.cardList
import com.example.tassty.util.paymentChannel

@Composable
fun ShimmerDebitPaymentCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Neutral20),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Neutral10)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.5f)
                    .shimmerLoadingAnimation()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Box(
                        modifier = Modifier
                            .size(45.dp, 30.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerLoadingAnimation()
                    )

                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .height(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerLoadingAnimation()
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
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerLoadingAnimation()
                )
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerLoadingAnimation()
                )
            }
        }
    }
}

@Composable
fun DebitPaymentCard(
    card: CardUserUiModel,
    logo: Int = card.cardBrand.toLogoRes(),
    isSelected: Boolean = false,
    backgroundImage: Int = card.themeBackground.toPatternRes(),
    color : CardColorOption = card.themeColor.toCardColor(),
    onCheckChanged: () -> Unit
) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp).clickable(onClick = onCheckChanged),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, color.borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column (modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.background)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
                .background(color.imageBackground)
            ) {
                Image(
                    modifier = Modifier.fillMaxWidth().clipToBounds(),
                    painter = painterResource(id = backgroundImage),
                    contentDescription = card.cardholderName,
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(color.backgroundColor)
                )

                if(isSelected) {
                    Checkbox(
                        checked = card.isSelected,
                        onCheckedChange = {onCheckChanged()},
                        modifier = Modifier.align(Alignment.TopEnd),
                        colors = CheckboxDefaults.colors(
                            checkmarkColor = Neutral10,
                            checkedColor = Orange500,
                            uncheckedColor = Neutral40,
                        )
                    )
                }

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
                        text = card.maskedNumber,
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
                    text = card.cardholderName,
                    style = LocalCustomTypography.current.h7Bold,
                    color = LocalCustomColors.current.headerText
                )
                Text(
                    text = card.expDate,
                    style = LocalCustomTypography.current.bodySmallMedium,
                    color = LocalCustomColors.current.text
                )
            }
        }
    }
}


@Composable
fun DebitSmallPaymentCard(
    card: CardUserUiModel,
    logo: Int = card.cardBrand.toLogoRes(),
    backgroundImage: Int = card.themeBackground.toPatternRes(),
    color : CardColorOption = card.themeColor.toCardColor()
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(Neutral10),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color.borderColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = logo),
                    contentDescription = "mastercard logo",
                    modifier = Modifier.size(50.dp),
                )
            }

            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxHeight()
                    .background(color.imageBackground)
            ) {
                Image(
                    painter = painterResource(id = backgroundImage),
                    contentDescription = card.cardholderName,
                    modifier = Modifier.fillMaxSize().clipToBounds()
                        .scale(1.5f),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(color.backgroundColor)
                )


                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.End),
                        text = card.maskedNumber,
                        color = Neutral100,
                        style = LocalCustomTypography.current.bodyMediumBold
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = card.cardholderName,
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
fun PaymentChannelCard(
    channel: PaymentChannelUiModel,
    onCheckChanged: () -> Unit,
) {
    val borderColor = if (channel.isSelected) Orange500 else LocalCustomColors.current.border
    val textColor = if (channel.isEnabled) LocalCustomColors.current.headerText else LocalCustomColors.current.text

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(onClick = onCheckChanged),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.background)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = getPaymentIcon(channel.iconKey)),
                    contentDescription = channel.name,
                    modifier = Modifier.size(60.dp, 30.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = if (!channel.isEnabled) {
                        ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }) // Greyscale jika mati
                    } else null
                )
            }

            VerticalDivider(
                modifier = Modifier.fillMaxHeight(),
                thickness = 1.dp,
                color = LocalCustomColors.current.border
            )

            Row(
                modifier = Modifier
                    .weight(0.65f)
                    .fillMaxHeight()
                    .background(LocalCustomColors.current.background),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = channel.name,
                    color = textColor,
                    style = LocalCustomTypography.current.bodyLargeBold,
                    modifier = Modifier.weight(1f).padding(start = 12.dp)
                )

                Checkbox(
                    modifier = Modifier.padding(end = 4.dp),
                    enabled = channel.isEnabled,
                    checked = channel.isSelected,
                    onCheckedChange = { onCheckChanged() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Orange500,
                        uncheckedColor = Neutral40,
                    )
                )
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

//@Preview(showBackground = true)
//@Composable
//fun PaymentCardPreview() {
//    Column(
//        modifier = Modifier.fillMaxSize().padding(24.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        DebitPaymentCard(
//            card = cardList[0],
//            isSelected = true,
//            onCheckChanged = {}
//        )
//
//        ShimmerDebitPaymentCard()
//
//        PaymentChannelCard(
//            channel = paymentChannel[0]
//        ) { }
//    }
//}


