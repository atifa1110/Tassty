package com.example.tassty.screen.message

import android.net.Uri
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.MessageUiModel
import com.example.core.ui.model.OrderUiModel
import io.getstream.chat.android.models.User
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf

data class MessageUiState(
    val groupedMessages: ImmutableMap<String, ImmutableList<MessageUiModel>> = persistentMapOf(),
    val order: Resource<OrderUiModel> = Resource(),
    val user : User = User(),
    val placeholder : String = "",
    val sendMessage: String = "",
    val selectedImageUri: Uri? = null,
    val isImagePreviewVisible: Boolean = false,
    val isImageSheetVisible: Boolean = false,
    val selectedMessage: MessageUiModel? = null,
    val isLoading: Boolean = false,
    val uploadProgress: Float = 0f
)

data class MessageInternalState(
    val placeholder : String = "",
    val sendMessage: String = "",
    val selectedImageUri: Uri? = null,
    val isImagePreviewVisible: Boolean = false,
    val isImageSheetVisible: Boolean = false,
    val selectedMessage: MessageUiModel? = null,
    val isLoading: Boolean = false,
    val progress: Float = 0f
)

sealed interface MessageEvent {
    data class ShowMessage(val message: String): MessageEvent
}