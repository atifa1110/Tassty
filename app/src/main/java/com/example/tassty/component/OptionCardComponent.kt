package com.example.tassty.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.ui.mapper.FilterCategory
import com.example.core.ui.model.FilterOptionUi
import com.example.tassty.R
import com.example.tassty.model.ChipFilterOption
import com.example.tassty.model.getFilterDrawable
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Orange500
import kotlin.collections.forEach

@Composable
fun ChipFilterSection(
    title: String,
    options: List<FilterOptionUi<FilterCategory>>,
    onToggleOption: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = title,
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100,
        )
        Spacer(Modifier.height(12.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                CustomBorderChip(
                    label = option.label,
                    icon = getFilterDrawable(option.iconRes)?:0,
                    selected = option.isSelected,
                    onClick = { onToggleOption(option.key) }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipSearchSection(
    title: String,
    options: List<ChipFilterOption>,
    selected: Boolean
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderListBlackTitle(title = title)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                CustomSearchChip(
                    label = option.label,
                    icon = option.iconId?:0,
                    selected = selected,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun ChipSearchExpandSection(
    title: String,
    options: List<ChipFilterOption>
) {
    var expanded by remember { mutableStateOf(false) }
    val maxVisible = 5

    Column(modifier = Modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HeaderListBlackTitle(title = title)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val visibleOptions = if (expanded || options.size <= 5) {
                options
            } else {
                options.take(maxVisible - 1)
            }

            visibleOptions.forEach { option ->
                CustomSearchChip(
                    label = option.label,
                    icon = option.iconId ?: 0,
                    selected = true,
                    onClick = {}
                )
            }

            // Tambahkan expand button kalau belum expanded
            if (!expanded && options.size > maxVisible) {
                CustomSearchChip(
                    label = "",
                    icon = R.drawable.arrow_down, // pakai icon ▼
                    selected = true,
                    onClick = { expanded = true }
                )
            }

        }
    }
}

@Composable
fun RadioFilterTitleSection(
    title: String,
    isTitleShown: Boolean = true,
    options: List<FilterOptionUi<FilterCategory>>,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal=24.dp),
    ) {
        if(isTitleShown) {
            Text(
                text = title,
                style = LocalCustomTypography.current.h5Bold,
                color = Neutral100,
            )
            Spacer(Modifier.height(24.dp))
        }
        options.forEachIndexed { index, option ->
            RadioFilterItem(
                label = option.label,
                isSelected = option.isSelected,
                onSelect = { onOptionSelected(option.key) }
            )
            if (index < options.lastIndex) {
                DashedDivider(
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
    }
}

@Composable
fun RadioFilterItem(
    label : String,
    isSelected: Boolean,
    onSelect: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Neutral100,
            style = LocalCustomTypography.current.h6Regular,
            modifier = Modifier.weight(1f)
        )

        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = Orange500,
                unselectedColor = Neutral40
            ),
            modifier = Modifier.padding(0.dp).size(24.dp)

        )
    }
}