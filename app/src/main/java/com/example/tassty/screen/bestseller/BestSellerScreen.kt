package com.example.tassty.screen.bestseller

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.MenuUiModel
import com.example.tassty.R
import com.example.tassty.component.CategoryTopAppBar
import com.example.tassty.component.CollectionAddContent
import com.example.tassty.component.CollectionContent
import com.example.tassty.component.CommonImage
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.CustomTwoColorText
import com.example.tassty.component.EmptyFavoriteContent
import com.example.tassty.component.ErrorScreen
import com.example.tassty.component.FoodWideListCard
import com.example.tassty.component.ImageIcon
import com.example.tassty.component.SearchBar
import com.example.tassty.component.ShimmerFoodWideListCard
import com.example.tassty.component.shimmerLoadingAnimation
import com.example.tassty.util.menusItem
import com.example.tassty.screen.detailrestaurant.ShoppingCartBottomBar
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.collectionUiModel

@Composable
fun BestSellerScreen(
    onNavigateBack:() -> Unit,
    onNavigateToDetailMenu:(String) -> Unit,
    viewModel: BestSellerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { event->
            when(event){
                is BestSellerUiEffect.ShowMessage -> {
                    Toast.makeText(context,event.message,
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    BestSellerContent(
        uiState = uiState,
        onFavoriteClick = viewModel::onMenuFavorite,
        onNavigateBack = onNavigateBack,
        onNavigateToDetailMenu = onNavigateToDetailMenu
    )

    CustomBottomSheet(
        visible = uiState.isCollectionSheetVisible,
        onDismiss = { viewModel.onShowCollectionSheet() }
    ) {
        CollectionContent(
            items = uiState.collections,
            onCollectionSelected = {id, check -> viewModel.onCollectionCheckChange(id)},
            onSaveCollectionClick = {viewModel.onSaveToCollection()},
            onAddCollectionClick = { viewModel.onShowAddCollectionSheet() }
        )
    }

    CustomBottomSheet(
        visible = uiState.isAddCollectionSheet,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        CollectionAddContent (
            collectionName = uiState.newCollectionName,
            onValueName = viewModel::onNewCollectionNameChanged,
            onDismissClick = viewModel::onDismissAddCollectionSheet,
            onAddCollection = viewModel::onCreateNewCollection
        )
    }
}

@Composable
fun BestSellerContent(
    uiState: BestSellerUiState,
    onFavoriteClick:(MenuUiModel) -> Unit,
    onNavigateBack:() -> Unit,
    onNavigateToDetailMenu:(String) -> Unit
) {
    val resource = uiState.menus
    val isLoading = resource.isLoading
    val menus = resource.data.orEmpty()

    val scrollState = rememberLazyListState()
    val isScrolled by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0 ||
                    scrollState.firstVisibleItemScrollOffset > 100
        }
    }

    val iconBgColor by animateColorAsState(
        targetValue = if (isScrolled) LocalCustomColors.current.cardBackground else LocalCustomColors.current.topBarBackgroundColor,
        animationSpec = tween(300),
        label = "iconBackground"
    )

    val appBarAlpha by animateFloatAsState(
        targetValue = if (isScrolled) 1f else 0f,
        animationSpec = tween(300),
        label = "alpha"
    )

    Scaffold (
        containerColor = LocalCustomColors.current.background,
        bottomBar = {
            if (uiState.totalItems > 0){
                ShoppingCartBottomBar(
                    itemCount = uiState.totalItems,
                    totalPrice = uiState.totalPrice,
                    onCartClick = {}
                )
            }
        }
    ) { padding ->
        BoxWithConstraints(
            modifier = Modifier.padding(padding).fillMaxSize()
                .background(Color.Transparent)
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                if(isLoading){
                    loadingSection()
                } else{
                    item(key = "header_content") {
                        HeaderContent(
                            fixedHeight = maxHeight,
                            imageUrl = uiState.imageUrl,
                            query = uiState.query
                        )
                    }
                    if(resource.errorMessage!=null){
                        item(key = "error_content"){
                            ErrorScreen()
                        }
                    }else if(menus.isEmpty()){
                        item(key = "empty_content") {
                            Spacer(Modifier.height(24.dp))
                            EmptyFavoriteContent(
                                onClick = {}
                            )
                        }
                    } else {
                        item(key = "spacer") { Spacer(Modifier.height(24.dp)) }
                        items(items = menus, key = { it.id }) { item ->
                            Column(Modifier.padding(horizontal = 24.dp)) {
                                FoodWideListCard(
                                    menu = item,
                                    onFavoriteClick = { onFavoriteClick(item) },
                                    onAddToCart = {
                                        onNavigateToDetailMenu(item.id)
                                    }
                                )
                                Spacer(Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }

            CategoryTopAppBar(
                iconBackground = iconBgColor,
                onFilterClick = {},
                onBackClick = onNavigateBack,
                modifier = Modifier.align(Alignment.TopCenter)
                    .background(
                        LocalCustomColors.current.background.copy(alpha = appBarAlpha)
                    )
            )
        }
    }
}

fun LazyListScope.loadingSection(){
    item {
        Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .shimmerLoadingAnimation()
        )
        Spacer(Modifier.height(32.dp))
    }

    items(count = 4) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
            ShimmerFoodWideListCard()
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun HeaderContent(
    modifier: Modifier = Modifier,
    fixedHeight: Dp,
    query: String,
    imageUrl: String,
) {
    val imageHeight = fixedHeight * 0.4f
    val searchBarHeight = 56.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(imageHeight + (searchBarHeight / 2))
    ) {
        CommonImage(
            imageUrl = imageUrl,
            name = "category header image",
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
                .align(Alignment.TopCenter)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight * 0.45f)
                .align(Alignment.BottomCenter)
                .padding(bottom = searchBarHeight / 2)
                .background(
                    color = LocalCustomColors.current.modalBackgroundFrame
                )
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            BestSellerNameHeader()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(searchBarHeight)
                .align(Alignment.BottomCenter)
        ) {
            SearchBar(
                value = query,
                onValueChange = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun BestSellerNameHeader(){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CustomTwoColorText(
                fullText = "Our best seller!",
                highlightText = "!",
                textColor = LocalCustomColors.current.headerText,
                normalStyle = LocalCustomTypography.current.h3Bold,
                textAlign = TextAlign.Start
            )
            Text(
                text = "Top-selling delicacies you can't miss!",
                style = LocalCustomTypography.current.bodySmallMedium,
                color = LocalCustomColors.current.text
            )
        }

        ImageIcon(
            image = R.drawable.best_seller,
            contentDescription = "best seller icon",
            modifier = Modifier.size(44.dp)
        )
    }
}
//
//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun BestSellerLightPreview(){
//    TasstyTheme {
//        BestSellerContent(
//            uiState = BestSellerUiState(
//                menus = Resource(data = menusItem, isLoading = false, errorMessage = null),
//                totalItems = 0,
//                totalPrice = 120000
//            ),
//            onFavoriteClick = {}
//        )
//
//        CustomBottomSheet(
//            visible = true,
//            onDismiss = {}
//        ) {
//            CollectionContent(
//                resource = Resource(collectionUiModel),
//                onCollectionSelected = {_,_->},
//                onSaveCollectionClick = {},
//                onAddCollectionClick = { }
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true, name = "Dark Mode")
//@Composable
//fun BestSellerDarkPreview(){
//    TasstyTheme(darkTheme = true) {
//        BestSellerContent(
//            uiState = BestSellerUiState(
//                menus = Resource(data = menusItem),
//                totalItems = 0,
//                totalPrice = 120000
//            ),
//            onFavoriteClick = {}
//        )
//
//        CustomBottomSheet(
//            visible = true,
//            onDismiss = {}
//        ) {
//            CollectionContent(
//                resource = Resource(collectionUiModel),
//                onCollectionSelected = {_,_->},
//                onSaveCollectionClick = {},
//                onAddCollectionClick = { }
//            )
//        }
//    }
//}