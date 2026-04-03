package com.example.tassty.screen.voucher

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.tassty.R
import com.example.tassty.component.Divider32
import com.example.tassty.component.EmptyVoucherContent
import com.example.tassty.component.FavoriteTopAppBar
import com.example.tassty.component.Header
import com.example.tassty.component.HeaderListItemCountTitle
import com.example.tassty.component.HeaderTitleScreen
import com.example.tassty.component.LoadingRowState
import com.example.tassty.component.voucherVerticalListBlock
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.voucherUiModel

@Composable
fun VoucherScreen(
    onNavigateBack: () -> Unit,
    viewModel: VoucherViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    VoucherContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun VoucherContent(
    onNavigateBack: () -> Unit,
    uiState: VoucherUiState
){
    Scaffold(
        containerColor = Neutral10,
        topBar = {
            FavoriteTopAppBar(
                onBackClick = onNavigateBack,
                onSearchClick = {}
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier .padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(top = 24.dp, bottom = 32.dp)
        ) {
            item{
                HeaderTitleScreen(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    title = stringResource(R.string.promo_vouchers)
                )
                Divider32()
            }

            when {
                uiState.vouchers.isLoading -> {
                    item {
                        LoadingRowState()
                    }
                }

                uiState.vouchers.errorMessage != null -> {
                    item {
                        HeaderListItemCountTitle(
                            itemCount = 0,
                            title = stringResource(R.string.available),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        EmptyVoucherContent()
                    }
                }
            }

            val groups = uiState.groupedVouchers.entries.toList()
            groups.forEachIndexed { index, entry ->
                val (headerName, voucherList) = entry

                voucherVerticalListBlock(
                    headerText = "$headerName Vouchers",
                    voucherItems = voucherList,
                    onNavigateToDetailVoucher = {}
                )

                if (index < groups.size - 1) {
                    item {
                        Divider32()
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun VoucherLightPreview(){
//    TasstyTheme {
//        VoucherContent(
//            uiState = VoucherUiState(
//                vouchers = Resource(
//                    isLoading = false,
//                    data = voucherUiModel,
//                    errorMessage = null
//                )
//            ),
//            onNavigateBack = {}
//        )
//    }
//}