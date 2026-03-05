package com.example.tassty.screen.card

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CardUserUiModel
import com.example.tassty.cardList
import com.example.tassty.component.AddTopAppBar
import com.example.tassty.component.Divider32
import com.example.tassty.component.ErrorListState
import com.example.tassty.component.HeaderTitleScreen
import com.example.tassty.component.LoadingRowState
import com.example.tassty.component.cardUserSelectedVerticalListBlock
import com.example.tassty.component.cardUserVerticalListBlock
import com.example.tassty.screen.payment.cardUserSection
import com.example.tassty.ui.theme.Neutral10

@Composable
fun CardScreen(
    onNavigateToAddCard : () -> Unit,
    viewModel: CardViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CardContent(
        uiState = uiState,
        onAddClick = onNavigateToAddCard
    )
}

@Composable
fun CardContent(
    uiState: CardUiState,
    onAddClick:()-> Unit
) {
    Scaffold(
        containerColor = Neutral10,
        topBar = {
            AddTopAppBar(
                onAddClick = onAddClick,
                onBackClick = {}
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            item {
                HeaderTitleScreen(title = "My payment methods")
                Divider32()
            }

            cardSection(resource = uiState.cardPayment)
        }
    }
}

fun LazyListScope.cardSection(
    resource: Resource<List<CardUserUiModel>>,
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
            cardUserVerticalListBlock(
                headerText = "Card",
                cards = cardItems
            )

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardContentPreview(){
    CardContent(
        uiState = CardUiState(cardPayment = Resource(data = cardList))
    ) { }
}