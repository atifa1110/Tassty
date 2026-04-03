package com.example.tassty.screen.setupcompleted

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.tassty.component.HeaderTitleScreen
import com.example.tassty.component.LogoTopAppBar
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.TasstyTheme

@Composable
fun SetUpCompletedScreen (
    onNavigateToHome: () -> Unit,
    viewModel: SetUpCompleteViewModel = hiltViewModel()
){
    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when(event){
                SetUpCompleteEvent.NavigateToHome -> onNavigateToHome
            }
        }
    }
    SetUpCompletedContent (
        onExploreButtonClick = viewModel::updateLogin
    )
}
@Composable
fun SetUpCompletedContent (
    onExploreButtonClick: () -> Unit
){
    Scaffold(
        containerColor = LocalCustomColors.current.background,
        topBar = {
            LogoTopAppBar()
        },
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(color = LocalCustomColors.current.divider)
                Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp)) {
                    ButtonComponent(
                        modifier= Modifier.fillMaxWidth(),
                        enabled = true,
                        labelResId = R.string.explore_home,
                        onClick = onExploreButtonClick
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.friends),
                contentDescription = "Pager Image"
            )
            Spacer(Modifier.height(24.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.setup_account_completed),
                    style = LocalCustomTypography.current.h1Bold,
                    color = LocalCustomColors.current.headerText,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(R.string.more_address_from_profile_menu),
                    style = LocalCustomTypography.current.bodyMediumRegular,
                    color = LocalCustomColors.current.text,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun SetupCompletedLightPreview() {
//    TasstyTheme(darkTheme = false) {
//        SetUpCompletedContent { }
//    }
//}
//
//@Preview(showBackground = true, name = "Dark Mode")
//@Composable
//fun SetupCompletedDarkPreview() {
//    TasstyTheme(darkTheme = true) {
//        SetUpCompletedContent { }
//    }
//}