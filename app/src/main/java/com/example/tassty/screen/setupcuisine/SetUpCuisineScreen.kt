package com.example.tassty.screen.setupcuisine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tassty.R
import com.example.tassty.categories
import com.example.tassty.component.ButtonSmallComponent
import com.example.tassty.component.SearchBarWhiteSection
import com.example.tassty.component.SetupTopAppBar
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import com.example.tassty.component.CategoryFoundHeader
import com.example.tassty.component.Divider32
import com.example.tassty.component.FoodCategoryCard
import com.example.tassty.model.Category

@Composable
fun SetupCuisineRoute(
    onNextClick: () -> Unit,
    onSkipClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: SetupCuisineViewModel = viewModel()
) {
    val state = viewModel.uiState
    val searchText by viewModel.searchQueryText.collectAsStateWithLifecycle()

    SetupCuisineScreen(
        uiState = state,
        searchText = searchText,
        onSearchText = viewModel::onSearchTextChanged,
        onSelectedCategory = viewModel::toggleCategorySelection,
        onNextClick = onNextClick,
        onSkipClick = onSkipClick,
        onBackClick = onBackClick
    )
}

@Composable
fun SetupCuisineScreen(
    uiState: SetupCuisineUiState,
    searchText: String,
    onSearchText : (String) -> Unit,
    onSelectedCategory: (Int) -> Unit,
    onNextClick: () -> Unit,
    onSkipClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = Neutral10,
        topBar = {
            SetupTopAppBar(
                currentStep = 1,
                totalStep = 2,
                onBackClick = onBackClick,
                onSkipClick = onSkipClick
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ButtonSmallComponent(
                    enabled = uiState.canProceed,
                    labelResId = R.string.next,
                    onClick = onNextClick
                )
            }
        }
    ) { innerPadding ->
        LazyColumn (
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
                .background(Neutral10)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.choose_your_preferred_cuisine),
                        style = LocalCustomTypography.current.h2Bold,
                        color = Neutral100
                    )

                    Text(
                        text = stringResource(R.string.dozens_food),
                        style = LocalCustomTypography.current.bodyMediumRegular,
                        color = Neutral70,
                        textAlign = TextAlign.Start
                    )

                    Spacer(Modifier.height(12.dp))

                    SearchBarWhiteSection(
                        value = searchText,
                        onValueChange = onSearchText
                    )
                }
            }

            item {
                Divider32()
            }
            item {
                if (uiState.isLoading || uiState.filteredCategories.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    CategoriesContent(
                        searchText = searchText,
                        selectedCategoryIds = uiState.selectedCategoryIds,
                        filteredCategories = uiState.filteredCategories,
                        onSelectedCategory = onSelectedCategory
                    )
                }
            }
        }
    }
}

@Composable
fun CategoriesContent(
    searchText: String,
    selectedCategoryIds: List<Int>,
    filteredCategories: List<Category>,
    onSelectedCategory: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CategoryFoundHeader(
            searchQuery = searchText,
            filteredCategories = filteredCategories,
        )

        Column(
            modifier = Modifier
        ) {
            filteredCategories.chunked(3).forEachIndexed { rowIndex, rowItems ->
                if (rowIndex > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEachIndexed { itemIndexInRow, item ->
                        FoodCategoryCard(
                            isSelected = selectedCategoryIds.contains(item.id),
                            category = item,
                            onCardClick = { onSelectedCategory(item.id) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSetupCuisineScreen() {
    SetupCuisineScreen(
        uiState = SetupCuisineUiState(
            categories = categories,
            selectedCategoryIds = listOf(1,2),
            currentSearchQuery = "bakery",
            filteredCategories = categories
        ),
        searchText = "",
        onSearchText = {},
        onSelectedCategory = {},
        onNextClick = {},
        onSkipClick = {},
        onBackClick = {}
    )
}