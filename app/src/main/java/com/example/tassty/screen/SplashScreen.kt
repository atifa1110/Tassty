package com.example.tassty.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tassty.R
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var showLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(3000)
        showLoading = false
        onFinished()
    }

    SplashGradientBackground {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_white),
                contentDescription = "App Logo",
                modifier = Modifier.size(114.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Tasstty!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(165.dp))

            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                strokeWidth = 4.dp,
                color = Color.White,
            )
        }
    }
}

@Composable
fun SplashGradientBackground(content: @Composable () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.0f to Color(0xFFFFCF24), // 0% → kuning
                        0.84f to Color(0xFFF07C2A) // 84% → oranye
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                )
            )
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    SplashScreen(){

    }
}