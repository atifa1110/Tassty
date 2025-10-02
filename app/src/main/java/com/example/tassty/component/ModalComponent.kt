package com.example.tassty.component

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.collection
import com.example.tassty.model.FavoriteCollection
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.operationalHours
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import kotlin.collections.take

@Composable
fun CustomBottomSheet(
    visible: Boolean,
    dismissOnClickOutside: Boolean = true,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val transition = updateTransition(targetState = visible,
        label = "BottomSheetTransition")

    // Animasi untuk background hitam
    val scrimAlpha by transition.animateFloat(
        transitionSpec = { tween(300) }, label = "ScrimAlpha"
    ) { if (it) 0.5f else 0f }

    // Animasi untuk sheet (slide dari bawah)
    val offsetY by transition.animateDp(
        transitionSpec = { tween(300) }, label = "OffsetY"
    ) { show ->
        if (show) 0.dp else 500.dp // turun 500dp saat ditutup
    }

    if (scrimAlpha > 0f || visible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = scrimAlpha))
                .then(
                    if (dismissOnClickOutside) Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onDismiss() }
                    else Modifier
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(Modifier
                .offset(y = offsetY)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                content()

                Box(
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .size(width = 60.dp, height = 10.dp)
                        .clip(RoundedCornerShape(99.dp))
                        .background(Neutral10)
                        .shadow(
                            elevation = 4.dp,
                        )
                        .align(Alignment.TopCenter),
                    contentAlignment = Alignment.Center
                ) {
                    Spacer(
                        modifier = Modifier
                            .size(width = 25.dp, height = 1.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(Neutral70)
                    )
                }
            }
        }
    }
}

@Composable
fun CollectionContent(
    collections: List<FavoriteCollection>
){
    Column(modifier = Modifier.fillMaxWidth()
        .clip(RoundedCornerShape(topStart = 24.dp,
            topEnd = 24.dp))
        .background(Neutral20)
        .padding(top = 24.dp, bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal=24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextHeader(
                title = stringResource(R.string.save_to_collection),
                subtitle = stringResource(R.string.save_your_favorite)
            )

            TopBarButton(icon = Icons.Default.Add,
                boxColor = Orange500, iconColor = Neutral10
            ) {  }
        }

        HorizontalDivider(Modifier.padding(vertical = 32.dp))
        TitleListHeader(
            data = collections.size,
            text = "Collection"
        )
        Spacer(Modifier.height(12.dp))

        Column(Modifier.padding(horizontal = 24.dp)) {
            collections.take(2).forEach { collection ->
                CollectionCard(collection = collection)
                Spacer(Modifier.height(8.dp))
            }
            Spacer(Modifier.height(24.dp))
            ButtonComponent(
                enabled = false,
                labelResId = R.string.save,
                onClick = {}
            )
        }
    }
}

@Composable
fun CollectionAddContent(
){
    Column(modifier = Modifier.fillMaxWidth()
        .clip(RoundedCornerShape(topStart = 24.dp,
            topEnd = 24.dp))
        .background(Neutral20)
        .padding(top = 24.dp, bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal=24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text= "Create collection",
                style = LocalCustomTypography.current.h3Bold,
                color = Neutral100
            )

            TopBarButton(icon = R.drawable.arrow_left,
                boxColor = Neutral10, iconColor = Neutral100
            ) {  }
        }

        HorizontalDivider(Modifier.padding(vertical = 32.dp))

        Column(Modifier.padding(horizontal = 24.dp)) {
            Text(
                text= "Collection name",
                style = LocalCustomTypography.current.h5Bold,
                color = Neutral100
            )
            Spacer(Modifier.height(24.dp))
            ButtonComponent(
                enabled = false,
                labelResId = R.string.create,
                onClick = {}
            )
        }
    }
}

@Composable
fun ModalStatusContent(
    title: String,
    subtitle: String,
    buttonTitle: String,
    onClick:() -> Unit,
    imageContent : @Composable () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Color.White)
            .padding(top = 24.dp, bottom = 46.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        imageContent()

        StatusTextHeader(title = title,subtitle = subtitle)

        HorizontalDivider()

        // Button
        TextButton(text = buttonTitle,onClick = onClick)
    }
}

@Composable
fun DetailScheduleContent(
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Neutral20)
            .padding(top = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ItemImage(
                    imageUrl = "",
                    status = RestaurantStatus.OPEN,
                    name = "Restaurant Modal Popup",
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Indah Cafe",
                        style = LocalCustomTypography.current.h3Bold,
                        color = Neutral100
                    )
                    Text(
                        text = "Gerunung Lombok Tengah, Praya",
                        style = LocalCustomTypography.current.bodySmallMedium,
                        color = Neutral70
                    )
                }
            }

            TopBarButton(icon = R.drawable.phone,
                boxColor = Orange500, iconColor = Neutral10
            ) {  }
        }

        HorizontalDivider()

        Column(
            modifier = Modifier.padding(horizontal=24.dp),
        ) {
            Text(
                text = "Operational Hour",
                style = LocalCustomTypography.current.h5Bold,
                color = Neutral100
            )
            Spacer(Modifier.height(12.dp))
            operationalHours.forEach { day ->
                RestaurantOperationalCard(day)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewModalDialog() {
    CustomBottomSheet(
        visible = true,
        onDismiss = {}
    ) {
        CollectionAddContent()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailModal(){
    CustomBottomSheet(
        visible = true,
        onDismiss = {}
    ) {
        CollectionContent(collections = listOf(collection))
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewSaveFavorite(){
//    CustomBottomSheet(
//        visible = true,
//        dismissOnClickOutside = false,
//        onDismiss = { }
//    ) {
//        ModalStatusContent(
//            title = "Saved to my favorite restaurants!",
//            subtitle = "Lorem ipsum dolor sit amet, consectetur \nadipiscing elit, sed do eiusmod.",
//            buttonTitle ="Confirm",
//            onClick = {}
//        ){
//            Image(
//                painter = painterResource(id = R.drawable.success),
//                contentDescription = "Success Icon",
//                modifier = Modifier.size(64.dp)
//            )
//        }
//    }
//}