package com.example.tassty.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.model.SearchUiState
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500

@Composable
fun CustomBarSpaceBetween(
    modifier : Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@Composable
fun CustomBarStart(
    content: @Composable RowScope.() -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@Composable
fun LogoTopAppBar(){
    CustomBarStart {
        LogoImage()
    }
}


@Composable
fun TopBarButton(
    icon: Any,
    boxColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clickable(onClick = onClick)
            .clip(CircleShape)
            .background(boxColor) ,
        contentAlignment = Alignment.Center
    ) {
        val painter = when (icon) {
            is androidx.compose.ui.graphics.vector.ImageVector -> rememberVectorPainter(icon)
            is Int -> painterResource(icon)
            else -> return // Handle case not supported
        }
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painter,
            contentDescription = null,
            tint = iconColor
        )
    }
}


@Composable
fun AuthTopAppBar(){
    CustomBarSpaceBetween {
        LogoImage()
        TopBarButton(
            icon = R.drawable.question_mark_circle,
            boxColor = Neutral20, iconColor = Neutral100
        ) { }
    }
}

@Composable
fun BackTopAppBar(
    onBackClick : () -> Unit
) {
    CustomBarSpaceBetween {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = Neutral20, iconColor = Neutral100
        ) { onBackClick() }
    }
}

@Composable
fun ProfileTopAppBar(
) {
    CustomBarSpaceBetween {
        Row{
            Text(
                text = "Profile",
                style = LocalCustomTypography.current.h3Bold,
                color = Neutral100
            )
            Text(
                text = ".",
                style = LocalCustomTypography.current.h3Bold,
                color = Orange500
            )
        }
        Row {
            TopBarButton(
                icon = R.drawable.pencil,
                boxColor = Neutral20, iconColor = Neutral100
            ) { }
            Spacer(Modifier.width(8.dp))
            TopBarButton(
                icon = R.drawable.setting,
                boxColor = Neutral20, iconColor = Neutral100
            ) { }
        }

    }
}

@Composable
fun CategoryTopAppBar(
    modifier: Modifier = Modifier,
    onBackClick : () -> Unit,
    onFilterClick : () -> Unit
) {
    CustomBarSpaceBetween(modifier = modifier) {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = Neutral10.copy(0.9f), iconColor = Neutral100
        ) { onBackClick() }

        TopBarButton(icon = R.drawable.filter,
            boxColor =  Orange500, iconColor = Neutral10
        ) { onFilterClick }
    }
}

@Composable
fun CartTopAppBar(
    modifier: Modifier = Modifier,
) {
    CustomBarSpaceBetween(modifier = modifier) {
        Row {
            Text(
                text = "My cart",
                style = LocalCustomTypography.current.h3Bold,
                color = Neutral100
            )
            Text(
                text = ".",
                style = LocalCustomTypography.current.h3Bold,
                color = Orange500
            )
        }

        TopBarButton(icon = R.drawable.trash,
            boxColor =  Pink500, iconColor = Neutral10
        ) {  }
    }
}


@Composable
fun SetupTopAppBar(
    currentStep: Int,
    totalStep: Int,
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    CustomBarSpaceBetween {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = Neutral20, iconColor = Neutral100
        ) { onBackClick() }
        
        StepIndicator(currentStep,totalStep)

        Text(
            text = "Skip",
            style = LocalCustomTypography.current.bodyMediumMedium,
            color = Neutral70,
            modifier = Modifier.clickable { onSkipClick() }
        )
    }
}

@Composable
fun MapSearchTopAppBar(
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    CustomBarSpaceBetween {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = Neutral10, iconColor = Neutral100
        ) { onBackClick() }

        TopBarButton(icon = Icons.Default.Search,
            boxColor = Neutral10, iconColor = Neutral100
        ) { onSearchClick() }
    }
}

@Composable
fun TitleTopAppBar(
    title : String,
    onBackClick:() -> Unit,
    onFilterClick:() -> Unit
){
    CustomBarSpaceBetween {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = Neutral20, iconColor = Neutral100
        ) { onBackClick() }

        Text(
            text = title,
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100,
        )

        TopBarButton(icon = R.drawable.filter,
            boxColor =  Orange500, iconColor = Neutral10
        ) { onFilterClick() }
    }
}

@Composable
fun DetailTopAppBar(
    onShowSearch : () -> Unit,
    onBackClick:() -> Unit,
    onFavoriteClick:() -> Unit,
    onShareClick:() -> Unit
){
    CustomBarSpaceBetween {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = Neutral20.copy(0.9f), iconColor = Neutral100
        ) { onBackClick() }

        Row (horizontalArrangement = Arrangement.spacedBy(8.dp)){
            TopBarButton(
                icon = R.drawable.search,
                boxColor = Neutral10, iconColor = Neutral100
            ) { onShowSearch() }

            TopBarButton(
                icon = R.drawable.heart,
                boxColor = Neutral10, iconColor = Pink500
            ) { onFavoriteClick() }

            TopBarButton(
                icon = R.drawable.share,
                boxColor = Neutral10, iconColor = Neutral100
            ) { onShareClick() }
        }
    }
}

@Composable
fun DetailMenuTopAppBar(
    onBackClick:() -> Unit,
    onFavoriteClick:() -> Unit,
    onShareClick:() -> Unit
){
    CustomBarSpaceBetween {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = Neutral20.copy(0.9f), iconColor = Neutral100
        ) { onBackClick() }

        Row (horizontalArrangement = Arrangement.spacedBy(8.dp)){

            TopBarButton(
                icon = R.drawable.heart,
                boxColor = Neutral10, iconColor = Pink500
            ) { onFavoriteClick() }

            TopBarButton(
                icon = R.drawable.share,
                boxColor = Neutral10, iconColor = Neutral100
            ) { onShareClick() }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopAppBarPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AuthTopAppBar()
        BackTopAppBar(onBackClick = {})
        CategoryTopAppBar (onBackClick = {}, onFilterClick = {})
        SetupTopAppBar(1,2, onBackClick = {}, onSkipClick = {})
        MapSearchTopAppBar(onBackClick = {}) { }
        TitleTopAppBar(title = "Recommended Restaurant",onBackClick = {}) { }
        DetailTopAppBar(onShowSearch = {}, onShareClick = {}, onFavoriteClick = {}, onBackClick = {})
        DetailMenuTopAppBar(onShareClick = {}, onFavoriteClick = {}, onBackClick = {})
    }
}