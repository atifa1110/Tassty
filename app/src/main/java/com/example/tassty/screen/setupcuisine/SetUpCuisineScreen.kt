package com.example.tassty.screen.setupcuisine

import android.widget.Toast
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tassty.R
import com.example.tassty.component.SetupTopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.ui.model.CategoryUiModel
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.CategoryFoundHeader
import com.example.tassty.component.Divider32
import com.example.tassty.component.EmptySearchContent
import com.example.tassty.component.ErrorScreen
import com.example.tassty.component.FoodCategoryCard
import com.example.tassty.component.HeaderTitleSubtitleScreen
import com.example.tassty.component.LoadingRowState
import com.example.tassty.component.LoadingScreen
import com.example.tassty.component.SearchBar
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.RestaurantPreviewData

@Composable
fun SetupCuisineRoute(
    onNavigateToSetUpLocation: (List<String>) -> Unit,
    onSkipClick: () -> Unit,
    onBackButtonClick: () -> Unit,
    viewModel: SetupCuisineViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when(event){
                is SetupCuisineEvent.ShowError -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is SetupCuisineEvent.NavigateToSetUpLocation -> {
                    onNavigateToSetUpLocation(event.cuisines)
                }
            }
        }
    }

    SetupCuisineScreen(
        uiState = uiState,
        onSearchText = viewModel::onSearchTextChanged,
        onSelectedCategory = viewModel::toggleCategorySelection,
        onNextClick = viewModel::onNextClick,
        onSkipClick = onSkipClick,
        onBackButtonClick = onBackButtonClick
    )
}

@Composable
fun SetupCuisineScreen(
    uiState: SetupCuisineUiState,
    onSearchText : (String) -> Unit,
    onSelectedCategory: (String) -> Unit,
    onNextClick: () -> Unit,
    onSkipClick: () -> Unit,
    onBackButtonClick: () -> Unit
) {
    Scaffold(
        containerColor = LocalCustomColors.current.background,
        topBar = {
            SetupTopAppBar(
                currentStep = 1,
                totalStep = 2,
                onBackClick = onBackButtonClick,
                onSkipClick = onSkipClick
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding().background(LocalCustomColors.current.modalBackgroundFrame)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ButtonComponent(
                    modifier = Modifier.width(220.dp),
                    enabled = uiState.selectedCategoryIds.isNotEmpty(),
                    labelResId = R.string.next,
                    onClick = onNextClick
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentPadding = PaddingValues(top = 24.dp, bottom = 32.dp)
        ) {
            item(key = "header_content") {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    HeaderTitleSubtitleScreen(
                        title = R.string.choose_your_preferred_cuisine,
                        subtitle = R.string.dozens_food
                    )
                    SearchBar(
                        value = uiState.currentSearchQuery,
                        onValueChange = onSearchText)
                }
                Divider32()
            }

            if (uiState.isLoading) {
                item(key = "loading_content") {
                    LoadingRowState()
                }
            } else if(uiState.errorMessage!= null){
                item(key = "error_content") {
                    ErrorScreen()
                }
            } else {
                categoriesContent(
                    uiState = uiState,
                    onSelectedCategory = onSelectedCategory
                )
            }
        }
    }
}

fun LazyListScope.categoriesContent(
    uiState: SetupCuisineUiState,
    onSelectedCategory: (String) -> Unit
) {
    item(key = "search_header") {
        Column(Modifier.padding(horizontal = 24.dp)) {
            CategoryFoundHeader(
                searchQuery = uiState.currentSearchQuery,
                filteredCategories = uiState.filteredCategories
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
    }

    if(uiState.filteredCategories.isEmpty()){
        item {
            EmptySearchContent()
        }
    }

    val rows = uiState.filteredCategories.chunked(3)
    items(
        count = rows.size,
        key = { index -> "row_$index" }
    ) { rowIndex ->
        val rowItems = rows[rowIndex]
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            rowItems.forEach { item ->
                Box(modifier = Modifier.weight(1f)) {
                    FoodCategoryCard(
                        isSelected = uiState.selectedCategoryIds.contains(item.id),
                        category = item,
                        onCardClick = { onSelectedCategory(item.id) }
                    )
                }
            }
            repeat(3 - rowItems.size) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}


@Preview(showBackground = true, name = "Light Mode")
@Composable
fun SetupCuisineLightPreview() {
    val dummyCategories = listOf(
        CategoryUiModel("CAT-001", "Bakery", ""),
        CategoryUiModel("CAT-002", "Martabak", "")
    )
    TasstyTheme(darkTheme = false) {
        SetupCuisineScreen(
            uiState = SetupCuisineUiState(
                categories = dummyCategories,
                selectedCategoryIds = listOf(),
                currentSearchQuery = "bakery",
                isLoading = false
            ),
            onSearchText = {},
            onSelectedCategory = {},
            onNextClick = {},
            onSkipClick = {},
            onBackButtonClick = {}
        )
    }
}

//@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun SetupCuisineDarkPreview() {
    TasstyTheme(darkTheme = true) {
        SetupCuisineScreen(
            uiState = SetupCuisineUiState(
                categories = RestaurantPreviewData.categoriesUiModel,
                selectedCategoryIds = listOf("CAT-001"),
                currentSearchQuery = "bakery"
            ),
            onSearchText = {},
            onSelectedCategory = {},
            onNextClick = {},
            onSkipClick = {},
            onBackButtonClick = {}
        )
    }
}