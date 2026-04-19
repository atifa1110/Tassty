package com.example.tassty.screen.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.mapper.OrderFilterCategory
import com.example.core.ui.model.FilterOptionUi
import com.example.core.ui.model.OrderUiModel
import com.example.tassty.R
import com.example.tassty.component.CustomBorderChip
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.DatePickerContent
import com.example.tassty.component.Divider32
import com.example.tassty.component.EmptyOrderContent
import com.example.tassty.component.ErrorScreen
import com.example.tassty.component.HeaderListBlackTitle
import com.example.tassty.component.HeaderTitleScreen
import com.example.tassty.component.LoadingRowState
import com.example.tassty.component.MyOrderTopAppBar
import com.example.tassty.component.OrderListCard
import com.example.tassty.component.SearchBar
import com.example.tassty.getFilterIconRes
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.OrderPreviewData
import com.jakewharton.threetenabp.AndroidThreeTen

@Composable
fun OrderScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPayment:(String, String) -> Unit,
    onNavigateToOrderProcess:(String) -> Unit,
    onNavigateToOrderDetail:(String) -> Unit,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect (viewModel.navigation) {
        viewModel.navigation.collect { event ->
            when (event) {
                is OrderEvent.NavigateToPayment -> {
                    onNavigateToPayment(event.orderId, event.total)
                }
                is OrderEvent.NavigateToOrderDetail -> {
                    onNavigateToOrderDetail(event.orderId)
                }

                is OrderEvent.NavigateToOrderProcess -> {
                    onNavigateToOrderProcess(event.orderId)
                }
            }
        }
    }

    OrderContent(
        uiState = uiState,
        onFilterClick = viewModel::onFilterSelected,
        onCalendarClick = {viewModel.onCalendarClick(true)},
        onCardClick = viewModel::onOrderClicked,
        onNavigateBack = onNavigateBack
    )

    CustomBottomSheet(
        visible = uiState.isCalendarVisible,
        dismissOnClickOutside = false,
        onDismiss = { }
    ) {
        DatePickerContent(
            currentMonth = uiState.currentDisplayMonth,
            startDate = uiState.startDateSelected,
            endDate = uiState.endDateSelected,
            onDateClick = viewModel::onDateSelected,
            onMonthChange = viewModel::onMonthChange,
            onSelectClick = { viewModel.onCalendarClick(false) },
            onCancelClick = viewModel::onCancelClick
        )
    }
}

@Composable
fun OrderContent(
    uiState: OrderUiState,
    onFilterClick : (OrderFilterCategory) -> Unit,
    onCalendarClick:() -> Unit,
    onCardClick: (OrderUiModel) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = LocalCustomColors.current.background,
        topBar = {
            MyOrderTopAppBar(
                onBackClick = onNavigateBack,
                onCalendarCLick = onCalendarClick
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HeaderTitleScreen(title = stringResource(R.string.my_orders_header))
                    SearchBar(
                        value = "",
                        onValueChange = {},
                        placeholder = stringResource(R.string.search_for_your_order)
                    )
                    ChipOrderFilterSection(
                        options = uiState.filter,
                        onToggleOption = onFilterClick
                    )
                }
                Divider32()
            }

            val resource = uiState.orders

            when {
                resource.isLoading -> {
                    item {
                        LoadingRowState()
                    }
                }

                resource.errorMessage != null ->{
                    item {
                        ErrorScreen()
                    }
                }

                uiState.groupedOrders.isEmpty() -> {
                    item {
                        EmptyOrderContent()
                    }
                }

                resource.data != null -> {
                    val groupKeys = uiState.groupedOrders.keys.toList()
                    groupKeys.forEachIndexed { index, time ->
                        val orders = uiState.groupedOrders[time] ?: emptyList()
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                            ) {
                                HeaderListBlackTitle(title = time)
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }

                        items(orders, key = { it.id }) { order ->
                            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                                OrderListCard(order = order, onCardClick = onCardClick)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        if (index < groupKeys.size - 1) {
                            item {
                                Divider32()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChipOrderFilterSection(
    options: List<FilterOptionUi<OrderFilterCategory>>,
    onToggleOption: (OrderFilterCategory) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(options, key = { it.key }) { option ->
            CustomBorderChip(
                label = option.label,
                icon = getFilterIconRes(option),
                selected = option.isSelected,
                onClick = { onToggleOption(option.category) }
            )
        }
    }
}

//@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun OrderDarkPreview() {
    AndroidThreeTen.init(LocalContext.current)
    val activeFilter = OrderFilterCategory.ALL
    val mockFilters = OrderPreviewData.orderFilters.map {
        it.copy(isSelected = it.category == activeFilter)
    }

    val uiState = OrderUiState(
        orders = Resource(data = OrderPreviewData.orderUiList),
        filter = mockFilters,
        selectedCategory = activeFilter,
        isCalendarVisible = false,
        startDateSelected = null,
        endDateSelected = null
    )
    TasstyTheme (darkTheme = true){
        OrderContent(
            uiState = uiState,
            onFilterClick = {},
            onCalendarClick = {},
            onCardClick = {},
            onNavigateBack = {}
        )
    }
}
