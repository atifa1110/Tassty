package com.example.tassty.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.core.domain.model.RestaurantStatus
import com.example.core.ui.model.VoucherUiModel
import com.example.tassty.R
import com.example.tassty.screen.rating.HeaderIconText
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange50
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Orange900
import com.example.tassty.ui.theme.Pink500
import com.example.tassty.util.voucherUiModel

@Composable
fun SpecialCardOffer(){
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(174.dp),
        shape = RoundedCornerShape(20.dp),
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.0f to Color(0xFFFFCF24),
                        0.60f to Color(0xFFF07C2A),
                        0.82f to Color(0xFFD76413)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                )
            )
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .align(Alignment.CenterStart),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.happy_sunday),
                        color = Color.White,
                        style = LocalCustomTypography.current.h2ExtraBold
                    )
                    Text(
                        text = stringResource(R.string.get_50_discount),
                        color = Color.White,
                        style = LocalCustomTypography.current.bodyMediumMedium
                    )

                }
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange900
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = stringResource(R.string.get_now), color = Neutral10,
                            style = LocalCustomTypography.current.bodySmallMedium)
                        Icon(
                            painter = painterResource(R.drawable.arrow_left_up),
                            contentDescription = "arrow left up",
                            tint = Neutral10
                        )
                    }
                }
            }

            Image(
                painter = painterResource(R.drawable.kiwi),
                contentDescription = "Banner Image",
                modifier = Modifier
                    .size(185.dp)
                    .offset(y = 8.dp)
                    .zIndex(1f)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
fun VoucherCard(
    voucher: VoucherUiModel,
    status: RestaurantStatus
) {
    Card(
        modifier = Modifier.width(320.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalCustomColors.current.cardBackground2
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PromoIcon(status = status)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DiscountFoodContent(
                    title = voucher.title,
                    description = voucher.description
                )
                HorizontalDivider(color = LocalCustomColors.current.divider)
                DateAndMinTransactionContent(
                    date = voucher.expireLabel,
                    minTransaction = voucher.formatMinOrder
                )
            }
        }
    }
}

@Composable
fun VoucherSelectorCard(
    voucher : VoucherUiModel,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(voucher.isSelected) },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(width= 1.dp, color = if(voucher.isSelected) Orange500 else Neutral40),
        colors = CardDefaults.cardColors(
            containerColor =  if(voucher.isSelected) LocalCustomColors.current.selectedOrangeBackground else LocalCustomColors.current.background
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            PromoIcon(status = RestaurantStatus.OPEN)
            Spacer(Modifier.width(10.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DiscountFoodContent(
                    title = voucher.title,
                    description = voucher.description
                )
                HorizontalDivider(color =  if(voucher.isSelected) Neutral40 else Neutral30)
                DateAndMinTransactionContent(
                    date = voucher.expireLabel,
                    minTransaction = voucher.formatMinOrder
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier=Modifier.height(85.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Checkbox(
                    checked = voucher.isSelected,
                    onCheckedChange = onCheckedChange,
                    modifier = Modifier
                        .padding(0.dp)
                        .size(24.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = Orange500,
                        uncheckedColor = Neutral40
                    )
                )
            }
        }
    }
}

@Composable
fun VoucherLargeCard(
    voucher : VoucherUiModel
) {
    Card(
        modifier = Modifier.width(320.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalCustomColors.current.background
        ),
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(R.drawable.sale),
                    contentDescription = "voucher",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .aspectRatio(3f / 1f)
                )
            }

            Row(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PromoIcon(status = RestaurantStatus.OPEN)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = voucher.title,
                        style = LocalCustomTypography.current.h5Bold,
                        color = LocalCustomColors.current.headerText,
                        maxLines = 1
                    )

                    DateAndMinTransactionContent(
                        date = voucher.expireLabel,
                        minTransaction = voucher.formatMinOrder
                    )
                }
            }
        }
    }
}

@Composable
fun VoucherExtraLargeCard(
    voucher: VoucherUiModel
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalCustomColors.current.cardBackground
        ),
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(R.drawable.sale),
                    contentDescription = "voucher",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PromoIcon(status = RestaurantStatus.OPEN)
                Column(modifier = Modifier.fillMaxWidth()) {
                    DiscountFoodContent(
                        title = voucher.title,
                        description = voucher.description
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp),
                        color = LocalCustomColors.current.divider)
                    DateAndMinTransactionContent(
                        date = voucher.expireLabel,
                        minTransaction = voucher.formatMinOrder
                    )
                }
            }
        }
    }
}

@Composable
fun DateAndMinTransactionContent(
    date: String = "12 Oct 2024",
    minTransaction : String = "Rp0"
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderIconText(
            icon = R.drawable.calendar,
            text = date,
            iconColor = Green500,
            style = LocalCustomTypography.current.bodySmallMedium,
        )

        Box(
            modifier = Modifier
                .size(3.dp)
                .background(Neutral40)
                .clip(CircleShape)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.currency),
                contentDescription = "Date",
                tint = Pink500,
                modifier = Modifier.size(16.dp)
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
    }
}

@Composable
fun DiscountFoodContent(
    title: String = "Discount Food 50% +",
    description: String = "Use with Wallet",
){
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = title,
            style = LocalCustomTypography.current.h5Bold,
            color = LocalCustomColors.current.headerText,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = description,
            style = LocalCustomTypography.current.bodySmallMedium,
            color = LocalCustomColors.current.text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun VoucherDiscount(
    title: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier
        .clip(RoundedCornerShape(24.dp))
        .background(Pink500)
        .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = LocalCustomTypography.current.bodyXtraSmallSemiBold,
            color = Neutral10
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVoucherScreen() {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        VoucherCard(voucher = voucherUiModel[0], status = RestaurantStatus.CLOSED)
        VoucherLargeCard(voucher = voucherUiModel[0])
        VoucherExtraLargeCard(voucher = voucherUiModel[0])
        VoucherSelectorCard(
            voucher = voucherUiModel[0] ,
            onCheckedChange = {})
    }
}