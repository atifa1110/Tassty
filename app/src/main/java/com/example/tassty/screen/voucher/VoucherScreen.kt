package com.example.tassty.screen.voucher

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.example.tassty.component.HeaderListItemCountTitle
import com.example.tassty.component.HeaderTitleScreen
import com.example.tassty.component.LoadingRowState
import com.example.tassty.component.voucherVerticalListBlock
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.voucherUiModel
import kotlinx.collections.immutable.toImmutableList

@Composable
fun VoucherScreen(
    onNavigateBack: () -> Unit,
    viewModel: VoucherViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    VoucherContent(
        context = context,
        uiState = uiState,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun VoucherContent(
    context: Context,
    onNavigateBack: () -> Unit,
    uiState: VoucherUiState
){
    Scaffold(
        containerColor = LocalCustomColors.current.background,
        topBar = {
            FavoriteTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                onBackClick = onNavigateBack,
                onSearchClick = {},
                iconBackground = LocalCustomColors.current.topBarBackgroundColor
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(top = 24.dp, bottom = 32.dp)
        ) {
            item(key="header"){
                HeaderTitleScreen(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    title = stringResource(R.string.promo_vouchers)
                )
                Divider32()
            }

            when {
                uiState.vouchers.isLoading -> {
                    item(key = "loading_content") {
                        LoadingRowState()
                    }
                }

                uiState.vouchers.errorMessage != null -> {
                    item(key = "error_content") {
                        HeaderListItemCountTitle(
                            itemCount = 0,
                            title = stringResource(R.string.available),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        EmptyVoucherContent()
                    }
                }

                uiState.vouchers.data != null -> {
                    val groups = uiState.groupedVouchers
                    groups.entries.forEachIndexed { index, entry ->
                        val (headerName, voucherList) = entry

                        voucherVerticalListBlock(
                            headerText = context.getString(R.string.header_vouchers, headerName),
                            voucherItems = voucherList,
                            onNavigateToDetailVoucher = {}
                        )

                        if (index < groups.size - 1) {
                            item(key = "divider_$headerName") {
                                Divider32()
                            }
                        }
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true, name = "Light Mode")
@Composable
fun VoucherLightPreview(){
    TasstyTheme {
        VoucherContent(
            context = LocalContext.current,
            uiState = VoucherUiState(
                vouchers = Resource(
                    isLoading = false,
                    data = voucherUiModel.toImmutableList(),
                    errorMessage = null
                )
            ),
            onNavigateBack = {}
        )
    }
}

//@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun VoucherDarkPreview(){
    TasstyTheme(darkTheme = true) {
        VoucherContent(
            context = LocalContext.current,
            uiState = VoucherUiState(
                vouchers = Resource(
                    isLoading = false,
                    data = voucherUiModel.toImmutableList(),
                    errorMessage = null
                )
            ),
            onNavigateBack = {}
        )
    }
}