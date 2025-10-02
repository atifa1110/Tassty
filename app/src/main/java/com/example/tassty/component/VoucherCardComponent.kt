package com.example.tassty.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Pink500

@Composable
fun VoucherCard(
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
            ) {
                DiscountFoodContent()
                HorizontalDivider(modifier=Modifier.padding(vertical = 12.dp))
                DateAndMinTransactionContent()
            }
        }
    }
}

@Composable
fun VoucherLargeCard() {
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
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
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
                        text = "Discount Food 50%+",
                        style = LocalCustomTypography.current.h5Bold,
                        color = Neutral100
                    )

                    DateAndMinTransactionContent()
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
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
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
fun DateAndMinTransactionContent(){
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
                modifier = Modifier.size(14.dp)
            )

            Text(
                text = "12 Oct 24",
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
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "min",
                style = LocalCustomTypography.current.bodySmallMedium,
                color = Neutral70
            )
            Text(
                text = "Rp15.000",
                style = LocalCustomTypography.current.h6Bold,
                color = Neutral100
            )

        }
    }
}

@Composable
fun DiscountFoodContent(){
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "Discount Food 50%+",
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100
        )
        Text(
            text = "Use with eWallet",
            style = LocalCustomTypography.current.bodySmallMedium,
            color = Neutral70
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewVoucherScreen() {
    Column (
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        VoucherCard(status = RestaurantStatus.CLOSED)
        VoucherLargeCard()
        VoucherExtraLargeCard()
    }
}