package com.example.tassty.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.addresses
import com.example.tassty.model.AddressType
import com.example.tassty.model.UserAddress
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange50
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink100
import com.example.tassty.ui.theme.Pink600

@Composable
fun LocationCard(
    address: UserAddress
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Neutral20
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ){

        CircleImageIcon(
            boxColor = Pink100,
            icon = R.drawable.location,
            iconColor = Pink600,
            iconSize = 16.dp,
            contentDescription = "location icon",
            modifier = Modifier.size(32.dp)
        )


        Column(modifier = Modifier.fillMaxWidth()){
                Text(
                    text = address.addressName,
                    style = LocalCustomTypography.current.h6Bold,
                    color = Neutral100
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = address.fullStreetAddress,
                    style = LocalCustomTypography.current.bodySmallMedium,
                    color = Neutral70
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    NotesText(" - ")
                    NotesBoxButton(
                        title = "Notes",
                        onClick = {}
                    )
                }
            }

        }
    }
}

@Composable
fun LocationSelectorCard(
    address: UserAddress,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable{onCheckedChange(address.isSelected)},
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(width= 1.dp, color = if(address.isSelected) Orange500 else Neutral40),
        colors = CardDefaults.cardColors(
            containerColor = if(address.isSelected) Orange50 else Neutral10
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier.size(94.dp,104.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Neutral60)
            )
            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f).height(104.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = address.addressName,
                    style = LocalCustomTypography.current.h5Bold,
                    color = Neutral100
                )
                Spacer(Modifier.height(4.dp))
                LocationContent(
                    verticalAlignment = Alignment.Top,
                    icon = R.drawable.location,
                    iconColor = Pink600,
                    value = address.fullStreetAddress
                )
                Spacer(Modifier.height(12.dp))
                LocationContent(
                    icon = R.drawable.person,
                    iconColor = Blue500,
                    value = address.addressType.displayName
                )
            }
            Spacer(Modifier.width(8.dp))
            Checkbox(
                checked = address.isSelected,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(0.dp).size(24.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = Orange500,
                    uncheckedColor = Neutral40
                )
            )

        }
    }
}

@Composable
fun LocationContent(
    icon: Int,
    iconColor: Color,
    value: String,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically
){
    Row (verticalAlignment = verticalAlignment){
        Icon(
            painter = painterResource(icon),
            tint = iconColor,
            contentDescription = "location icon",
        )
        Spacer(Modifier.width(2.dp))
        Text(
            text = value,
            style = LocalCustomTypography.current.bodySmallMedium,
            color = Neutral70
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLocationCard() {
    Column(modifier = Modifier.fillMaxWidth().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        LocationCard(address = addresses[0])
        LocationSelectorCard(
            address = addresses[0],
            onCheckedChange = {}
        )
    }
}