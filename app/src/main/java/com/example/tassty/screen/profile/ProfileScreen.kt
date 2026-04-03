package com.example.tassty.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tassty.R
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.CircleImageIcon
import com.example.tassty.component.CommonImage
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.Divider32
import com.example.tassty.component.LogoutContent
import com.example.tassty.component.ProfileTopAppBar
import com.example.tassty.ui.theme.Blue100
import com.example.tassty.ui.theme.Blue400
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.Green100
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Green600
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral80
import com.example.tassty.ui.theme.Orange100
import com.example.tassty.ui.theme.Orange200
import com.example.tassty.ui.theme.Orange600
import com.example.tassty.ui.theme.Orange900
import com.example.tassty.ui.theme.Pink100
import com.example.tassty.ui.theme.Pink50
import com.example.tassty.ui.theme.Pink500
import com.example.tassty.ui.theme.Pink600
import com.example.tassty.ui.theme.TasstyTheme

@Composable
fun ProfileScreen(
    onNavigateToCollection: () -> Unit,
    onNavigateToFavorite:() -> Unit,
    onNavigateToVoucher:()-> Unit,
    onNavigateToAddress: () -> Unit,
    onNavigateToCard: ()-> Unit,
    onNavigateToOrder: ()-> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToTerm: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileContent (
        uiState = uiState,
        onShowLogoutSheet = {viewModel.handleShowLogoutSheet(true)},
        onNavigateToCollection = onNavigateToCollection,
        onNavigateToFavorite = onNavigateToFavorite,
        onNavigateToVoucher = onNavigateToVoucher,
        onNavigateToAddress = onNavigateToAddress,
        onNavigateToCard = onNavigateToCard,
        onNavigateToOrder = onNavigateToOrder,
        onNavigateToEditProfile = onNavigateToEditProfile,
        onNavigateToTerm = onNavigateToTerm
    )

    CustomBottomSheet(
        visible = uiState.isLogoutSheetVisible,
        onDismiss = {},
        dismissOnClickOutside = false
    ) {
        LogoutContent(
            onLogout = {
                viewModel.onLogout()
                onNavigateToLogin()
            },
            onDismissClick = {
                viewModel.handleShowLogoutSheet(false)
            }
        )
    }
}
@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    onNavigateToCollection: () -> Unit,
    onNavigateToFavorite:() -> Unit,
    onNavigateToVoucher: () -> Unit,
    onNavigateToAddress: () -> Unit,
    onNavigateToCard: ()-> Unit,
    onNavigateToOrder: ()-> Unit,
    onShowLogoutSheet:() -> Unit,
    onNavigateToTerm: () -> Unit,
    onNavigateToEditProfile: () -> Unit
) {
    Scaffold (
        containerColor = LocalCustomColors.current.background,
        topBar = {
            ProfileTopAppBar(
                onEditClick = onNavigateToEditProfile
            )
        }
    ){ padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
        ) {
            item(key = "header") {
                ProfileHeaderSection(
                    name = uiState.name,
                    imageUrl = uiState.imageUrl,
                    email = uiState.email
                )
                Divider32()
            }
            
            item(key = "menu_section") {
                ProfileMenuSection(
                    onNavigateToCollection = onNavigateToCollection,
                    onNavigateToFavorite = onNavigateToFavorite,
                    onNavigateToVoucher = onNavigateToVoucher,
                    onNavigateToAddress = onNavigateToAddress,
                    onNavigateToCard = onNavigateToCard,
                    onNavigateToOrder = onNavigateToOrder
                )
                Divider32()
            }

            item (key = "support_section"){
                SupportSection(
                    onNavigateToTerm = onNavigateToTerm
                )
                Spacer(Modifier.height(24.dp))
            }

            item(key = "logout_section"){
                ButtonComponent(
                    labelResId = R.string.logout,
                    onClick = onShowLogoutSheet,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Pink50,
                        contentColor = Pink500,
                        disabledContentColor = Neutral100,
                        disabledContainerColor = Neutral40
                    )
                )
            }
        }
    }
}

@Composable
fun ProfileHeaderSection(
    name: String,
    imageUrl: String,
    email:String,
) {
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
                                    imageUrl = imageUrl,
                                    name = "profile picture"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth().height(60.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = name,
                                style = LocalCustomTypography.current.h4Bold,
                                color = Neutral10
                            )

                            Text(
                                text = email,
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
fun ProfileMenuSection(
    onNavigateToCollection: () -> Unit,
    onNavigateToFavorite:() -> Unit,
    onNavigateToVoucher: () -> Unit,
    onNavigateToAddress: () -> Unit,
    onNavigateToCard: ()-> Unit,
    onNavigateToOrder: ()-> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Account",
            style = LocalCustomTypography.current.h5Bold,
            color = LocalCustomColors.current.headerText
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = LocalCustomColors.current.cardBackground
            )
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                ProfileMenuItem(
                    icon = R.drawable.icon,
                    iconColor = Green600,
                    boxColor = Green100,
                    title = "My orders",
                    onClick = onNavigateToOrder
                )
                
                HorizontalDivider(color = LocalCustomColors.current.divider)
                
                ProfileMenuItem(
                    icon = R.drawable.promo,
                    iconColor = Orange600,
                    boxColor = Orange100,
                    title = "Vouchers",
                    onClick = onNavigateToVoucher
                )

                HorizontalDivider(color = LocalCustomColors.current.divider)

                ProfileMenuItem(
                    icon = R.drawable.calendar,
                    iconColor = Orange600,
                    boxColor = Orange100,
                    title = "Subscriptions",
                    onClick = {}
                )

                HorizontalDivider(color = LocalCustomColors.current.divider)

                ProfileMenuItem(
                    icon = R.drawable.heart,
                    iconColor = Pink600,
                    boxColor = Pink100,
                    title = "Collections",
                    onClick = onNavigateToCollection
                )

                HorizontalDivider(color = LocalCustomColors.current.divider)

                ProfileMenuItem(
                    icon = R.drawable.store,
                    iconColor = Pink600,
                    boxColor = Pink100,
                    title = "Favorite Restaurants",
                    onClick = onNavigateToFavorite
                )

                HorizontalDivider(color = LocalCustomColors.current.divider)

                ProfileMenuItem(
                    icon = R.drawable.credit_card,
                    iconColor = Pink600,
                    boxColor = Pink100,
                    title = "Payment Methods",
                    onClick = onNavigateToCard
                )

                HorizontalDivider(color = LocalCustomColors.current.divider)

                ProfileMenuItem(
                    icon = R.drawable.location,
                    iconColor = Blue500,
                    boxColor = Blue100,
                    title = "My Addresses",
                    onClick = onNavigateToAddress
                )

                HorizontalDivider(color = LocalCustomColors.current.divider)

                ProfileMenuItem(
                    icon = R.drawable.calendar,
                    iconColor = Blue500,
                    boxColor = Blue100,
                    title = "Invite Friends",
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun SupportSection(
    onNavigateToTerm: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Supports",
            style = LocalCustomTypography.current.h5Bold,
            color = LocalCustomColors.current.headerText
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = LocalCustomColors.current.cardBackground
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
                    onClick = {}
                )

                HorizontalDivider(color = LocalCustomColors.current.divider)

                ProfileMenuItem(
                    icon = R.drawable.question_mark_circle,
                    iconColor = Orange600,
                    boxColor = Orange100,
                    title = "Help Center",
                    onClick = {}
                )

                HorizontalDivider(color = LocalCustomColors.current.divider)

                ProfileMenuItem(
                    icon = R.drawable.clipboard_list,
                    iconColor = Orange600,
                    boxColor = Orange100,
                    title = "Terms of Service",
                    onClick = onNavigateToTerm
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
    textColor: Color = LocalCustomColors.current.headerText,
    onClick: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 14.dp).clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleImageIcon(
            modifier = Modifier.size(32.dp),
            boxColor = boxColor,
            iconSize = 16.dp,
            icon = icon,
            iconColor = iconColor,
            contentDescription = ""
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = LocalCustomTypography.current.h6Bold,
            color = textColor
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            tint = LocalCustomColors.current.iconFocused,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun ProfileMenuSwitchItem(
    icon: Int,
    iconColor: Color,
    boxColor: Color,
    title: String,
    textColor: Color = LocalCustomColors.current.headerText,
    isSwitch: Boolean = false,
    onSwitchChange:(Boolean) -> Unit,
    onClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth().padding(horizontal = 12.dp, vertical = 14.dp)
        .clickable(onClick= onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleImageIcon(
            boxColor =boxColor,
            icon = icon,
            iconColor = iconColor,
            iconSize = 16.dp,
            contentDescription = title,
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = LocalCustomTypography.current.h6Bold,
            color = textColor
        )

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


//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun ProfileLightPreview() {
//    TasstyTheme {
//        ProfileContent(
//            uiState = ProfileUiState(
//                name = "Atifa Fiorenza",
//                email = "atifafiorenza24@gmail.com",
//                imageUrl = "",
//            ),
//            onNavigateToCard = {},
//            onNavigateToOrder = {},
//            onNavigateToVoucher = {},
//            onNavigateToAddress = {},
//            onNavigateToFavorite = {},
//            onNavigateToCollection = {},
//            onNavigateToEditProfile = {},
//            onShowLogoutSheet = {}
//        )
//    }
//}
//
//@Preview(showBackground = true, name = "Dark Mode")
//@Composable
//fun ProfileDarkPreview() {
//    TasstyTheme (darkTheme = true){
//        ProfileContent(
//            uiState = ProfileUiState(
//                name = "Atifa Fiorenza",
//                email = "atifafiorenza24@gmail.com",
//                imageUrl = "",
//            ),
//            onNavigateToCard = {},
//            onNavigateToOrder = {},
//            onNavigateToVoucher = {},
//            onNavigateToAddress = {},
//            onNavigateToFavorite = {},
//            onNavigateToCollection = {},
//            onNavigateToEditProfile = {},
//            onShowLogoutSheet = {}
//        )
//    }
//}