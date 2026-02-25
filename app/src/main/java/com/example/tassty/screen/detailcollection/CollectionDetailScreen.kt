package com.example.tassty.screen.detailcollection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.core.ui.model.CollectionRestaurantWithMenuUiModel
import com.example.tassty.collectionRestaurantMenuUiModel
import com.example.tassty.component.CollectionDeleteContent
import com.example.tassty.component.CollectionDetailTopAppBar
import com.example.tassty.component.CollectionEditContent
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.component.collectionList
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20

@Composable
fun CollectionDetailScreen(
    onNavigateBack:()-> Unit,
    onDeleteSuccess: (String) -> Unit,
    viewModel: CollectionDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val data by viewModel.collectionMenusUi.collectAsStateWithLifecycle()
    val snackHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.NavigateBackWithResult -> {
                    onDeleteSuccess(event.message)
                    onNavigateBack()
                }
                is UiEvent.ShowError -> {
                    snackHostState.showSnackbar(event.message)
                }
            }
        }
    }

    CollectionDetailContent(
        name = uiState.collectionName,
        image = uiState.collectionImage,
        collections = data,
        onEditClick = {viewModel.onEvent(CollectionDetailEvent.OnEditSheetClick)},
        onDeleteClick = {viewModel.onEvent(CollectionDetailEvent.OnDeleteSheetClick)},
        onNavigateBack = onNavigateBack,
        snackHostState = snackHostState
    )

    CustomBottomSheet(
        visible = uiState.isEditCollection,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        CollectionEditContent (
            collectionName = uiState.nameInput,
            onValueName = { viewModel.onEvent(CollectionDetailEvent.OnNewCollectionNameChange(it))},
            onDismissClick = {viewModel.onEvent(CollectionDetailEvent.OnDismissAddCollectionSheet)},
            onUpdateCollection = { viewModel.onEvent(CollectionDetailEvent.OnUpdateCollection)}
        )
    }

    CustomBottomSheet(
        visible = uiState.isDeleteCollection,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        CollectionDeleteContent(
            collectionImage = uiState.collectionImage,
            onDismissClick = {viewModel.onEvent(CollectionDetailEvent.OnDismissDeleteCollectionSheet)},
            onDeleteCollection = {viewModel.onEvent(CollectionDetailEvent.OnDeleteClick)}
        )
    }
}
@Composable
fun CollectionDetailContent(
    name: String,
    image: String,
    collections: List<CollectionRestaurantWithMenuUiModel>,
    onEditClick: () -> Unit,
    onDeleteClick:() -> Unit,
    onNavigateBack:()-> Unit,
    snackHostState: SnackbarHostState
){
    Scaffold(
        snackbarHost = { SnackbarHost(snackHostState) },
        containerColor = Neutral10,
        topBar = {
            CollectionDetailTopAppBar(
                onBackClick = onNavigateBack, onEditClick = onEditClick, onRemoveClick = onDeleteClick
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {
            item{
                Column(
                    modifier = Modifier.fillMaxWidth().height(92.dp).background(Neutral20),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                        text = name,
                        style = LocalCustomTypography.current.h3Bold,
                        color = Neutral100
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            collectionList(
                items = collections
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CollectionDetailScreenPreview() {
    val snackHostState = remember { SnackbarHostState() }
    CollectionDetailContent(
        name = "Favorite Salad",
        image = "",
        collections = collectionRestaurantMenuUiModel,
        onEditClick = {},
        onDeleteClick = {},
        onNavigateBack = {},
        snackHostState = snackHostState
    )
}