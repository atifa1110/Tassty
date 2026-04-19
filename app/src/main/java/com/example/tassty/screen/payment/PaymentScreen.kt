package com.example.tassty.screen.payment

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CardUserUiModel
import com.example.tassty.R
import com.example.tassty.component.ErrorListState
import com.example.tassty.component.FoodPriceText
import com.example.tassty.component.HeaderListBlackTitle
import com.example.tassty.component.PaymentChannelCard
import com.example.tassty.component.PaymentSwipeButton
import com.example.tassty.component.TopBarButton
import com.example.tassty.component.cardUserSelectedVerticalListBlock
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Orange500
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.tassty.component.ShimmerDebitPaymentCard
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.OrderPreviewData
import com.example.tassty.util.UserData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay

@Composable
fun PaymentScreen(
    total: String,
    onNavigateToOrderProcess: (String) -> Unit,
    onNavigateBack:() -> Unit,
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
            context = context,
            uiState = uiState,
            onCardSelected = viewModel::onCardSelected,
            onChannelSelected = viewModel::onChannelSelected,
            onSwipePayment = viewModel::onSwipePayment,
            onNavigateBack = onNavigateBack
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
                onNavigateToOrderProcess(orderId)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentContent(
    total: String,
    context: Context,
    uiState: PaymentUiState,
    onCardSelected:(String) -> Unit,
    onChannelSelected:(String) -> Unit,
    onSwipePayment:() -> Unit,
    onNavigateBack:() -> Unit
) {
    Scaffold(
        containerColor = LocalCustomColors.current.background,
        bottomBar = {
            Column(Modifier
                .fillMaxWidth()
                .background(LocalCustomColors.current.modalBackgroundFrame)
                .padding(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.total_order),
                        style = LocalCustomTypography.current.h5Bold,
                        color = LocalCustomColors.current.headerText
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
            Column(modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()) {
                CenterAlignedTopAppBar(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = LocalCustomColors.current.background
                    ),
                    title = {
                        Text(
                            text = stringResource(R.string.payment),
                            style = LocalCustomTypography.current.h5Bold,
                            color = LocalCustomColors.current.headerText
                        )
                    },
                    navigationIcon = {
                        TopBarButton(
                            icon = R.drawable.arrow_left,
                            boxColor = LocalCustomColors.current.topBarBackgroundColor,
                            iconColor = LocalCustomColors.current.iconFocused,
                            onClick = onNavigateBack
                        )
                    },
                )
                HorizontalDivider(color = LocalCustomColors.current.divider)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            cardUserSection(
                context = context,
                resource = uiState.cardPayment,
                onCardSelected = onCardSelected
            )

            uiState.groupedPaymentChannels.forEach { (category, channels) ->
                item {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
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
    context: Context,
    resource: Resource<ImmutableList<CardUserUiModel>>,
    onCardSelected: (String) -> Unit
) {
    val cardItems = resource.data.orEmpty()
    when{
        resource.isLoading -> {
            items(2){
                Column(Modifier.padding(horizontal = 24.dp)) {
                    ShimmerDebitPaymentCard()
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
        resource.errorMessage != null  -> {
            item {
                ErrorListState(
                    title = stringResource(R.string.card),
                    onRetry = {}
                )
            }
        }

        cardItems.isEmpty() -> {}

        else->{
            cardUserSelectedVerticalListBlock(
                headerText = context.getString(R.string.card),
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
            .background(LocalCustomColors.current.background),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(160.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.payment_successful),
                style = LocalCustomTypography.current.h2Bold,
                color = LocalCustomColors.current.headerText
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                text = stringResource(R.string.your_order_has_been_placed_successfully_and_now_being_prepared_by_the_restaurant),
                style = LocalCustomTypography.current.bodyMediumRegular,
                color = LocalCustomColors.current.text,
                textAlign = TextAlign.Center
            )
        }
    }
}

//@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun PaymentDarkPreview() {
    TasstyTheme(darkTheme = true){
        PaymentContent(
            total = "Rp150.0000",
            uiState = PaymentUiState(
                cardPayment = Resource(data = UserData.cardList),
                paymentChannel = Resource(data = OrderPreviewData.paymentChannels),
                selectedCardId = "",
                isButtonEnabled = true,
                isLoading = true
            ),
            onCardSelected = {},
            onChannelSelected = {},
            onSwipePayment = {},
            onNavigateBack = {},
            context = LocalContext.current
        )
    }
}