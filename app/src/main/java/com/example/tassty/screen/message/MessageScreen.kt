package com.example.tassty.screen.message

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.MessageUiModel
import com.example.core.ui.model.OrderStatus
import com.example.tassty.R
import com.example.tassty.component.ChatBubble
import com.example.tassty.component.ChatTopBar
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.DateHeader
import com.example.tassty.component.MessageImageContent
import com.example.tassty.component.MessageInputBar
import com.example.tassty.component.OrderCard
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.dummyMessages
import com.example.tassty.util.orderList
import io.getstream.chat.android.models.User

@Composable
fun MessageScreen(
    onNavigateBack:() -> Unit,
    viewModel: MessageViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.events.collect { event ->
            when(event){
                is MessageEvent.ShowMessage -> {
                    Toast.makeText(context,event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult (
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            viewModel.onImageSelected(uri)
        }
    )

    MessageContent(
        uiState = uiState,
        onTextChanged = viewModel::onTextChange,
        onSendMessage = viewModel::onSendMessage,
        onNavigateBack = onNavigateBack,
        onAttachClick = {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onCancelClick = viewModel::onImageClose,
        onImageClick = viewModel::onImageClick
    )

    CustomBottomSheet(
        visible = uiState.isImageSheetVisible,
        dismissOnClickOutside = true,
        onDismiss = viewModel::onDismissImageSheet
    ) {
        MessageImageContent(
            message = uiState.selectedMessage,
            status = "Seen",
        )
    }
}

@Composable
fun MessageContent(
    uiState: MessageUiState,
    onTextChanged:(String) -> Unit,
    onSendMessage:() -> Unit,
    onNavigateBack:() -> Unit,
    onAttachClick:() -> Unit,
    onCancelClick: () -> Unit,
    onImageClick: (MessageUiModel) -> Unit
) {
    val order = uiState.order.data
    Scaffold (
        containerColor = LocalCustomColors.current.background,
        topBar = {
            Column(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
                ChatTopBar(
                    user = uiState.user,
                    onBackClick = onNavigateBack,
                    onCallClick = {},
                    onCameraClick = {}
                )

                HorizontalDivider(color = LocalCustomColors.current.divider)

                if (uiState.isLoading) {
                    if (uiState.uploadProgress in 0.01f..0.99f) {
                        val animatedProgress by animateFloatAsState(targetValue = uiState.uploadProgress)
                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp),
                            color = Orange500,
                            trackColor = Color.Transparent
                        )
                    } else {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp),
                            color = Orange500,
                            trackColor = Color.Transparent
                        )
                    }
                }

                if(order!= null){
                    OrderCard(order = order, onCardClick = {})
                }
            }
        },
        bottomBar = {
            val orderStatus = uiState.order.data?.status
            if (orderStatus != OrderStatus.COMPLETED) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth().background(LocalCustomColors.current.modalBackgroundFrame)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (uiState.isImagePreviewVisible && uiState.selectedImageUri != null) {
                        ImagePreviewCard(
                            uri = uiState.selectedImageUri,
                            onCancel = onCancelClick
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    MessageInputBar(
                        text = uiState.sendMessage,
                        placeholder = uiState.placeholder,
                        onTextChanged = onTextChanged,
                        onSendMessage = onSendMessage,
                        onAttachClick = onAttachClick
                    )
                }
            }else{
                Surface (
                    modifier = Modifier.fillMaxWidth(),
                    color = LocalCustomColors.current.cardBackground,
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = "This chat is closed because the order is completed.",
                        modifier = Modifier.padding(16.dp),
                        style = LocalCustomTypography.current.bodySmallRegular,
                        color = LocalCustomColors.current.text,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    ) { paddingValues ->
        Box (modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {

            Image(
                painter = painterResource(id = R.drawable.chat_background),
                contentDescription = "chat background",
                modifier = Modifier.fillMaxSize(),
                colorFilter = ColorFilter.tint(LocalCustomColors.current.cardBackground),
                contentScale = ContentScale.Crop
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = 24.dp, vertical = 12.dp
                ),
                reverseLayout = true
            ) {
                if (uiState.groupedMessages.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.belum_ada_percakapan),
                            style = LocalCustomTypography.current.bodyXtraSmallRegular,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = Neutral60
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }

                uiState.groupedMessages.forEach { (date,messagesInDate) ->
                    items(messagesInDate) { message ->
                        ChatBubble(
                            message = message,
                            onImageClick= { onImageClick(message)}
                        )
                    }
                    item { DateHeader(date = date) }
                }
            }
        }
    }
}

@Composable
fun ImagePreviewCard(
    uri: Uri?,
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Neutral40)
    ) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 8.dp)
                .size(24.dp)
                .background(Neutral70.copy(alpha = 0.6f), CircleShape)
                .clip(CircleShape)
                .clickable { onCancel() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cancel",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun MessageLightPreview(){
//    TasstyTheme {
//        MessageContent(
//            uiState = MessageUiState(
//                groupedMessages = dummyMessages.groupBy{it.date},
//                isLoading = false,
//                sendMessage = "",
//                order = Resource(data = orderList[1]),
//                selectedImageUri = null,
//                isImagePreviewVisible = false,
//                placeholder = "Add a caption...",
//                user = User(name = "Lucas", image = "", online = false)
//            ),
//            onTextChanged = {},
//            onSendMessage = {},
//            onNavigateBack = {},
//            onAttachClick = {},
//            onCancelClick = {},
//            onImageClick = {}
//        )
//    }
//}

//@Preview(showBackground = true, name = "Dark Mode")
//@Composable
//fun MessageDarkPreview(){
//    TasstyTheme (darkTheme = true){
//        MessageContent(
//            uiState = MessageUiState(
//                groupedMessages = dummyMessages.groupBy{it.date},
//                isLoading = false,
//                sendMessage = "",
//                order = Resource(data = orderList[0]),
//                selectedImageUri = null,
//                isImagePreviewVisible = false,
//                placeholder = "Add a caption...",
//                user = User(name = "Lucas", image = "", online = false)
//            ),
//            onTextChanged = {},
//            onSendMessage = {},
//            onNavigateBack = {},
//            onAttachClick = {},
//            onCancelClick = {},
//            onImageClick = {}
//        )
//    }
//}