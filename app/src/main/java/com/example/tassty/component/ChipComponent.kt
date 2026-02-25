package com.example.tassty.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.model.FilterOptionUi
import com.example.tassty.R
import com.example.tassty.model.getFilterDrawable
import com.example.tassty.model.getFilterPalette
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange200
import com.example.tassty.ui.theme.Orange50
import com.example.tassty.ui.theme.Orange500

@Composable
fun Personal30Chip(){
    CustomLongChip(
        image = R.drawable.chat_2,
        iconColor = Blue500,
        label = "Personal",
        labelColor = Neutral100,
        label2 = "30+ ratings",
        labelColor2 = Neutral70,
        color = Neutral20
    )
}


@Composable
fun AllReviewChip(){
    CustomShortChip(
        image = R.drawable.chat,
        color = Orange500,
        iconColor = Neutral10,
        label = "All reviews",
        labelColor = Neutral10
    )
}

@Composable
fun Personal50Chip(){
    CustomLongChip(
        image = R.drawable.chat,
        iconColor = Green500,
        label = "Personal",
        labelColor = Neutral100,
        label2 = "50+ ratings",
        labelColor2 = Neutral70,
        color = Neutral20
    )
}

@Composable
fun CustomShortChip(
    image : Int?,
    label : String,
    labelColor : Color,
    color: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
){
    Surface(
        modifier = modifier.clip(RoundedCornerShape(100.dp)),
        color = color,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 11.dp)
        ) {
            image?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = "",
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text = label,
                style = LocalCustomTypography.current.bodyMediumMedium,
                color = labelColor
            )
        }
    }
}

@Composable
fun CustomLongChip(
    image : Int,
    label : String,
    labelColor : Color,
    label2: String,
    labelColor2 : Color,
    color: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
){
    Surface(
        modifier = modifier.clip(RoundedCornerShape(99)),
        color = color,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 11.dp)
        ) {
            Icon(
                painter = painterResource(id = image),
                contentDescription = "",
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = label,
                style = LocalCustomTypography.current.bodyMediumSemiBold,
                color = labelColor
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = label2,
                style = LocalCustomTypography.current.bodyMediumMedium,
                color = labelColor2
            )
        }
    }
}

@Composable
fun CustomBorderChip(
    label: String,
    icon : Int,
    selected:Boolean,
    onClick : () -> Unit
){
    CustomShortChip(
        image = icon,
        color = if(selected) Orange50 else Neutral10,
        iconColor = if(selected)Orange500 else Neutral70,
        label = label,
        labelColor = if(selected) Neutral100 else Neutral70,
        modifier = Modifier.border(1.dp,if(selected) Orange200 else Neutral40,
            RoundedCornerShape(100.dp)
        ).clickable(onClick = onClick)
    )
}

@Composable
fun CustomSearchChip(
    label: String,
    icon : Int,
    selected: Boolean,
    onClick: () -> Unit,
) {
    CustomShortChip(
        image = icon,
        color = if(selected) Orange50 else Neutral20,
        iconColor = if(selected)Orange500 else Neutral70,
        label = label,
        labelColor = if(selected) Neutral100 else Neutral70,
        modifier = Modifier.border(1.dp,if(selected) Orange200 else
            Color.Transparent,
            RoundedCornerShape(100.dp)
        ).clickable(onClick = onClick)
    )
}

@Composable
fun CustomFilterChip(
    option: FilterOptionUi,
    onClick: () -> Unit,
) {
    val image = getFilterDrawable(option.iconRes)
    val pallet = getFilterPalette(option.category)
    CustomShortChip(
        image = image,
        color = if(option.isSelected) pallet.backgroundColor else Neutral20,
        iconColor = if(option.isSelected) pallet.iconColor else Neutral70,
        label = option.label,
        labelColor = if(option.isSelected) pallet.labelColor else Neutral70,
        modifier = Modifier.border(1.dp,if(option.isSelected) pallet.borderColor else Color.Transparent,
            RoundedCornerShape(100.dp)
        ).clickable(onClick = onClick)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewFilterScreen() {
    Column(
        modifier = Modifier.background(Neutral10),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AllReviewChip()
        Personal50Chip()
        Personal30Chip()
        CustomBorderChip(
            icon = R.drawable.star,
            label = "Rated 4.0+", selected = true,
            onClick = {}
        )

//        ChipFilterSection(
//            title = "Resto ratings",
//            options = restoRatingsOptions,
//            selectedKeys = setOf("Rated 2.0+", "tea", "juice")
//        ) { }
    }
}