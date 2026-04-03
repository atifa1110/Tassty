package com.example.tassty.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.model.CollectionUiModel
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.util.collectionUiModel
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Orange50
import com.example.tassty.ui.theme.Orange500

@Composable
fun CollectionCard(
    collection: CollectionUiModel,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable{onCheckedChange(!collection.isSelected)},
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(width=1.dp, color = if(collection.isSelected) Orange500 else Neutral40),
        colors = CardDefaults.cardColors(containerColor = if (collection.isSelected) LocalCustomColors.current.selectedOrangeBackground else LocalCustomColors.current.cardBackground2)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CollectionImageRound(
                    collection = collection,
                    modifier = Modifier.size(75.dp)
                )

                Column (verticalArrangement = Arrangement.Center){
                    Text(
                        text = collection.title,
                        style = LocalCustomTypography.current.h5Bold,
                        color = LocalCustomColors.current.headerText
                    )
                    Spacer(Modifier.height(6.dp))
                    CollectionText(itemCount = collection.menuCount)
                }
            }

            Checkbox(
                checked = collection.isSelected,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Orange500,
                    uncheckedColor = Neutral40
                ),
                modifier = Modifier.padding(0.dp).size(24.dp)
            )
        }
    }
}

@Composable
fun CollectionVerticalCard(
    collection: CollectionUiModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.width(160.dp).clickable{
            onClick()
        },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.cardBackground)
    ) {
        Column (
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CollectionImageRound(
                collection = collection,
                modifier = Modifier.height(140.dp)
            )

            Column (
                modifier = Modifier.padding(4.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ){
                Text(
                    text = collection.title,
                    style = LocalCustomTypography.current.h5Bold,
                    color = LocalCustomColors.current.headerText,
                    minLines = 1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                CollectionText(itemCount = collection.menuCount)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCollectionCard() {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(24.dp)
    ) {
        CollectionCard(collection = collectionUiModel[0], onCheckedChange = {})
        CollectionVerticalCard(collectionUiModel[1], onClick = {})
    }
}