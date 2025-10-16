package com.example.tassty.component

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.domain.model.RestaurantStatus
import com.example.core.ui.model.VoucherUiModel
import com.example.tassty.R
import com.example.tassty.getSampleVouchers
import com.example.tassty.model.Voucher
import com.example.tassty.toCleanRupiahFormat
import com.example.tassty.toUiDateString
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange50
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500
import com.example.tassty.voucherItem

@Composable
fun VoucherCard(
    voucher: Voucher,
    status: RestaurantStatus
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Neutral10
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PromoIcon(status = status)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DiscountFoodContent(title = voucher.title, description = voucher.description)
                HorizontalDivider(color = Neutral30)
                DateAndMinTransactionContent(
                    date = voucher.expiryDate.toUiDateString(),
                    minTransaction = voucher.minOrderValue
                )
            }
        }
    }
}

@Composable
fun VoucherSelectorCard(
    voucher : Voucher,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable{ onCheckedChange(voucher.isSelected) },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(width= 1.dp, color = if(voucher.isSelected) Orange500 else Neutral40),
        colors = CardDefaults.cardColors(
            containerColor =  if(voucher.isSelected) Orange50 else Neutral10
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
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
                    date = voucher.expiryDate.toUiDateString(),
                    minTransaction = voucher.minOrderValue
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier=Modifier.height(85.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Checkbox(
                    checked = voucher.isSelected,
                    onCheckedChange = onCheckedChange,
                    modifier = Modifier.padding(0.dp).size(24.dp),
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
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Neutral10
        ),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(R.drawable.sale),
                    contentDescription = "voucher",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .aspectRatio(3f / 1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PromoIcon(status = RestaurantStatus.OPEN)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = voucher.voucher.title,
                        style = LocalCustomTypography.current.h5Bold,
                        color = Neutral100
                    )

                    DateAndMinTransactionContent(
                        date = voucher.expireLabel,
                        minTransaction = voucher.voucher.minOrderValue
                    )
                }
            }
        }
    }
}

@Composable
fun VoucherExtraLargeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Neutral10
        ),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(R.drawable.sale),
                    contentDescription = "voucher",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .aspectRatio(3f / 1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PromoIcon(status = RestaurantStatus.OPEN)
                Column(modifier = Modifier.fillMaxWidth()) {
                    DiscountFoodContent()
                    HorizontalDivider(Modifier.padding(vertical = 12.dp))
                    DateAndMinTransactionContent()
                }
            }
        }
    }
}

@Composable
fun DateAndMinTransactionContent(
    date: String = "12 Oct 2024",
    minTransaction : Int = 0
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.calendar),
                contentDescription = "Date",
                tint = Green500,
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = date,
                style = LocalCustomTypography.current.bodySmallMedium,
                color = Neutral70
            )
        }

        Box(
            modifier = Modifier.size(3.dp)
                .background(Neutral40).clip(CircleShape)
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
                color = Neutral70
            )
            Text(
                text = minTransaction.toCleanRupiahFormat(),
                style = LocalCustomTypography.current.h6Bold,
                color = Neutral100
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
            color = Neutral100
        )
        Text(
            text = description,
            style = LocalCustomTypography.current.bodySmallMedium,
            color = Neutral70,
            maxLines = 1
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
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        VoucherCard(voucher = getSampleVouchers()[0], status = RestaurantStatus.CLOSED)
        VoucherLargeCard(voucher = voucherItem)
        VoucherExtraLargeCard()
        VoucherSelectorCard(
            voucher = getSampleVouchers()[0] ,
            onCheckedChange = {})
    }
}