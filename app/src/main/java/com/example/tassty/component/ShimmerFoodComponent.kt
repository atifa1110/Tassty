package com.example.tassty.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.model.MenuUiModel
import com.example.tassty.ui.theme.Neutral20

@Composable
fun ShimmerFoodGridCard() {
    Card(
        modifier = Modifier.width(140.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral20),
    ) {
        Column (modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(CircleShape)
                    .shimmerLoadingAnimation()
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
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
                        .height(10.dp)
                        .shimmerLoadingAnimation()
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth().clip(RoundedCornerShape(20.dp))
                        .height(12.dp)
                        .shimmerLoadingAnimation()
                )
            }
        }
    }
}

@Composable
fun ShimmerFoodLargeGridCard(
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier.width(157.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Neutral20
        ),
    ) {
        Column (modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(137.dp)
                    .clip(CircleShape)
                    .shimmerLoadingAnimation()
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
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
                        .height(10.dp)
                        .shimmerLoadingAnimation()
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth().clip(RoundedCornerShape(20.dp))
                        .height(12.dp)
                        .shimmerLoadingAnimation()
                )
            }
        }
    }
}

@Composable
fun ShimmerFoodWideListCard(){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral20)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(
                modifier = Modifier.size(120.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .shimmerLoadingAnimation()
            )

            Column(
                Modifier.padding(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth().clip(RoundedCornerShape(20.dp))
                        .height(14.dp)
                        .shimmerLoadingAnimation()
                )
                Spacer(
                    modifier = Modifier
                        .width(140.dp).clip(RoundedCornerShape(20.dp))
                        .height(14.dp)
                        .shimmerLoadingAnimation()
                )

                Spacer(
                    modifier = Modifier
                        .width(100.dp).clip(RoundedCornerShape(20.dp))
                        .height(14.dp)
                        .shimmerLoadingAnimation()
                )
            }

        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewShimmerFood(
//){
//    ShimmerFoodWideListCard()
//}