package com.example.tassty.screen.payment

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CardUserUiModel
import com.example.tassty.R
import com.example.tassty.cardList
import com.example.tassty.component.ErrorListState
import com.example.tassty.component.FoodPriceText
import com.example.tassty.component.HeaderListBlackTitle
import com.example.tassty.component.LoadingRowState
import com.example.tassty.component.PaymentChannelCard
import com.example.tassty.component.PaymentSwipeButton
import com.example.tassty.component.TopBarButton
import com.example.tassty.component.cardUserSelectedVerticalListBlock
import com.example.tassty.paymentChannel
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Orange500
import androidx.core.net.toUri
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.LogoImage
import com.example.tassty.ui.theme.Green100
import com.example.tassty.ui.theme.Green200
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.Neutral70
import kotlinx.coroutines.delay

@Composable
fun PaymentScreen(
    total: String,
    onNavigateToOrderDetail: (String) -> Unit,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showSuccess by remember { mutableStateOf(false) }
    var orderId by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.navigation) {
        viewModel.navigation.collect { event->
            when(event){
                is PaymentEvent.NavigateToOrderDetail -> {
                    orderId = event.id
                    haptic.performHapticFeedback(
                        HapticFeedbackType.LongPress
                    )

                    showSuccess = true
                }
                is PaymentEvent.OnShowError -> {
                    Toast.makeText(context,event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PaymentContent(
            total = total,
            uiState = uiState,
            onCardSelected = viewModel::onCardSelected,
            onChannelSelected = viewModel::onChannelSelected,
            onSwipePayment = viewModel::onSwipePayment
        )

        AnimatedVisibility(
            visible = uiState.isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Neutral100.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Green500)
                    Spacer(Modifier.height(8.dp))
                    Text("Securely processing...", color = Neutral10)
                }
            }
        }
    }

    AnimatedVisibility(
        visible = showSuccess,
        enter = fadeIn(tween(300)),
        exit = fadeOut(tween(300))
    ) {
        PaymentSuccessOverlay(
            onFinished = {
                onNavigateToOrderDetail(orderId)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentContent(
    total: String,
    uiState: PaymentUiState,
    onCardSelected:(String) -> Unit,
    onChannelSelected:(String) -> Unit,
    onSwipePayment:() -> Unit
) {
    Scaffold(
        containerColor = Neutral10,
        bottomBar = {
            Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total Order",
                        style = LocalCustomTypography.current.h5Bold,
                        color = Neutral100
                    )

                    FoodPriceText(price = total, color = Orange500)
                }
                PaymentSwipeButton(
                    isEnabled = uiState.isButtonEnabled,
                    onSwiped = onSwipePayment
                )
            }
        },
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                CenterAlignedTopAppBar(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Neutral10
                    ),
                    title = {
                        Text(
                            text = "Payment",
                            style = LocalCustomTypography.current.h5Bold,
                            color = Neutral100
                        )
                    },
                    navigationIcon = {
                        TopBarButton(
                            icon = R.drawable.arrow_left,
                            boxColor = Neutral20, iconColor = Neutral100
                        ) { }
                    },
                )
                HorizontalDivider()
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            item {
                Spacer(Modifier.height(24.dp))
            }
            cardUserSection(resource = uiState.cardPayment, onCardSelected = onCardSelected)

            uiState.groupedPaymentChannels.forEach { (category, channels) ->
                item {
                    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                    ) {
                        HeaderListBlackTitle(title = category)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                items(channels, key = { it.channelCode}) { channel ->
                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        PaymentChannelCard(
                            channel = channel,
                            onCheckChanged = { onChannelSelected(channel.channelCode) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

fun LazyListScope.cardUserSection(
    resource: Resource<List<CardUserUiModel>>,
    onCardSelected: (String) -> Unit
) {
    val cardItems = resource.data.orEmpty()
    when{
        resource.isLoading -> {
            item { LoadingRowState() }
        }
        resource.errorMessage != null  -> {
            item {
                ErrorListState(
                    title = "Our recommended menu",
                    onRetry = {}
                )
            }
        }
        cardItems.isEmpty() -> {}

        else->{
            cardUserSelectedVerticalListBlock(
                headerText = "Card",
                cards = cardItems,
                onCardSelected = onCardSelected
            )

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

        }
    }
}

@Composable
fun PaymentSuccessOverlay(
    isPreview: Boolean = false,
    onFinished: () -> Unit
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.success_check)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    LaunchedEffect(progress) {
        if (!isPreview && progress == 1f) {
            delay(800)
            onFinished()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Neutral10),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(160.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Payment Successful",
                style = LocalCustomTypography.current.h2Bold,
                color = Neutral100
            )

            Text(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                text = "Your order has been placed successfully and \nnow being prepared by the restaurant.",
                style = LocalCustomTypography.current.bodyMediumRegular,
                color = Neutral70,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
    PaymentContent(
        total = "Rp150.0000",
        uiState = PaymentUiState(
            cardPayment = Resource(data = cardList),
            paymentChannel = Resource(data = paymentChannel),
            selectedCardId = "",
            isButtonEnabled = true,
            isLoading = true
        ),
        onCardSelected = {},
        onChannelSelected = {},
        onSwipePayment = {}
    )
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Neutral100.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Green500)
                    Spacer(Modifier.height(8.dp))
                    Text("Securely processing...", color = Neutral10)
                }
            }
        }
    }
}
