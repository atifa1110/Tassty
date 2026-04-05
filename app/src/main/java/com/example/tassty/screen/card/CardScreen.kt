package com.example.tassty.screen.card

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CardUserUiModel
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

@Composable
fun CardScreen(
    onNavigateBack:() -> Unit,
    onNavigateToAddCard : () -> Unit,
    viewModel: CardViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CardContent(
        uiState = uiState,
        onAddClick = onNavigateToAddCard,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun CardContent(
    uiState: CardUiState,
    onAddClick:()-> Unit,
    onNavigateBack:() -> Unit
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
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                HeaderTitleScreen(
                    modifier = Modifier.fillMaxWidth().padding(start = 24.dp,
                        end = 24.dp, top = 24.dp
                    ),
                    title = "My payment methods."
                )
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
        resource.isLoading || resource.data != null-> {
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
                headerText = "Card",
                cards = cardItems
            )
        }
    }
}

//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun CardLightPreview() {
//    TasstyTheme{
//        CardContent(
//            uiState = CardUiState(
//                cardPayment = Resource(data = cardList)
//            ),
//            onNavigateBack = {},
//            onAddClick = {}
//        )
//    }
//}
//
//
//@Preview(showBackground = true, name = "Dark Mode")
//@Composable
//fun CardDarkPreview(){
//    TasstyTheme (darkTheme = true){
//        CardContent(
//            uiState = CardUiState(
//                cardPayment = Resource(data = cardList)
//            ),
//            onNavigateBack = {},
//            onAddClick = {}
//        )
//    }
//}