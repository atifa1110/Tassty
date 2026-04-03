package com.example.core.ui.model

data class NotificationUiModel(
    val id: String,
    val title: String,
    val message: String,
    val displayTime: String,
    val displayDate:String,
    val isRead: Boolean,
    val type: NotificationType
)

enum class NotificationType {
    ORDER,    // Ikon Motor (Hijau Muda)
    DISCOUNT, // Ikon Persen (Oranye Muda)
    PROMO,    // Ikon User/Invite (Ungu Muda)
    TRANSACTION // ikon transaction
}