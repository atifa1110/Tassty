package com.example.tassty.screen.card

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CardUserUiModel
import com.example.tassty.R
import com.example.tassty.util.cardList
import com.example.tassty.component.AddTopAppBar
import com.example.tassty.component.Divider32
import com.example.tassty.component.EmptyCardContent
import com.example.tassty.component.ErrorScreen
import com.example.tassty.component.HeaderTitleScreen
import com.example.tassty.component.LoadingRowState
import com.example.tassty.component.ShimmerDebitPaymentCard
import com.example.tassty.component.cardUserVerticalListBlock
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.TasstyTheme
import dagger.hilt.android.internal.Contexts
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CardScreen(
    onNavigateBack:() -> Unit,
    onNavigateToAddCard : () -> Unit,
    viewModel: CardViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CardContent(
        context = context,
        uiState = uiState,
        onAddClick = onNavigateToAddCard,
        onNavigateBack = onNavigateBack,
        onRemoveItemClicked = {},
        onRevealChange = viewModel::onRevealChange
    )
}

@Composable
fun CardContent(
    context: Context,
    uiState: CardUiState,
    onAddClick:()-> Unit,
    onNavigateBack:() -> Unit,
    onRevealChange: (String, Boolean) -> Unit,
    onRemoveItemClicked: (String) -> Unit
) {
    Scaffold(
        containerColor = LocalCustomColors.current.background,
        topBar = {
            AddTopAppBar(
                onAddClick = onAddClick,
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                HeaderTitleScreen(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 24.dp,
                            end = 24.dp, top = 24.dp
                        ),
                    title = stringResource(R.string.my_payment_methods)
                )
                Divider32()
            }

            cardSection(
                context = context,
                resource = uiState.cardPayment,
                onRevealChange = onRevealChange,
                onRemoveItemClicked = onRemoveItemClicked
            )
        }
    }
}

fun LazyListScope.cardSection(
    context: Context,
    resource: Resource<ImmutableList<CardUserUiModel>>,
    onRevealChange: (String, Boolean) -> Unit,
    onRemoveItemClicked: (String) -> Unit
) {
    val cardItems = resource.data.orEmpty()
    when{
        resource.data == null ->{
        }

        resource.isLoading -> {
            items(3){
                Column(Modifier.padding(horizontal = 24.dp)) {
                    ShimmerDebitPaymentCard()
                    Spacer(Modifier.height(12.dp))
                }
            }
        }

        resource.errorMessage != null -> {
            item {
                ErrorScreen()
            }
        }

        cardItems.isEmpty() -> {
            item {
                EmptyCardContent()
            }
        }

        else->{
            cardUserVerticalListBlock(
                headerText = context.getString(R.string.card_payment),
                cards = cardItems,
                onRevealChange = onRevealChange,
                onRemoveItemClicked = onRemoveItemClicked
            )
        }
    }
}

//@Preview(showBackground = true, name = "Light Mode")
@Composable
fun CardLightPreview() {
    TasstyTheme{
        CardContent(
            uiState = CardUiState(
                cardPayment = Resource(data = cardList.toImmutableList())
            ),
            onNavigateBack = {},
            onAddClick = {},
            onRevealChange = {_,_->},
            onRemoveItemClicked = {},
            context = LocalContext.current
        )
    }
}


//@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun CardDarkPreview(){
    TasstyTheme (darkTheme = true){
        CardContent(
            uiState = CardUiState(
                cardPayment = Resource(data = cardList.toImmutableList())
            ),
            onNavigateBack = {},
            onAddClick = {},
            onRevealChange = {_,_->},
            onRemoveItemClicked = {},
            context = LocalContext.current
        )
    }
}