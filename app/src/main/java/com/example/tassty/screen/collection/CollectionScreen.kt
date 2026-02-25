package com.example.tassty.screen.collection

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CollectionUiModel
import com.example.tassty.collectionUiModel
import com.example.tassty.component.CollectionAddContent
import com.example.tassty.component.CollectionTopAppBar
import com.example.tassty.component.CollectionVerticalCard
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.Divider32
import com.example.tassty.component.EmptyCollectionContent
import com.example.tassty.component.HorizontalTitleItemCountSection
import com.example.tassty.component.ShimmerFoodGridCard
import com.example.tassty.component.ShimmerHorizontalTitleButtonSection
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Orange500

@Composable
fun CollectionScreen (
    snackHostState : SnackbarHostState,
    onNavigateBack:()-> Unit,
    onNavigateToDetailCollection: (String,String, String) -> Unit,
    viewModel: CollectionViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CollectionContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onNavigateToDetailCollection=onNavigateToDetailCollection,
        onAddClick = {viewModel.onEvent(CollectionEvent.OnShowAddCollectionSheet)},
        snackHostState = snackHostState
    )

    CustomBottomSheet(
        visible = uiState.isAddCollectionSheet,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        CollectionAddContent (
            collectionName = uiState.newCollectionName,
            onValueName = {viewModel.onEvent(CollectionEvent.OnNewCollectionNameChange(it))},
            onDismissClick = { viewModel.onEvent(CollectionEvent.OnDismissAddCollectionSheet) },
            onAddCollection = {viewModel.onEvent(CollectionEvent.OnCreateCollection)}
        )
    }
}

@Composable
fun CollectionContent (
    uiState: CollectionUiState,
    onNavigateBack:()-> Unit,
    onNavigateToDetailCollection: (String,String, String) -> Unit,
    onAddClick:()-> Unit,
    snackHostState: SnackbarHostState
){
    Scaffold(
        snackbarHost = { SnackbarHost(snackHostState) },
        containerColor = Neutral10,
        topBar = {
            CollectionTopAppBar(
                onBackClick = onNavigateBack, onAddClick = onAddClick
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    text = buildAnnotatedString {
                        withStyle(style = LocalCustomTypography.current.h2Bold.toSpanStyle().copy(color = Neutral100)) {
                            append("My Collections")
                        }
                        withStyle(style = LocalCustomTypography.current.h2Bold.toSpanStyle().copy(color = Orange500)) {
                            append(".")
                        }
                    }
                )
                Divider32()
            }

            item {
                CollectionListSection(
                    resource = uiState.collections,
                    onNavigateToDetailCollection = onNavigateToDetailCollection
                )
            }
            item {
                Divider32()
            }
        }
    }
}

@Composable
fun CollectionListSection(
    resource: Resource<List<CollectionUiModel>>,
    onNavigateToDetailCollection: (String,String, String) -> Unit
){
    val items = resource.data.orEmpty()
    when {
        resource.isLoading -> {
            ShimmerHorizontalTitleButtonSection {
                items(4) {
                    ShimmerFoodGridCard()
                }
            }
        }

        resource.errorMessage != null || items.isEmpty() -> {
            EmptyCollectionContent()
        }

        else -> {
            HorizontalTitleItemCountSection(
                itemCount = items.size,
                headerText = "Collections",
            ) {
                items(items = items) { data ->
                    CollectionVerticalCard(
                        collection = data,
                        onClick = {
                            onNavigateToDetailCollection(data.id,data.title,data.imageUrl)
                        }
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CollectionScreenPreview() {
    val snackHostState = remember { SnackbarHostState() }
    CollectionContent(
        uiState = CollectionUiState(
            isAddCollectionSheet = false,
            newCollectionName = "",
            collections = Resource(isLoading = false, data = collectionUiModel)
        ),
        onNavigateToDetailCollection = {_,_,_->},
        onAddClick = {},
        onNavigateBack = {},
        snackHostState = snackHostState
    )
}