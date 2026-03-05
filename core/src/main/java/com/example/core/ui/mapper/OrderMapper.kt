package com.example.core.ui.mapper

import com.example.core.domain.model.Order
import com.example.core.domain.model.OrderItem
import com.example.core.domain.utils.getHeaderTimeAndDate
import com.example.core.domain.utils.toCleanRupiahFormat
import com.example.core.ui.model.OrderItemUiModel
import com.example.core.ui.model.OrderStatus
import com.example.core.ui.model.OrderUiModel
import java.util.Locale

fun Order.toUiModel(): OrderUiModel {
    val dateResult = this.createdAt.getHeaderTimeAndDate()
    return OrderUiModel(
        id = this.id,
        orderNumber = this.orderNumber,
        restaurantName = this.restaurantName,
        restaurantImage = this.restaurantImage,
        status = when (this.status.uppercase()) {
            "PENDING_PAYMENT" -> OrderStatus.PENDING
            "PLACED" -> OrderStatus.PLACED
            "PREPARING" -> OrderStatus.PREPARING
            "ON_DELIVERY" -> OrderStatus.ON_DELIVERY
            "COMPLETED" -> OrderStatus.COMPLETED
            "CANCELLED" -> OrderStatus.CANCELLED
            else -> OrderStatus.PENDING
        },
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
        quantity= this.quantity,
        menuName = this.menuName,
        notesSummary = combinedSummary.ifBlank { "Notes: -" }
    )
}