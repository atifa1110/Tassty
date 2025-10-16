package com.example.tassty.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.tassty.ui.theme.Neutral20

@Composable
fun ShimmerRestaurantGridCard (){
    Card(
        modifier = Modifier.width(156.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral20),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(140.dp,100.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .shimmerLoadingAnimation()
            )

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth().clip(RoundedCornerShape(20.dp))
                        .height(12.dp)
                        .shimmerLoadingAnimation()
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth().clip(RoundedCornerShape(20.dp))
                        .height(8.dp)
                        .shimmerLoadingAnimation()
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(0.5f).clip(RoundedCornerShape(20.dp))
                        .height(8.dp)
                        .shimmerLoadingAnimation()
                )
            }
        }
    }
}
