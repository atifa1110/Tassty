package com.example.tassty.screen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.Composable
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tassty.R
import com.example.tassty.component.WormIndicator
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import kotlinx.coroutines.launch

@Composable
fun OnBoardingScreen(
    onNavigateToAuth: () -> Unit
) {
    val pages = listOf(OnBoardingPage.First, OnBoardingPage.Second, OnBoardingPage.Third)
    val pagerState = rememberPagerState(initialPage = 0) { pages.size }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White).padding(vertical=24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pagerState,
            verticalAlignment = Alignment.CenterVertically
        ) { position ->
            PagerScreen(onBoardingPage = pages[position])
        }

        WormIndicator(pagerState, pages.size)

        HorizontalDivider()

        if (pagerState.currentPage == pages.lastIndex) {
            TextButton(
                onClick = {
                    // TODO: navigasi ke Home / Login
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(60.dp)
                    .background(
                        color = Orange500,
                        shape = MaterialTheme.shapes.extraLarge
                    ),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White,
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.skip),
                    fontSize = 14.sp,
                    color = Neutral70,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(
                                    page = pages.lastIndex,
                                    animationSpec = tween(
                                        durationMillis = 600,  // lama animasi
                                        easing = FastOutSlowInEasing // efek easing
                                    )
                                )
                            }
                        }
                )

                Row(
                    modifier = Modifier.weight(1f).clickable {
                        if (pagerState.currentPage + 1 < pages.size) {
                            scope.launch {
                                pagerState.animateScrollToPage(page = pagerState.currentPage + 1,
                                    animationSpec = tween(
                                        durationMillis = 600,  // lama animasi
                                        easing = FastOutSlowInEasing // efek easing
                                    ))
                            }
                        }
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.next),
                        fontSize = 14.sp,
                        color = Neutral100,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .weight(1f)
                            .alpha(if (pagerState.currentPage == pages.size - 1) 0f else 1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next",
                        tint = Orange500,
                        modifier = Modifier.size(20.dp)
                    )
                }

            }
        }
    }
}

@Composable
fun PagerScreen(
    onBoardingPage: OnBoardingPage,
) {
    Column (modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(id = onBoardingPage.image),
            contentDescription = "Pager Image"
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column (modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = onBoardingPage.title,
                textAlign = TextAlign.Center,
                fontSize = 40.sp,
                lineHeight = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = onBoardingPage.subtitle,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = Color(0XFF656565)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun OnBoardingPreview() {
    OnBoardingScreen(
        onNavigateToAuth = {}
    )
}

