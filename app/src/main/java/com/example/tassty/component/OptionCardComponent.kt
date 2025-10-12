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
import com.example.tassty.R
import com.example.tassty.model.ChipFilterOption
import com.example.tassty.model.RadioFilterOption
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Orange500
import okhttp3.internal.http2.Header
import kotlin.collections.forEach

@Composable
fun SortOptionItemStandard(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Column {
        options.forEachIndexed { index, option ->
            RadioFilterItem(
                label = option,
                isSelected = selected == option,
                onSelect = { onSelect(option) }
            )

            if (index < options.lastIndex) {
                DashedDivider(
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipFilterSection(
    title: String,
    options: List<ChipFilterOption>,
    selectedKeys: Set<String>,
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
                val isSelected = selectedKeys.contains(option.label)

                CustomBorderChip(
                    label = option.label,
                    icon = option.iconId?:0,
                    selected = isSelected,
                    onClick = { onToggleOption(option.label) }
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

@OptIn(ExperimentalLayoutApi::class)
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
                options.take(maxVisible - 1) // show 4 chip + 1 expand button
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
fun RadioFilterSection(
    title: String,
    options: List<RadioFilterOption>,
    selectedKey: String,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal=24.dp),
    ) {
        Text(
            text = title,
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100,
        )
        Spacer(Modifier.height(24.dp))
        options.forEachIndexed { index, option ->
            RadioFilterItem(
                label = option.label,
                isSelected = selectedKey == option.key,
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