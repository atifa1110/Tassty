package com.example.tassty.screen.detailcollection

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.ui.model.CollectionRestaurantWithMenuUiModel
import com.example.tassty.util.collectionRestaurantMenuUiModel
import com.example.tassty.component.CollectionDeleteContent
import com.example.tassty.component.CollectionDetailTopAppBar
import com.example.tassty.component.CollectionEditContent
import com.example.tassty.component.CommonImage
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.StatusItemImage
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.component.collectionList
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.TasstyTheme

@Composable
fun CollectionDetailScreen(
    onNavigateBack:()-> Unit,
    onNavigateToDetailRest: (String) -> Unit,
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
        onNavigateToDetailRest = onNavigateToDetailRest,
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
    onNavigateToDetailRest: (String) -> Unit,
    snackHostState: SnackbarHostState
){
    val scrollState = rememberLazyListState()
    val isScrolled by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0 ||
                    scrollState.firstVisibleItemScrollOffset > 100
        }
    }

    val iconBackground by animateColorAsState(
        targetValue = if (isScrolled) LocalCustomColors.current.cardBackground else LocalCustomColors.current.topBarBackgroundColor,
        animationSpec = tween(300),
        label = "iconBackground"
    )

    val appBarAlpha by animateFloatAsState(
        targetValue = if (isScrolled) 1f else 0f,
        animationSpec = tween(300),
        label = "alpha"
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackHostState) },
        containerColor = LocalCustomColors.current.background
    ) { padding ->
        BoxWithConstraints(modifier = Modifier.padding(padding).fillMaxSize()) {
            val screenHeight = maxHeight
            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                item{
                    HeaderContent(
                        fixedHeight = screenHeight,
                        imageUrl = image,
                        name = name
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                collectionList(
                    items = collections,
                    onNavigateToDetailRest = onNavigateToDetailRest
                )
            }

            CollectionDetailTopAppBar(
                iconBackground = iconBackground,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .background(
                        LocalCustomColors.current.background.copy(alpha = appBarAlpha)
                    ),
                onBackClick = onNavigateBack,
                onEditClick = onEditClick,
                onRemoveClick = onDeleteClick
            )
        }
    }
}

@Composable
fun HeaderContent(
    fixedHeight: Dp,
    imageUrl: String,
    name: String
){
    val imageHeight = fixedHeight * 0.3f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        CommonImage(
            imageUrl = imageUrl,
            name = "detail header image",
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        )

        Column(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomEnd)
                .background(LocalCustomColors.current.modalBackgroundFrame),
            verticalArrangement = Arrangement.Center
        ) {
            Column (Modifier.fillMaxWidth().padding(24.dp)){
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = name,
                    style = LocalCustomTypography.current.h3Bold,
                    color = LocalCustomColors.current.headerText
                )
            }
        }
    }
}

//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun CollectionDetailLightPreview() {
//    val snackHostState = remember { SnackbarHostState() }
//    TasstyTheme {
//        CollectionDetailContent(
//            name = "Favorite Salad",
//            image = "",
//            collections = collectionRestaurantMenuUiModel,
//            onEditClick = {},
//            onDeleteClick = {},
//            onNavigateBack = {},
//            onNavigateToDetailRest = {},
//            snackHostState = snackHostState
//        )
//    }
//}
//
//@Preview(showBackground = true, name = "Dark Mode")
//@Composable
//fun CollectionDetailDarkPreview() {
//    val snackHostState = remember { SnackbarHostState() }
//    TasstyTheme(darkTheme = true) {
//        CollectionDetailContent(
//            name = "Favorite Salad",
//            image = "",
//            collections = collectionRestaurantMenuUiModel,
//            onEditClick = {},
//            onDeleteClick = {},
//            onNavigateBack = {},
//            onNavigateToDetailRest = {},
//            snackHostState = snackHostState
//        )
//    }
//}