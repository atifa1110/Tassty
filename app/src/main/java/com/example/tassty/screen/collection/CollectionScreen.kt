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
import com.example.core.ui.model.CollectionUiModel
import com.example.tassty.component.CollectionAddContent
import com.example.tassty.component.CollectionTopAppBar
import com.example.tassty.component.CollectionVerticalCard
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.Divider32
import com.example.tassty.component.EmptyCollectionContent
import com.example.tassty.component.HeaderTitleScreen
import com.example.tassty.component.HorizontalTitleItemCountSection
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.collectionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CollectionScreen (
    snackHostState : SnackbarHostState,
    onNavigateBack:()-> Unit,
    onNavigateToDetailCollection: (String,String, String) -> Unit,
    viewModel: CollectionViewModel = hiltViewModel()
){
    val collections by viewModel.collectionsState.collectAsStateWithLifecycle()
    val sheetState by viewModel.sheetState.collectAsStateWithLifecycle()

    CollectionContent(
        uiState = collections,
        onNavigateBack = onNavigateBack,
        onNavigateToDetailCollection = onNavigateToDetailCollection,
        onAddClick = {viewModel.onEvent(CollectionEvent.OnShowAddCollectionSheet)},
        snackHostState = snackHostState
    )

    CustomBottomSheet(
        visible = sheetState.isAddCollectionSheet,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        CollectionAddContent (
            collectionName = sheetState.newCollectionName,
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
                    items = uiState.collections,
                    onNavigateToDetailCollection = onNavigateToDetailCollection
                )
            }

        }
    }
}

@Composable
fun CollectionListSection(
    items: ImmutableList<CollectionUiModel>?,
    onNavigateToDetailCollection: (String,String, String) -> Unit
){
    if (items == null) {
        return
    }

    if (items.isEmpty()) {
        EmptyCollectionContent()
    } else {
        HorizontalTitleItemCountSection(
            itemCount = items.size,
            headerText = "Collections",
        ) {
            items(items = items) { data ->
                CollectionVerticalCard(
                    collection = data,
                    onClick = {
                        onNavigateToDetailCollection(data.id, data.title, data.imageUrl)
                    }
                )
            }
        }
    }
}

//@Preview(showBackground = true, name = "Light Mode")
@Composable
fun CollectionLightPreview() {
    val snackHostState = remember { SnackbarHostState() }
    TasstyTheme {
        CollectionContent(
            uiState = CollectionUiState(
                collections = collectionUiModel.toImmutableList()
            ),
            onNavigateToDetailCollection = { _, _, _ -> },
            onAddClick = {},
            onNavigateBack = {},
            snackHostState = snackHostState
        )
    }
}

//@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun CollectionDarkPreview() {
    val snackHostState = remember { SnackbarHostState() }
    TasstyTheme(darkTheme = true){
        CollectionContent(
            uiState = CollectionUiState(
                collections = collectionUiModel.toImmutableList()
            ),
            onNavigateToDetailCollection = { _, _, _ -> },
            onAddClick = {},
            onNavigateBack = {},
            snackHostState = snackHostState
        )
    }
}