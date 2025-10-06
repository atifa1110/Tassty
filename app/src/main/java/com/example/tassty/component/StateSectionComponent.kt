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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500


@Composable
fun EmptyRestaurant(){
    Column (
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(R.drawable.empty_search),
            contentDescription = "Empty Search Restaurant",
        )

        Spacer(Modifier.height(12.dp))

        Text(
            textAlign = TextAlign.Center,
            text = "We couldn’t find \nthis menu.",
            style = LocalCustomTypography.current.h2Bold,
            color = Neutral100
        )

        Spacer(Modifier.height(10.dp))

        Text(
            textAlign = TextAlign.Center,
            text = "Try a different search keyword or look for your \nfavorite menu at other restaurants.",
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
        modifier = Modifier.fillMaxSize().padding(24.dp),
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
            modifier = Modifier.fillMaxWidth().clickable(onClick = onRetry),
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