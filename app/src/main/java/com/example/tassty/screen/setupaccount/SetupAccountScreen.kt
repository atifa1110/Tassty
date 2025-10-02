package com.example.tassty.screen.setupaccount

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.component.BoxLocation
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.FoodCategoryGrid
import com.example.tassty.component.SetupTopAppBar
import com.example.tassty.component.SimpleSearchBar
import com.example.tassty.model.Category
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500

enum class SetupStage {
    CUISINE,
    LOCATION
}

@Composable
fun SetupAccountScreen() {
    // State yang menentukan UI mana yang akan ditampilkan
    var currentStage by remember { mutableStateOf(SetupStage.CUISINE) }

    when (currentStage) {
        SetupStage.CUISINE -> {
            SetupCuisineScreen(
                categories = listOf(
                    Category(1, "","Salad"),
                    Category(2,"","Burger"),
                    Category(3,"","Pizza"),
                    Category(4,"","Bakery"),
                    Category(5,"","Steak"),
                    Category(6,"","Salad"),
                    Category(7,"","Burger"),
                    Category(8,"","Pizza"),
                    Category(9,"","Bakery"),
                    Category(10,"","Steak"),
                ),
                // Tombol "Next" akan mengubah state
                onNextClick = {
                    currentStage = SetupStage.LOCATION
                },
                onSkipClick = {
                    // Logika untuk melewati
                },
                onBackClick = {}
            )
        }
        SetupStage.LOCATION -> {
            SetupLocationScreen(
                onBackClick = {},
                onSkipClick = {},
                // Tombol "Submit" akan menyelesaikan setup
                onSubmitClick = {
                    // Logika untuk navigasi ke halaman utama
                }
            )
        }
    }
}

// Composables dummy untuk setiap stage
@Composable
fun SetupCuisineScreen(
    categories : List<Category>,
    onNextClick: () -> Unit,
    onSkipClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val selectedCategoryIds = remember { mutableStateListOf<Int>() }
    // Daftar kategori yang disaring berdasarkan teks pencarian
    val filteredCategories = categories.filter {
        it.name.contains(searchText, ignoreCase = true)
    }

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
                    .padding(24.dp)
            ) {
                ButtonComponent(
                    enabled = selectedCategoryIds.isNotEmpty(),
                    labelResId = R.string.next,
                    onClick = onNextClick
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Neutral10)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
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

                SimpleSearchBar(
                    text= searchText,
                    onTextChange = {searchText =it}
                )
            }

            HorizontalDivider(
                color = Neutral30
            )

            FoodCategoryGrid(
                searchQuery = searchText,
                selectedCategoryIds = selectedCategoryIds,
                filteredCategories = filteredCategories,
            )
        }
    }
}

@Composable
fun SetupLocationScreen(
    onBackClick:() -> Unit,
    onSkipClick: () -> Unit,
    onSubmitClick: () -> Unit
) {
    Scaffold(
        containerColor = Neutral10,
        topBar = {
            SetupTopAppBar(
                currentStep = 2,
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
                    .padding(24.dp)
            ) {
                ButtonComponent(
                    enabled = true,
                    labelResId = R.string.submit,
                    onClick = onSubmitClick
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Neutral10)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.setup_your_delivery_location),
                    style = LocalCustomTypography.current.h2Bold,
                    color = Neutral100
                )

                Text(
                    text = stringResource(R.string.primary_delivery_location),
                    style = LocalCustomTypography.current.bodyMediumRegular,
                    color = Neutral70,
                    textAlign = TextAlign.Start
                )
            }

            HorizontalDivider(color = Neutral30)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.primary_address),
                        style = LocalCustomTypography.current.h5Bold
                    )

                    Text(
                        modifier= Modifier.clickable{},
                        text = stringResource(R.string.set_location),
                        style = LocalCustomTypography.current.bodyMediumMedium,
                        color = Orange500
                    )

                }

                BoxLocation() { }
            }
        }
    }
}


@Preview
@Composable
fun PreviewSetupAccountScreen() {
    SetupLocationScreen (
        onBackClick = {},
        onSkipClick={},
        onSubmitClick={}
    )
}