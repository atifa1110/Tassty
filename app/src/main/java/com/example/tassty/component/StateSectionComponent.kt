package com.example.tassty.component

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.ui.theme.LocalCustomTypography
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
fun EmptyCartContent(){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(R.drawable.empty_cart),
            contentDescription = "Empty Cart",
        )

        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.your_cart_is_empty),
                style = LocalCustomTypography.current.h2Bold,
                color = Neutral100
            )
            Text(
                text = stringResource(R.string.exclamation),
                style = LocalCustomTypography.current.h2Bold,
                color = Orange500
            )
        }

        Spacer(Modifier.height(10.dp))

        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.start_exploring_your_favorite),
            style = LocalCustomTypography.current.bodyMediumRegular,
            color = Neutral70
        )

        Spacer(Modifier.height(10.dp))

        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Find recommended restaurants",
                style = LocalCustomTypography.current.bodyMediumMedium,
                color = Orange500
            )
            Spacer(Modifier.width(4.dp))
            Icon(
                painter = painterResource(R.drawable.arrow_left_up),
                contentDescription = "Arrow To Restaurant",
                tint = Orange500,
                modifier = Modifier.size(16.dp)
            )
        }
    }
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
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(R.drawable.sorry),
            contentDescription = "Sorry",
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