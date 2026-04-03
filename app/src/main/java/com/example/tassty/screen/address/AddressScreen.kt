package com.example.tassty.screen.address

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.tassty.util.addresses
import com.example.tassty.component.AddTopAppBar
import com.example.tassty.component.EmptyAddressContent
import com.example.tassty.component.ErrorScreen
import com.example.tassty.component.HeaderTitleScreen
import com.example.tassty.component.ShimmerAddressCard
import com.example.tassty.component.addressVerticalListBlock
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500

@Composable
fun AddressScreen(
    onNavigateBack:() -> Unit,
    viewModel: AddressViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AddressContent(
        uiState = uiState,
        onTabSelected = {viewModel.onTabSelected(it)},
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun AddressContent(
    uiState: AddressUiState,
    onTabSelected: (AddressTab) -> Unit,
    onNavigateBack:() -> Unit,
) {
    Scaffold(
        containerColor = Neutral10,
        topBar = {
            AddTopAppBar (
                onAddClick = {},
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item{
                HeaderTitleScreen(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    title = "My addresses."
                )
                Spacer(Modifier.height(8.dp))
                AddressTabContent (
                    selectedType = uiState.selectedTab,
                    onTabSelected = onTabSelected
                )
            }

            val resource = uiState.addressResource
            val address = resource.data.orEmpty()

            when{
                resource.isLoading -> {
                    items(count = 4){
                        Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                            ShimmerAddressCard()
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }
                resource.errorMessage!=null ->{
                    item {
                        ErrorScreen()
                    }
                }

                address.isEmpty() ->{
                    item {
                        EmptyAddressContent()
                    }
                }

                else ->{
                    addressVerticalListBlock(
                        headerText = "addresses",
                        addressItems = address
                    )
                }
            }
        }
    }
}

@Composable
fun AddressTabContent(
    selectedType: AddressTab,
    onTabSelected: (AddressTab) -> Unit
) {
    val tabs = AddressTab.entries.toTypedArray()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = Neutral20,
                shape = RoundedCornerShape(50)
            )
            .padding(6.dp)
    ) {
        Row {
            tabs.forEachIndexed { index, type ->
                val isSelected = type == selectedType

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (isSelected) Orange500 else Color.Transparent
                        )
                        .clickable { onTabSelected(type)}
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = type.title,
                        color = if (isSelected) Neutral10 else Neutral70,
                        style = LocalCustomTypography.current.h7Bold
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddressListPreview() {
    AddressContent(
        uiState = AddressUiState(
            selectedTab = AddressTab.ALL,
            addressResource = Resource(data = addresses)
        ),
        onTabSelected = {},
        onNavigateBack = {}
    )
}