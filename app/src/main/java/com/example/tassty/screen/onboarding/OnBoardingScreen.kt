package com.example.tassty.screen.onboarding

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tassty.R
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.CustomTwoColorText
import com.example.tassty.component.WormIndicator
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.TasstyTheme
import kotlinx.coroutines.launch

@Composable
fun OnBoardingScreen(
    onNavigateToAuth: () -> Unit,
    viewModel: OnBoardingViewModel = hiltViewModel()
) {
    val pages = listOf(OnBoardingPage.First, OnBoardingPage.Second, OnBoardingPage.Third)
    val pagerState = rememberPagerState(initialPage = 0) { pages.size }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pagerState,
            verticalAlignment = Alignment.CenterVertically
        ) { position ->
            PagerScreen(onBoardingPage = pages[position])
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            WormIndicator(pagerState, pages.size)

            HorizontalDivider(
                color = LocalCustomColors.current.text.copy(alpha = 0.1f)
            )

            OnBoardingNavigation(
                isLastPage = pagerState.currentPage == pages.lastIndex,
                onNextClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(
                            page = pagerState.currentPage + 1,
                            animationSpec = tween(600, easing = FastOutSlowInEasing)
                        )
                    }
                },
                onSkipClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(
                            page = pages.lastIndex,
                            animationSpec = tween(600, easing = FastOutSlowInEasing)
                        )
                    }
                },
                onGetStartedClick = {
                    viewModel.onGetStartClick()
                    onNavigateToAuth()
                }
            )
        }
    }
}

@Composable
private fun OnBoardingNavigation(
    isLastPage: Boolean,
    onNextClick: () -> Unit,
    onSkipClick: () -> Unit,
    onGetStartedClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLastPage) {
            ButtonComponent(
                modifier = Modifier.fillMaxWidth(),
                labelResId = R.string.get_started,
                onClick = onGetStartedClick
            )
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = stringResource(id = R.string.skip),
                    style = LocalCustomTypography.current.bodyMediumMedium,
                    color = LocalCustomColors.current.text,
                    modifier = Modifier.clickable(onClick = onSkipClick)
                )

                Row(
                    modifier = Modifier.clickable(onClick = onNextClick),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.next),
                        style = LocalCustomTypography.current.bodyMediumSemiBold,
                        color = LocalCustomColors.current.headerText
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Orange500,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PagerScreen(onBoardingPage: OnBoardingPage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = onBoardingPage.image),
            contentDescription = null,
            modifier = Modifier.size(280.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        CustomTwoColorText(
            fullText = stringResource(onBoardingPage.title),
            highlightText = ".",
            textColor = LocalCustomColors.current.headerText,
            normalStyle = LocalCustomTypography.current.h1Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(onBoardingPage.subtitle),
            style = LocalCustomTypography.current.bodyMediumRegular,
            color = LocalCustomColors.current.text,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}

//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun OnBoardingLightPreview() {
//    TasstyTheme {
//        OnBoardingScreen(
//            onNavigateToAuth = {}
//        )
//    }
//}
//
//@Preview(showBackground = true, name = "Dark Mode")
//@Composable
//fun OnBoardingDarkPreview() {
//    TasstyTheme(darkTheme = true) {
//        OnBoardingScreen(
//            onNavigateToAuth = {}
//        )
//    }
//}
