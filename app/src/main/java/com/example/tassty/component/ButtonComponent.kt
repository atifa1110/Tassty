package com.example.tassty.component

import android.graphics.drawable.Icon
import androidx.annotation.Size
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.toCleanRupiahFormat
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Neutral80
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink200
import com.example.tassty.ui.theme.Pink50
import com.example.tassty.ui.theme.Pink500

@Composable
fun ButtonComponent(
    enabled : Boolean,
    @StringRes labelResId: Int,
    onClick:() -> Unit,
) {
    Button(
        enabled = enabled,
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Orange500,
            contentColor = Color.White,
            disabledContentColor = Neutral100,
            disabledContainerColor = Neutral40
        )
    ) {
        Text(text = stringResource(labelResId),
            style=LocalCustomTypography.current.bodyMediumSemiBold
        )
    }
}

@Composable
fun ButtonSmallComponent(
    enabled : Boolean,
    @StringRes labelResId: Int,
    onClick:() -> Unit,
) {
    Button(
        enabled = enabled,
        onClick = onClick,
        modifier = Modifier
            .size(220.dp,60.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Orange500,
            contentColor = Color.White,
            disabledContentColor = Neutral100,
            disabledContainerColor = Neutral40
        )
    ) {
        Text(text = stringResource(labelResId),
            style=LocalCustomTypography.current.bodyMediumSemiBold
        )
    }
}

@Composable
fun ButtonLogout(
    enabled : Boolean,
    @StringRes labelResId: Int,
    onClick:() -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Button(
            enabled = enabled,
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Pink50,
                contentColor = Pink500,
                disabledContentColor = Neutral100,
                disabledContainerColor = Neutral40
            )
        ) {
            Text(
                text = stringResource(labelResId),
                style = LocalCustomTypography.current.bodyMediumSemiBold
            )
        }
    }
}

@Composable
fun ButtonLogin(){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.google_logo), // Ganti dengan resource logo
            contentDescription = "Login with Google",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Image(
            painter = painterResource(id = R.drawable.apple_logo),
            contentDescription = "Login with Apple",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Image(
            painter = painterResource(id = R.drawable.facebook_logo),
            contentDescription = "Login with Facebook",
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
fun CartAddItemButton(
    totalPrice: Int,
    itemCount: Int
){
    Surface(
        shape = RoundedCornerShape(100.dp),
        color = Orange500,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 19.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(
                    painter = painterResource(R.drawable.shopping_bag),
                    contentDescription = "Shopping Cart",
                    tint = Neutral10
                )
                Text(
                    text = "Cart",
                    color = Neutral10, // Warna teks putih
                    style = LocalCustomTypography.current.bodyMediumBold
                )

                Text(
                    text = " • ",
                    style = LocalCustomTypography.current.bodySmallMedium,
                    color = Neutral10
                )

                Text(
                    text = "$itemCount Item",
                    color = Color.White, // Warna teks putih
                    style = LocalCustomTypography.current.bodyMediumMedium
                )
            }
            FoodPriceText(
                price = totalPrice.toCleanRupiahFormat(),
                color = Neutral10
            )
        }
    }
}

@Composable
fun CartAddButton(
    totalPrice: Int,
    onClick: () -> Unit
){
    Surface(
        shape = RoundedCornerShape(100.dp),
        color = Orange500,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight().clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 19.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(
                    painter = painterResource(R.drawable.shopping_bag),
                    contentDescription = "Shopping Cart",
                    tint = Neutral10
                )
                Text(
                    text = "Add to Cart",
                    color = Neutral10, // Warna teks putih
                    style = LocalCustomTypography.current.bodyMediumBold
                )
            }
            FoodPriceText(
                price = totalPrice.toCleanRupiahFormat(),
                color = Neutral10
            )
        }
    }
}

@Composable
fun QuantityButton(
    onClick: () -> Unit,
    enabled: Boolean,
    icons : ImageVector,
    contentDescription: String
){
    Button(
        onClick = onClick,
        enabled = enabled, // Disable when quantity is 1
        // Make it small and circular (similar to the image)
        modifier = Modifier.size(30.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Neutral80,
            contentColor = Color.White
        ),
        shape = CircleShape
    ) {
        Icon(imageVector = icons, contentDescription = contentDescription)
    }
}


@Composable
fun QuantitySmallButton(
    onClick: () -> Unit,
    enabled: Boolean,
    icons : ImageVector,
    contentDescription: String
){
    Button(
        onClick = onClick,
        enabled = enabled, // Disable when quantity is 1
        // Make it small and circular (similar to the image)
        modifier = Modifier.size(24.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if(enabled) Neutral80 else Neutral70,
            contentColor = Color.White
        ),
        shape = CircleShape
    ) {
        Icon(imageVector = icons, contentDescription = contentDescription,
            modifier= Modifier.size(12.dp))
    }
}

@Composable
fun QuantityCartContent(
    itemCount: Int,
    enabled: Boolean,
    onIncrement:() -> Unit,
    onDecrement:() -> Unit
){
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Decrease Button
        QuantitySmallButton(
            onClick = onDecrement,
            enabled = enabled,
            icons = Icons.Filled.Remove,
            contentDescription = "Decrease Quantity"
        )

        // Quantity Text
        Text(
            text = itemCount.toString(),
            style = LocalCustomTypography.current.h6Regular,
            color = Neutral100,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        // Increase Button
        QuantitySmallButton(
            onClick = onIncrement,
            enabled = true,
            icons = Icons.Filled.Add,
            contentDescription = "Increase Quantity"
        )
    }
}

@Composable
fun FloatingAddButton(
    actionSize: Dp,
    iconSize: Dp,
    onClick: () -> Unit,
    modifier : Modifier = Modifier
){
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(actionSize),
        shape = CircleShape,
        containerColor = Neutral100,
        contentColor = Color.White
    ) {
        Icon(imageVector = Icons.Default.Add,
            contentDescription = "Add to cart",
            modifier = Modifier.size(iconSize))
    }
}

@Composable
fun NotesBoxButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Neutral10)
            .border(
                border = BorderStroke(1.dp, Color(0xFFDEDEDE)),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.pencil_alt),
            contentDescription = "Give rating",
            tint = Neutral100
        )

        Spacer(Modifier.width(4.dp))

        Text(
            text = title,
            style = LocalCustomTypography.current.h8Regular,
            color = Neutral100
        )
    }
}



@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    isWishlist: Boolean,
    onClick: () -> Unit
){
    var borderColor = if(isWishlist) Color.Transparent else Pink200
    var iconColor = if(isWishlist) Neutral10 else Pink500
    var buttonColor = if(isWishlist) Pink500 else Neutral10

    CircleImageIcon(
        boxColor = buttonColor,
        icon = R.drawable.heart,
        iconColor = iconColor,
        iconSize = 16.dp,
        contentDescription = if (isWishlist) {
            "Remove from Favorites"
        } else {
            "Add to Favorites"
        },
        modifier = modifier
            .size(32.dp).border(0.5.dp, borderColor, CircleShape)
            .clickable { onClick() },
    )
}

@Composable
fun RankBadgeIcon(
    modifier: Modifier = Modifier
){
    Row (
        modifier = modifier
            .background(
                color = Color(0xFFEE8E1E).copy(0.8f),
                shape = RoundedCornerShape(20.dp)
            ).padding(10.dp,4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ){
        Icon(
            painter = painterResource(R.drawable.crown), // Ganti dengan ikon yang sesuai
            contentDescription = "Rank Icon",
            tint = Color.White,
            modifier = Modifier.size(16.dp) // Ukuran ikon
        )

        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = LocalCustomTypography.current.bodyXtraSmallSemiBold.toSpanStyle()
                        .copy(color = Color.White)
                ) {
                    append("#")
                }
                withStyle(
                    style = LocalCustomTypography.current.bodySmallSemiBold.toSpanStyle()
                        .copy(color = Color.White)
                ) {
                    append("1")
                }
            },
            textAlign = TextAlign.Center,
            color = Color.White,
            style = LocalCustomTypography.current.bodySmallSemiBold,
        )
    }
}

@Composable
fun RankBadge(
    horizontal : Dp,
    vertical : Dp,
    modifier: Modifier = Modifier
){
    Text(
        text = buildAnnotatedString {
            withStyle(style = LocalCustomTypography.current.bodyXtraSmallSemiBold.toSpanStyle().copy(color = Color.White)) {
                append("#")
            }
            withStyle(style = LocalCustomTypography.current.bodySmallSemiBold.toSpanStyle().copy(color = Color.White)) {
                append("1")
            }
        },
        textAlign = TextAlign.Center,
        color = Color.White,
        style = LocalCustomTypography.current.bodySmallSemiBold,
        modifier = modifier
            .background(
                color = Color(0xFFEE8E1E).copy(0.8f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = horizontal, vertical = vertical)
    )
}

@Composable
fun TextButton(
    text : String,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    TextButton(
        onClick = onClick,
        modifier = modifier,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = text,
            style = LocalCustomTypography.current.bodyMediumMedium,
            color = textColor
        )
    }
}

@Composable
fun ResetButton(
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(44.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFDEDEDE)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Neutral10, // 2. Set the background color (Neutral10)
            contentColor = Neutral70 // Set the color for the Text inside
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Reset",
            style = LocalCustomTypography.current.bodySmallSemiBold
        )
    }
}