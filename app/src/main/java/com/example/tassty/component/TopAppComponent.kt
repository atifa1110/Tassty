package com.example.tassty.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.core.ui.model.UserUiModel
import com.example.tassty.R
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500
import io.getstream.chat.android.models.User

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
            .fillMaxWidth().statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@Composable
fun LogoTopAppBar(){
    CustomBarStart() {
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
    CircleImageIcon(
        boxColor = boxColor,
        icon = icon,
        iconSize = 20.dp,
        iconColor = iconColor,
        contentDescription = "top app bar icon",
        modifier = Modifier
            .size(44.dp)
            .clickable(onClick = onClick)
    )
}

@Composable
fun BorderTopBarButton(
    icon: Any,
    boxColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    CircleImageIcon(
        boxColor = boxColor,
        icon = icon,
        iconSize = 20.dp,
        iconColor = iconColor,
        contentDescription = "top app bar icon",
        modifier = Modifier
            .size(44.dp)
            .border(width = 1.dp, color = LocalCustomColors.current.topBarBorder,
                shape = CircleShape)
            .clickable(onClick = onClick)
    )
}


@Composable
fun AuthTopAppBar(){
    CustomBarSpaceBetween(modifier = Modifier.statusBarsPadding()) {
        LogoImage()
        TopBarButton(
            icon = R.drawable.question_mark_circle,
            boxColor = LocalCustomColors.current.topBarBackgroundColor,
            iconColor = LocalCustomColors.current.iconFocused
        ) { }
    }
}

@Composable
fun BackTopAppBar(
    onBackClick : () -> Unit
) {
    CustomBarSpaceBetween(modifier = Modifier.statusBarsPadding()) {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = LocalCustomColors.current.topBarBackgroundColor,
            iconColor = LocalCustomColors.current.iconFocused
        ) { onBackClick() }
    }
}

@Composable
fun ProfileTopAppBar(
    onEditClick: () -> Unit
) {
    CustomBarSpaceBetween {
        Row{
            Text(
                text = "Profile",
                style = LocalCustomTypography.current.h3Bold,
                color = LocalCustomColors.current.headerText
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
                boxColor = LocalCustomColors.current.topBarBackgroundColor,
                iconColor =LocalCustomColors.current.iconFocused,
                onClick = onEditClick
            )
            Spacer(Modifier.width(8.dp))
            TopBarButton(
                icon = R.drawable.setting,
                boxColor = LocalCustomColors.current.topBarBackgroundColor,
                iconColor = LocalCustomColors.current.iconFocused,
                onClick = {}
            )
        }

    }
}

@Composable
fun CollectionTopAppBar(
    onBackClick: () -> Unit,
    onAddClick: () -> Unit
) {
    CustomBarSpaceBetween(modifier = Modifier.statusBarsPadding()) {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = LocalCustomColors.current.topBarBackgroundColor,
            iconColor = LocalCustomColors.current.iconFocused
        ) { onBackClick() }

        Row (horizontalArrangement = Arrangement.spacedBy(8.dp)){
            TopBarButton(
                icon = R.drawable.search,
                boxColor = LocalCustomColors.current.topBarBackgroundColor,
                iconColor = LocalCustomColors.current.iconFocused
            ) { }

            TopBarButton(icon = R.drawable.add,
                boxColor =  Orange500, iconColor = Neutral10
            ) {  onAddClick() }
        }
    }
}

@Composable
fun CollectionDetailTopAppBar(
    iconBackground: Color,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    CustomBarSpaceBetween(modifier = modifier) {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = iconBackground,
            iconColor = LocalCustomColors.current.iconFocused
        ) { onBackClick() }

        Row (horizontalArrangement = Arrangement.spacedBy(8.dp)){
            TopBarButton(
                icon = R.drawable.pencil,
                boxColor = iconBackground,
                iconColor = LocalCustomColors.current.iconFocused
            ) {onEditClick() }

            TopBarButton(icon = R.drawable.trash,
                boxColor =  Pink500, iconColor = Neutral10
            ) {  onRemoveClick() }
        }
    }
}

@Composable
fun CategoryTopAppBar(
    modifier: Modifier = Modifier,
    iconBackground: Color,
    onBackClick : () -> Unit,
    onFilterClick : () -> Unit
) {
    CustomBarSpaceBetween(modifier = modifier) {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = iconBackground, iconColor = LocalCustomColors.current.iconFocused
        ) { onBackClick() }

        TopBarButton(icon = R.drawable.filter,
            boxColor =  Orange500, iconColor = Neutral10
        ) { onFilterClick() }
    }
}

@Composable
fun OrderDetailAppBar(
    isReviewId: Boolean = false,
    onBackClick : () -> Unit,
    onEditClick : () -> Unit
) {
    CustomBarSpaceBetween(modifier = Modifier.statusBarsPadding()) {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = LocalCustomColors.current.topBarBackgroundColor,
            iconColor =LocalCustomColors.current.iconFocused
        ) { onBackClick() }

        if(!isReviewId) {
            TopBarButton(
                icon = R.drawable.pencil_alt,
                boxColor = Orange500,
                iconColor = Neutral10
            ) { onEditClick() }
        }
    }
}

@Composable
fun AddTopAppBar(
    onBackClick : () -> Unit,
    onAddClick : () -> Unit
) {
    CustomBarSpaceBetween(modifier = Modifier.statusBarsPadding()) {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = LocalCustomColors.current.topBarBackgroundColor,
            iconColor = LocalCustomColors.current.iconFocused
        ) { onBackClick() }

        TopBarButton(icon = R.drawable.add,
            boxColor =  Orange500, iconColor = Neutral10
        ) { onAddClick() }
    }
}

@Composable
fun AddCardTopAppBar(
    onBackClick : () -> Unit,
    onAddClick : () -> Unit
) {
    CustomBarSpaceBetween(modifier = Modifier.statusBarsPadding()) {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = LocalCustomColors.current.topBarBackgroundColor,
            iconColor = LocalCustomColors.current.iconFocused
        ) { onBackClick() }

        TopBarButton(icon = Icons.Default.CameraAlt,
            boxColor =  Orange500, iconColor = Neutral10
        ) { onAddClick }
    }
}


@Composable
fun CartTopAppBar(
    modifier: Modifier = Modifier,
    onDeleteClick:() -> Unit
) {
    CustomBarSpaceBetween(modifier = modifier) {
        CustomTwoColorText(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            fullText = "My cart.",
            highlightText = ".",
            textColor = LocalCustomColors.current.headerText,
            normalStyle = LocalCustomTypography.current.h3Bold,
            textAlign = TextAlign.Start
        )

        TopBarButton(icon = R.drawable.trash,
            boxColor =  Pink500, iconColor = Neutral10
        ) { onDeleteClick() }
    }
}

@Composable
fun ChatTopAppBar(
    modifier: Modifier = Modifier,
    onDeleteClick:() -> Unit
) {
    CustomBarSpaceBetween(modifier = modifier) {
        Text(
            text = buildAnnotatedString {
                append("Messages")
                withStyle(style = SpanStyle(color = Orange500)) {
                    append(".")
                }
            },
            style = LocalCustomTypography.current.h3Bold,
            color = LocalCustomColors.current.headerText
        )

        Row (horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TopBarButton(
                icon = R.drawable.search,
                boxColor = LocalCustomColors.current.topBarBackgroundColor,
                iconColor = LocalCustomColors.current.iconFocused
            ) { onDeleteClick() }

            TopBarButton(
                icon = R.drawable.trash,
                boxColor = Pink500, iconColor = Neutral10
            ) { onDeleteClick() }
        }
    }
}

@Composable
fun SetupTopAppBar(
    currentStep: Int,
    totalStep: Int,
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    CustomBarSpaceBetween(Modifier.statusBarsPadding()) {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = LocalCustomColors.current.cardBackground,
            iconColor = LocalCustomColors.current.iconFocused
        ) { onBackClick() }
        
        StepIndicatorText(currentStep,totalStep)

        Text(
            text = "Skip",
            style = LocalCustomTypography.current.bodyMediumMedium,
            color = LocalCustomColors.current.text,
            modifier = Modifier.clickable { onSkipClick() }
        )
    }
}

@Composable
fun MapSearchTopAppBar(
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    CustomBarSpaceBetween(modifier = Modifier.statusBarsPadding()) {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = LocalCustomColors.current.topBarBackgroundColor, iconColor = LocalCustomColors.current.iconFocused
        ) { onBackClick() }

        TopBarButton(icon = Icons.Default.Search,
            boxColor = LocalCustomColors.current.topBarBackgroundColor, iconColor = LocalCustomColors.current.iconFocused
        ) { onSearchClick() }
    }
}

@Composable
fun FavoriteTopAppBar(
    modifier: Modifier = Modifier,
    iconBackground: Color,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    CustomBarSpaceBetween (modifier = modifier){
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = iconBackground,
            iconColor = LocalCustomColors.current.iconFocused
        ) { onBackClick() }

        TopBarButton(icon = Icons.Default.Search,
            boxColor = iconBackground,
            iconColor = LocalCustomColors.current.iconFocused
        ) { onSearchClick() }
    }
}

@Composable
fun MyOrderTopAppBar(
    onBackClick: () -> Unit,
    onCalendarCLick: () -> Unit
) {
    CustomBarSpaceBetween(Modifier.statusBarsPadding()) {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = LocalCustomColors.current.topBarBackgroundColor,
            iconColor = LocalCustomColors.current.iconFocused
        ) { onBackClick() }

        TopBarButton(icon = R.drawable.calendar,
            boxColor = Orange500, iconColor = Neutral10
        ) { onCalendarCLick()}
    }
}

@Composable
fun TitleTopAppBar(
    title : String,
    onBackClick:() -> Unit,
    onFilterClick:() -> Unit
){
    CustomBarSpaceBetween(modifier = Modifier.statusBarsPadding()) {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = LocalCustomColors.current.cardBackground,
            iconColor = LocalCustomColors.current.iconFocused
        ) { onBackClick() }

        Text(
            text = title,
            style = LocalCustomTypography.current.h5Bold,
            color = LocalCustomColors.current.headerText
        )

        TopBarButton(icon = R.drawable.filter,
            boxColor =  Orange500, iconColor = Neutral10
        ) { onFilterClick() }
    }
}

@Composable
fun DetailTopAppBar(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    iconBackground: Color,
    onShowSearch : () -> Unit,
    onBackClick:() -> Unit,
    onFavoriteClick:() -> Unit,
    onShareClick:() -> Unit
){
    CustomBarSpaceBetween(modifier = modifier) {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = iconBackground, iconColor = LocalCustomColors.current.iconFocused
        ) { onBackClick() }

        Row (horizontalArrangement = Arrangement.spacedBy(8.dp)){
            TopBarButton(
                icon = R.drawable.search,
                boxColor = iconBackground, iconColor = LocalCustomColors.current.iconFocused
            ) { onShowSearch() }

            TopBarButton(
                icon = R.drawable.heart,
                boxColor = if(isFavorite) Pink500 else iconBackground,
                iconColor = if(isFavorite) Neutral10 else Pink500
            ) { onFavoriteClick() }

            TopBarButton(
                icon = R.drawable.share,
                boxColor = iconBackground,
                iconColor = LocalCustomColors.current.iconFocused
            ) { onShareClick() }
        }
    }
}

@Composable
fun DetailMenuTopAppBar(
    modifier: Modifier = Modifier,
    iconBackground: Color,
    isFavorite: Boolean,
    onBackClick:() -> Unit,
    onFavoriteClick:() -> Unit,
    onShareClick:() -> Unit
){
    CustomBarSpaceBetween(modifier = modifier) {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = iconBackground, iconColor = LocalCustomColors.current.iconFocused
        ) { onBackClick() }

        Row (horizontalArrangement = Arrangement.spacedBy(8.dp)){

            TopBarButton(
                icon = R.drawable.heart,
                boxColor = if(isFavorite) Pink500 else iconBackground,
                iconColor = if(isFavorite) Neutral10 else Pink500
            ) { onFavoriteClick() }

            TopBarButton(
                icon = R.drawable.share,
                boxColor = iconBackground, iconColor =LocalCustomColors.current.iconFocused
            ) { onShareClick() }
        }
    }
}

@Composable
fun ChatTopBar(
    user: User,
    onBackClick: () -> Unit,
    onCameraClick: () -> Unit,
    onCallClick: () -> Unit,
) {
    CustomBarSpaceBetween(Modifier.background(LocalCustomColors.current.modalBackgroundFrame)) {
        BorderTopBarButton(
            icon = Icons.Default.ArrowBackIosNew,
            onClick = onBackClick,
            boxColor = LocalCustomColors.current.cardBackground,
            iconColor = LocalCustomColors.current.iconFocused,
        )
        Spacer(modifier = Modifier.width(12.dp))

        Box(modifier = Modifier.size(44.dp)) {
            AsyncImage(
                model = user.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Neutral40),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Blue500)
                    .align(Alignment.BottomStart)
                    .padding(3.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delivery),
                    contentDescription = null,
                    tint = Neutral10,
                    modifier = Modifier.fillMaxSize()
                )
            }

            if(user.online) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Neutral10)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(Green500)
                        .align(Alignment.TopEnd)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.name,
                style = LocalCustomTypography.current.h5Bold,
                color = LocalCustomColors.current.headerText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if(user.online) "Online" else "Offline",
                    style = LocalCustomTypography.current.bodyXtraSmallBold,
                    color = if(user.online) Green500 else LocalCustomColors.current.text,
                )
                Text(
                    text = if(user.extraData["user_role"] == "driver") " • Driver" else " • User",
                    style = LocalCustomTypography.current.bodyXtraSmallMedium,
                    color = LocalCustomColors.current.text
                )
            }
        }

        Row (horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BorderTopBarButton(
                icon = Icons.Default.CameraAlt,
                onClick = onCameraClick,
                boxColor = LocalCustomColors.current.cardBackground,
                iconColor = LocalCustomColors.current.iconFocused
            )

            BorderTopBarButton(
                icon = Icons.Default.Phone,
                onClick = onCallClick,
                boxColor = LocalCustomColors.current.cardBackground,
                iconColor = LocalCustomColors.current.iconFocused
            )
        }


    }
}

@Composable
fun DetailVoucherTopAppBar(
    onBackClick: () -> Unit,
    onCalendarCLick: () -> Unit
) {
    CustomBarSpaceBetween {
        TopBarButton(icon = R.drawable.arrow_left,
            boxColor = LocalCustomColors.current.topBarBackgroundColor,
            iconColor = LocalCustomColors.current.iconFocused
        ) { onBackClick() }

        TopBarButton(icon = R.drawable.dots_horizontal,
            boxColor = LocalCustomColors.current.topBarBackgroundColor,
            iconColor = LocalCustomColors.current.iconFocused
        ) { onCalendarCLick()}
    }
}

//@Preview(showBackground = true)
//@Composable
//fun TopAppBarPreview() {
//    Column(
//        verticalArrangement = Arrangement.spacedBy(10.dp)
//    ) {
//        CollectionTopAppBar(onBackClick = {}, onAddClick = {})
//        CollectionDetailTopAppBar(onBackClick = {}, onEditClick = {}, onRemoveClick = {})
//        AuthTopAppBar()
//        BackTopAppBar(onBackClick = {})
//        CategoryTopAppBar (onBackClick = {}, onFilterClick = {})
//        SetupTopAppBar(1,2, onBackClick = {}, onSkipClick = {})
//        MapSearchTopAppBar(onBackClick = {}) { }
//        TitleTopAppBar(title = "Recommended Restaurant",onBackClick = {}) { }
//        DetailTopAppBar(isFavorite = false, onShowSearch = {}, onShareClick = {}, onFavoriteClick = {}, onBackClick = {})
//        DetailMenuTopAppBar(isFavorite = false, onShareClick = {}, onFavoriteClick = {}, onBackClick = {})
//    }
//}