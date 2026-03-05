package com.example.tassty.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500


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
            color = Neutral100
        )

        Spacer(Modifier.height(10.dp))

        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.try_different),
            style = LocalCustomTypography.current.bodyMediumRegular,
            color = Neutral70
        )
    }
}

@Composable
fun EmptyContent(
    title: String,
    subtitle: String,
    buttonText: String,
    icon: Int,
    isButton: Boolean = true
){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(icon),
            contentDescription = title,
            modifier = Modifier.size(260.dp)
        )

        Spacer(Modifier.height(12.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = LocalCustomTypography.current.h2Bold.toSpanStyle()
                            .copy(color = Neutral100)
                    ) {
                        append(title)
                    }
                    withStyle(
                        style = LocalCustomTypography.current.h2Bold.toSpanStyle()
                            .copy(color = Orange500)
                    ) {
                        append("!")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = subtitle,
                style = LocalCustomTypography.current.bodyMediumRegular,
                color = Neutral70
            )
            if(isButton){
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                        contentDescription = "arrow click",
                        tint = Orange500,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyCartContent(){
    EmptyContent(
        title = stringResource(R.string.your_cart_is_empty),
        subtitle = stringResource(R.string.start_exploring_your_favorite),
        buttonText = "Find recommended restaurants",
        icon = R.drawable.empty_cart
    )
}

@Composable
fun EmptyAddressContent(){
    EmptyContent(
        title = "Where should we deliver?",
        subtitle = "You don't have any saved addresses right now. Add one to get started!",
        buttonText = "Find recommended restaurants",
        icon = R.drawable.empty_cart,
        isButton = false
    )
}

@Composable
fun EmptyCollectionContent(){
    EmptyContent(
        title = "Your collection \nis empty",
        subtitle = "You can add your favorite menus by clicking the love button on the menu details page.",
        buttonText = "Find menus",
        icon = R.drawable.empty_cart
    )
}

@Composable
fun EmptyFavoriteContent(){
    EmptyContent(
        title = "Your list is empty",
        subtitle = "You can add your favorite restaurants by clicking the love button on the restaurant details page.",
        buttonText = "Find restaurants",
        icon = R.drawable.phone_boarding
    )
}

@Composable
fun EmptyVoucherContent(){
    EmptyContent(
        title = "Your list is empty",
        subtitle = "You don't have any available voucher right now, please stay tune",
        buttonText = "",
        icon = R.drawable.phone_boarding,
        isButton = false
    )
}

@Composable
fun EmptyChatContent(){
    EmptyContent(
        title = "Your message is empty",
        subtitle = "The message from your order & Driver \nwill be displayed here.",
        buttonText = "",
        icon = R.drawable.phone_boarding,
        isButton = false
    )
}

@Composable
fun EmptyNotificationContent(){
    EmptyContent(
        title = "Your notification is empty",
        subtitle = "You haven't received any notifications yet. \nWe'll let you know when something arrives!",
        buttonText = "",
        icon = R.drawable.phone_boarding,
        isButton = false
    )
}

@Composable
fun EmptyOrderContent(){
    EmptyContent(
        title = "No order yet?",
        subtitle = "It looks like you haven't ordered anything. \nLet’s find something delicious for you!",
        buttonText = "",
        icon = R.drawable.phone_boarding,
        isButton = false
    )
}


@Composable
fun DoubleCheckContent(){
    StatusContent(
        title = stringResource(R.string.please_double_check_your_order),
        subtitle = stringResource(R.string.we_cant_cancel_your_order),
        icon = R.drawable.empty_search
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

        Text(
            textAlign = TextAlign.Center,
            text = title,
            style = LocalCustomTypography.current.h2Bold,
            color = Neutral100
        )

        Spacer(Modifier.height(10.dp))

        Text(
            textAlign = TextAlign.Center,
            text = subtitle,
            style = LocalCustomTypography.current.bodyMediumRegular,
            color = Neutral70
        )
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
fun ErrorScreen(){
    Column (
        modifier = Modifier
            .fillMaxSize()
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
            color = Neutral100
        )

        Spacer(Modifier.height(8.dp))

        Text(
            textAlign = TextAlign.Center,
            text ="Failed to connect. Please check \nyour internet connection.",
            style = LocalCustomTypography.current.bodyLargeRegular,
            color = Neutral70
        )

        Spacer(Modifier.height(24.dp))

        ButtonComponent(
            enabled = true,
            labelResId = R.string.refresh
        ) { }

    }
}

@Composable
fun LoadingRowState(){
    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
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
            color = Neutral100,
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
                tint = Neutral70,
                contentDescription = "refresh",
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Refresh",
                style = LocalCustomTypography.current.bodySmallMedium,
                color = Neutral70
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorPreview(){
    EmptySearchContent()
}

