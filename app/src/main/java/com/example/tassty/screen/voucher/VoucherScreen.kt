package com.example.tassty.screen.voucher

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.tassty.component.Divider32
import com.example.tassty.component.EmptyVoucherContent
import com.example.tassty.component.FavoriteTopAppBar
import com.example.tassty.component.HeaderListItemCountTitle
import com.example.tassty.component.LoadingRowState
import com.example.tassty.component.voucherVerticalListBlock
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.voucherUiModel

@Composable
fun VoucherScreen(
    viewModel: VoucherViewModel = hiltViewModel()
){
    val uiState by viewModel.voucherState.collectAsStateWithLifecycle()
    VoucherContent(
        resource = uiState
    )
}

@Composable
fun VoucherContent(
    resource: Resource<VoucherSections>
){
    val items = resource.data?: return
    Scaffold(
        containerColor = Neutral10,
        topBar = {
            FavoriteTopAppBar(
                onBackClick = {}
            ) { }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            item{
                Spacer(Modifier.height(24.dp))
                Text(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    text = "Promo & Vouchers.",
                    style = LocalCustomTypography.current.h2Bold,
                    color = Neutral100
                )
                Divider32()
            }

            when{
                resource.isLoading -> {
                    item {
                        LoadingRowState()
                    }
                }
                resource.errorMessage != null || items.available.isEmpty() -> {
                    item {
                        HeaderListItemCountTitle(
                            itemCount = items.available.size,
                            title = "Available",
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        EmptyVoucherContent()
                    }
                }
                resource.data != null -> {
                    val sections = resource.data?:return@LazyColumn
                    if(sections.available.isNotEmpty()){
                        voucherVerticalListBlock(
                            headerText = "Available",
                            voucherItems = sections.available,
                            onNavigateToDetail = {}
                        )
                    }

                    if(sections.upcoming.isNotEmpty()){
                        item {
                            Divider32()
                        }
                        voucherVerticalListBlock(
                            headerText = "Upcoming",
                            voucherItems = sections.upcoming,
                            onNavigateToDetail = {}
                        )
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun VoucherReview(){
    VoucherContent(
        resource = Resource(
            isLoading = false,
            data = VoucherSections(
                available = voucherUiModel
            ),
            errorMessage = null
        )
    )
}