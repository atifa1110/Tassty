package com.example.core.ui.model

data class NotificationUiModel(
    val id: String,
    val title: String, // Contoh: "Your order has been taken..."
    val description: String, // Contoh: "Lorem ipsum dolor sit amet"
    val displayTime: String, // Contoh: "15.00 PM"
    val displayHeader:String,
    val isRead: Boolean, // Untuk nampilin titik oranye di pojok kanan bawah
    val type: NotificationType
)

enum class NotificationType {
    ORDER,    // Ikon Motor (Hijau Muda)
    DISCOUNT, // Ikon Persen (Oranye Muda)
    PROMO,    // Ikon User/Invite (Ungu Muda)
    TRANSACTION // ikon transaction
}