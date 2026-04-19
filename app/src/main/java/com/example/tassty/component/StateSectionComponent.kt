package com.example.tassty.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import kotlin.math.max


@Composable
fun EmptySearchResult(
    title: String
){
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HeaderListItemCountTitle(itemCount = 0,
            title = title,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        EmptySearchContent()
    }
}

@Composable
fun EmptySearchContent(){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(R.drawable.empty_search),
            contentDescription = "Empty Search Restaurant",
        )

        Spacer(Modifier.height(12.dp))

        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.we_couldn_t_find_this_menu),
            style = LocalCustomTypography.current.h2Bold,
            color = LocalCustomColors.current.headerText
        )

        Spacer(Modifier.height(10.dp))

        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.try_different),
            style = LocalCustomTypography.current.bodyMediumRegular,
            color = LocalCustomColors.current.text
        )
    }
}

@Composable
fun EmptyContent(
    title: String,
    highlight: String = stringResource(R.string.exclamation),
    subtitle: String,
    buttonText: String = "",
    icon: Int? = null,
    contentImage: @Composable () -> Unit = {
        icon?.let {
            Image(
                painter = painterResource(it),
                contentDescription = title,
                modifier = Modifier.size(260.dp)
            )
        }
    },
    onClick: (() -> Unit)? = null
){
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        contentImage()

        Spacer(Modifier.height(12.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CustomTwoColorText(
                fullText = title,
                highlightText = highlight,
                textColor = LocalCustomColors.current.headerText,
                normalStyle = LocalCustomTypography.current.h2Bold,
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = subtitle,
                style = LocalCustomTypography.current.bodyMediumRegular,
                color = LocalCustomColors.current.text
            )
            if (onClick != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onClick),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = buttonText,
                        style = LocalCustomTypography.current.bodyMediumMedium,
                        color = Orange500
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(R.drawable.arrow_left_up),
                        contentDescription = "action",
                        tint = Orange500,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyCartContent(
    onClick: (() -> Unit)?
){
    EmptyContent(
        title = stringResource(R.string.your_cart_is_empty),
        subtitle = stringResource(R.string.start_exploring_your_favorite),
        buttonText = "Find recommended restaurants",
        icon = R.drawable.empty_cart,
        onClick = onClick
    )
}

@Composable
fun EmptyAddressContent(){
    EmptyContent(
        title = stringResource(R.string.where_should_we_deliver),
        highlight = "?",
        subtitle = stringResource(R.string.no_have_any_saved_addresses),
        icon = R.drawable.empty_cart
    )
}

@Composable
fun EmptyCollectionContent(){
    EmptyContent(
        title = stringResource(R.string.your_collection_is_empty),
        subtitle = stringResource(R.string.add_your_favorite_menus_by_clicking),
        buttonText = stringResource(R.string.find_menus),
        icon = R.drawable.phone_boarding,
        onClick = {}
    )
}

@Composable
fun EmptyCollectionSmallContent(){
    EmptyContent(
        title = stringResource(R.string.your_collection_is_empty),
        subtitle = stringResource(R.string.add_your_favorite_menus_by_clicking),
        icon = R.drawable.phone_boarding,
    )
}


@Composable
fun EmptyFavoriteContent(
    onClick: () -> Unit
){
    EmptyContent(
        title = stringResource(R.string.your_list_is_empty),
        subtitle = stringResource(R.string.add_your_favorite_restaurants),
        buttonText = stringResource(R.string.find_restaurants),
        icon = R.drawable.phone_boarding,
        onClick = onClick
    )
}

@Composable
fun EmptyVoucherContent(){
    EmptyContent(
        title = stringResource(R.string.your_list_is_empty),
        subtitle = stringResource(R.string.dont_have_any_available_voucher),
        icon = R.drawable.phone_boarding
    )
}

@Composable
fun EmptyChatContent(){
    EmptyContent(
        title = stringResource(R.string.your_message_is_empty),
        subtitle = stringResource(R.string.the_message_from_your_order),
        icon = R.drawable.phone_boarding
    )
}

@Composable
fun EmptyNotificationContent(){
    EmptyContent(
        title = stringResource(R.string.your_notification_is_empty),
        subtitle = stringResource(R.string.not_received_any_notifications_yet),
        contentImage = {
            NotificationIcon()
        },
    )
}

@Composable
fun EmptyCardContent(){
    EmptyContent(
        title = stringResource(R.string.no_cards_added_yet),
        subtitle = stringResource(R.string.not_added_any_debit),
        icon = R.drawable.phone_boarding
    )
}

@Composable
fun EmptyOrderContent(){
    EmptyContent(
        title = stringResource(R.string.no_order_yet),
        highlight = "?",
        subtitle = stringResource(R.string.no_ordered_anything),
        icon = R.drawable.phone_boarding
    )
}

@Composable
fun StatusContent(
    title: String,
    subtitle: String,
    icon: Int,
){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(icon),
            contentDescription = "status content",
        )

        Spacer(Modifier.height(12.dp))
        StatusModalContent(title,subtitle)
    }
}

@Composable
fun LoadingScreen(){
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        CircularProgressIndicator(
            color = Orange500,
            strokeWidth = 3.dp,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
fun LoadingOverlay(
    isLoading: Boolean,
    text: String = stringResource(R.string.sending),
    isEmpty: Boolean = false
) {
    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalCustomColors.current.headerText.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            if(!isEmpty) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(color = Green500)
                    Text(text, color = Neutral10)
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(
    onClick: () -> Unit = {}
){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(R.drawable.sorry),
            contentDescription = "Sorry",
            contentScale = ContentScale.FillWidth
        )

        Spacer(Modifier.height(12.dp))

        Text(
            textAlign = TextAlign.Center,
            text = "Sorry!",
            style = LocalCustomTypography.current.h2Bold,
            color = LocalCustomColors.current.headerText
        )

        Spacer(Modifier.height(8.dp))

        Text(
            textAlign = TextAlign.Center,
            text = "Failed to connect. Please check \nyour internet connection.",
            style = LocalCustomTypography.current.bodyLargeRegular,
            color = LocalCustomColors.current.text
        )

        Spacer(Modifier.height(24.dp))

        ButtonComponent(
            enabled = true,
            labelResId = R.string.refresh,
            onClick = onClick
        )
    }
}

@Composable
fun LoadingRowState(){
    Row (modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center){
        CircularProgressIndicator(color = Orange500)
    }
}

@Composable
fun ErrorListState(
    title : String,
    onRetry: () -> Unit
){
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = title,
            style = LocalCustomTypography.current.h5Bold,
            color = LocalCustomColors.current.headerText
        )
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onRetry),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                tint = LocalCustomColors.current.text,
                contentDescription = "refresh",
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Refresh",
                style = LocalCustomTypography.current.bodySmallMedium,
                color = LocalCustomColors.current.text
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ErrorPreview(){
//    Column(Modifier.fillMaxSize()) {
//        EmptyNotificationContent()
//        EmptyChatContent()
//    }
//}

