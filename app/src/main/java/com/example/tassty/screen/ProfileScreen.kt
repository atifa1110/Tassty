package com.example.tassty.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.radialGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tassty.R
import com.example.tassty.component.BackTopAppBar
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.ButtonLogout
import com.example.tassty.component.CommonImage
import com.example.tassty.component.ProfileTopAppBar
import com.example.tassty.ui.theme.Blue100
import com.example.tassty.ui.theme.Blue400
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.Green100
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.Green600
import com.example.tassty.ui.theme.Neutral80
import com.example.tassty.ui.theme.Orange100
import com.example.tassty.ui.theme.Orange200
import com.example.tassty.ui.theme.Orange300
import com.example.tassty.ui.theme.Orange50
import com.example.tassty.ui.theme.Orange600
import com.example.tassty.ui.theme.Orange900
import com.example.tassty.ui.theme.Pink100
import com.example.tassty.ui.theme.Pink500
import com.example.tassty.ui.theme.Pink600

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit = {}
) {
    Scaffold (
        containerColor = Neutral10,
        topBar = {
            // Top App Bar
            ProfileTopAppBar()
        }
    ){ padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize().background(Neutral10)
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                ProfileHeaderSection()
            }
            
            item {
                HorizontalDivider(color = Neutral30)
            }
            
            item {
                ProfileMenuSection()
            }

            item {
                HorizontalDivider(color = Neutral30)
            }

            item {
                SupportSection()
            }

            item{
                ButtonLogout(
                    enabled = true,
                    labelResId = R.string.logout,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    onClick = {}
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ProfileHeaderSection() {
    Column(Modifier.padding(horizontal = 24.dp)){
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colorStops = arrayOf(
                                0.0f to Color(0xFFFFCF24),
                                0.40f to Color(0xFFF07C2A),
                                0.82f to Color(0xFFD76413)
                            ),
                            start = Offset(0f, -20f),
                            end = Offset(0f, Float.POSITIVE_INFINITY)
                        )
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Profile Picture
                        Box(
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Neutral20)
                                    .border(3.dp, Neutral10.copy(0.25f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                CommonImage(
                                    imageUrl = "",
                                    name = "profile picture"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth().height(60.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // User Name
                            Text(
                                text = "Rafiq Daniel",
                                style = LocalCustomTypography.current.h4Bold,
                                color = Neutral10
                            )

                            // User Email
                            Text(
                                text = "rafiq.daniel@email.com",
                                style = LocalCustomTypography.current.bodyXtraSmallMedium,
                                color = Neutral10
                            )

                            Text(
                                text = "0791-1234-5678",
                                style = LocalCustomTypography.current.bodyXtraSmallMedium,
                                color = Neutral10
                            )

                        }
                    }

                    HorizontalDivider(color = Neutral10.copy(0.2f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier.size(26.dp)
                                .clip(CircleShape).background(Orange200),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.crown),
                                contentDescription = "Crown",
                                tint = Orange900,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Gold Member",
                                style = LocalCustomTypography.current.h7Bold,
                                color = Neutral10
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "2,230",
                                    style = LocalCustomTypography.current.h8Bold,
                                    color = Neutral10
                                )
                                Spacer(Modifier.width(2.dp))
                                Text(
                                    text = "points",
                                    style = LocalCustomTypography.current.bodyXtraSmallMedium,
                                    color = Neutral10
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Navigate",
                            tint = Neutral10,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier.size(110.dp)
                        .offset(y = -(18).dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(R.drawable.logo_white),
                        contentDescription = "logo",
                        tint = Neutral10.copy(0.12f)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileMenuSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Account",
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Neutral20
            )
        ) {
            Column {
                ProfileMenuItem(
                    icon = R.drawable.icon,
                    iconColor = Green600,
                    boxColor = Green100,
                    title = "My orders",
                    onClick = { /* Navigate to personal info */ }
                )
                
                HorizontalDivider(
                    color = Neutral30
                )
                
                ProfileMenuItem(
                    icon = R.drawable.promo,
                    iconColor = Orange600,
                    boxColor = Orange100,
                    title = "Vouchers",
                    onClick = { /* Navigate to personal info */ }
                )
                
                HorizontalDivider(
                    color = Neutral30
                )
                
                ProfileMenuItem(
                    icon = R.drawable.calendar,
                    iconColor = Orange600,
                    boxColor = Orange100,
                    title = "Subscriptions",
                    onClick = { /* Navigate to personal info */ }
                )
                
                HorizontalDivider(
                    color = Neutral30
                )
                
                ProfileMenuItem(
                    icon = R.drawable.heart,
                    iconColor = Pink600,
                    boxColor = Pink100,
                    title = "Collections",
                    onClick = { /* Navigate to personal info */ }
                )
                
                HorizontalDivider(
                    color = Neutral30
                )
                
                ProfileMenuItem(
                    icon = R.drawable.heart,
                    iconColor = Pink600,
                    boxColor = Pink100,
                    title = "Favorite Restaurants",
                    onClick = { /* Navigate to personal info */ }
                )
                
                HorizontalDivider(
                    color = Neutral30
                )
                
                ProfileMenuItem(
                    icon = R.drawable.heart,
                    iconColor = Pink600,
                    boxColor = Pink100,
                    title = "Payment Methods",
                    onClick = { /* Navigate to personal info */ }
                )

                HorizontalDivider(
                    color = Neutral30
                )

                ProfileMenuItem(
                    icon = R.drawable.location,
                    iconColor = Blue500,
                    boxColor = Blue100,
                    title = "My Addresses",
                    onClick = { /* Navigate to personal info */ }
                )

                HorizontalDivider(
                    color = Neutral30
                )

                ProfileMenuItem(
                    icon = R.drawable.calendar,
                    iconColor = Blue500,
                    boxColor = Blue100,
                    title = "Invite Friends",
                    onClick = { /* Navigate to personal info */ }
                )
            }
        }
    }
}

@Composable
fun SupportSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Supports",
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Neutral20
            )
        ) {
            Column {
                ProfileMenuSwitchItem(
                    icon = R.drawable.calendar,
                    iconColor = Blue500,
                    boxColor = Blue100,
                    title = "Dark Mode",
                    isSwitch = true,
                    onSwitchChange ={},
                    onClick = { /* Navigate to personal info */ }
                )

                HorizontalDivider(
                    color = Neutral30
                )

                ProfileMenuItem(
                    icon = R.drawable.calendar,
                    iconColor = Orange600,
                    boxColor = Orange100,
                    title = "Help Center",
                    onClick = { /* Navigate to personal info */ }
                )

                HorizontalDivider(
                    color = Neutral30
                )

                ProfileMenuItem(
                    icon = R.drawable.calendar,
                    iconColor = Orange600,
                    boxColor = Orange100,
                    title = "Terms of Service",
                    onClick = { /* Navigate to personal info */ }
                )
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: Int,
    iconColor: Color,
    boxColor: Color,
    title: String,
    textColor: Color = Neutral100,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = Neutral20
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape)
                    .background(boxColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Text Content
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = LocalCustomTypography.current.h6Bold,
                color = textColor
            )
            
            // Arrow
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = Neutral100,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun ProfileMenuSwitchItem(
    icon: Int,
    iconColor: Color,
    boxColor: Color,
    title: String,
    textColor: Color = Neutral100,
    isSwitch: Boolean = false,
    onSwitchChange:(Boolean) -> Unit,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = Neutral20
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape)
                    .background(boxColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Text Content
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = LocalCustomTypography.current.h6Bold,
                color = textColor
            )

            // Arrow
            Switch(
                checked = isSwitch,
                onCheckedChange = onSwitchChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Blue500,
                    checkedTrackColor = Blue400,
                    uncheckedThumbColor = Neutral80,
                    uncheckedTrackColor = Neutral70
                )
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}