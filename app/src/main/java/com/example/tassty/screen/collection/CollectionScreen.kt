package com.example.tassty.screen.collection

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CollectionUiModel
import com.example.tassty.util.collectionUiModel
import com.example.tassty.component.CollectionAddContent
import com.example.tassty.component.CollectionTopAppBar
import com.example.tassty.component.CollectionVerticalCard
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.Divider32
import com.example.tassty.component.EmptyCollectionContent
import com.example.tassty.component.HeaderTitleScreen
import com.example.tassty.component.HorizontalTitleItemCountSection
import com.example.tassty.component.ShimmerFoodGridCard
import com.example.tassty.component.ShimmerHorizontalTitleButtonSection
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.TasstyTheme

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
        containerColor = LocalCustomColors.current.background,
        topBar = {
            CollectionTopAppBar(
                onBackClick = onNavigateBack, onAddClick = onAddClick
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(top = 24.dp, bottom = 32.dp)
        ) {
            item {
                HeaderTitleScreen(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    title = "My collections."
                )
                Divider32()
            }

            item {
                CollectionListSection(
                    resource = uiState.collections,
                    onNavigateToDetailCollection = onNavigateToDetailCollection
                )
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

//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun CollectionLightPreview() {
//    val snackHostState = remember { SnackbarHostState() }
//    TasstyTheme {
//        CollectionContent(
//            uiState = CollectionUiState(
//                isAddCollectionSheet = false,
//                newCollectionName = "",
//                collections = Resource(isLoading = false, data = collectionUiModel)
//            ),
//            onNavigateToDetailCollection = { _, _, _ -> },
//            onAddClick = {},
//            onNavigateBack = {},
//            snackHostState = snackHostState
//        )
//    }
//}
//
//@Preview(showBackground = true, name = "Dark Mode")
//@Composable
//fun CollectionDarkPreview() {
//    val snackHostState = remember { SnackbarHostState() }
//    TasstyTheme(darkTheme = true){
//        CollectionContent(
//            uiState = CollectionUiState(
//                isAddCollectionSheet = false,
//                newCollectionName = "",
//                collections = Resource(isLoading = false, data = collectionUiModel)
//            ),
//            onNavigateToDetailCollection = { _, _, _ -> },
//            onAddClick = {},
//            onNavigateBack = {},
//            snackHostState = snackHostState
//        )
//    }
//}