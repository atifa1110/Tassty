package com.example.core.ui.mapper

import com.example.core.utils.DateFormatter
import com.example.core.domain.model.Order
import com.example.core.domain.model.OrderItem
import com.example.core.domain.utils.toCleanRupiahFormat
import com.example.core.ui.model.OrderItemUiModel
import com.example.core.ui.model.OrderStatus
import com.example.core.ui.model.OrderUiModel
import java.util.Locale

fun Order.toUiModel(): OrderUiModel {
    val dateResult = DateFormatter.getHeaderTimeAndDate(this.createdAt)
    return OrderUiModel(
        id = this.id,
        orderNumber = this.orderNumber,
        restaurantName = this.restaurantName,
        restaurantImage = this.restaurantImage,
        status = this.status,
        finalAmount = this.finalAmount.toCleanRupiahFormat(),
        queueNumber = String.format(Locale.US,"#%03d", this.queueNumber),
        displayHeader = dateResult.header,
        displayTime = dateResult.time,
        orderDate = dateResult.localDate
    )
}

fun OrderItem.toUiModel(): OrderItemUiModel{
    val combinedSummary = listOfNotNull(
        this.options.takeIf { it.isNotBlank() },
        this.notes.takeIf { it.isNotBlank() }
    ).joinToString("\n")

    return OrderItemUiModel(
        id = this.id,
        menuReviewId = this.menuReviewId,
        quantity= "x${this.quantity}",
        menuName = this.menuName,
        imageUrl = this.imageUrl,
        notesSummary = combinedSummary.ifBlank { "Notes: -" }
    )
}