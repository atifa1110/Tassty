package com.example.tassty.util

import com.example.core.ui.mapper.OrderFilterCategory
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.CartGroupUiModel
import com.example.core.ui.model.CartItemUiModel
import com.example.core.ui.model.DetailOrderUiModel
import com.example.core.ui.model.DriverUiModel
import com.example.core.ui.model.FilterOptionUi
import com.example.core.ui.model.OrderItemUiModel
import com.example.core.ui.model.OrderStatus
import com.example.core.ui.model.OrderUiModel
import com.example.core.ui.model.PaymentChannelUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.threeten.bp.LocalDate

object OrderPreviewData {
    val orderUiList: ImmutableList<OrderUiModel> = listOf(
        OrderUiModel(
            id="ORD-001",
            restaurantName = "Kopi Kenangan - Depok Town Square",
            status = OrderStatus.PENDING,
            displayHeader = "Today",
            displayTime = "09:35 AM",
            orderDate = LocalDate.of(2026,2,28),
            finalAmount = "Rp150.000",
            queueNumber = "#001",
            orderNumber = "T-1421423432d",
            restaurantImage = ""
        )
    ).toImmutableList()

    val paymentChannels: ImmutableList<PaymentChannelUiModel> = listOf(
        PaymentChannelUiModel(
            businessId = "PAY-001",
            channelCode = "BCA",
            name = "BCA virtual account",
            channelCategory = "Virtual Account",
            iconKey = "BCA",
            isEnabled = true,
            isSelected = false
        )
    ).toImmutableList()

    val listOrder : ImmutableList<OrderItemUiModel> = listOf(
        OrderItemUiModel(
            id = "ITEM-001",
            quantity = "x1",
            imageUrl = "",
            menuName = "Item 1",
            notesSummary = "Pilih Varian Kopi: Kenangan Blend\n" +
                    "Sugar Level: Normal Sugar\n" +
                    "Size: Iced Jumbo\n" +
                    "Milk Option: Fresh Milk\n" +
                    "Ice level: Less Ice\n" +
                    "Syrup: Hazelnut Syrup",
            menuReviewId = ""
        ),
        OrderItemUiModel(
            id = "ITEM-002",
            quantity = "x2",
            imageUrl = "",
            menuName = "item 2",
            notesSummary = "Note : -",
            menuReviewId = "menu-11"
        )
    ).toImmutableList()

    val detailOrder = DetailOrderUiModel(
        id = "DET-001",
        orderNumber = "T-12340002",
        userAddress = UserData.addresses[0],
        restaurant = RestaurantPreviewData.restaurants[0].toUiModel(),
        userId = "",
        status = OrderStatus.PLACED,
        totalPrice = 15000,
        deliveryFee = 0,
        discount = 10000,
        finalAmount = 140000,
        paymentStatus = "",
        cardPayment = UserData.cardList[0],
        createdAt = "Today, 09:35 AM",
        queueNumber = "#001",
        driver = DriverUiModel(id = "DRI-001", name = "Luchas",4.7, profileImage = ""),
        orderItems = listOrder,
        chatChannelId = "",
        restaurantReviewId = "RES-001"
    )

    val orderFilters: List<FilterOptionUi<OrderFilterCategory>> = listOf(
        FilterOptionUi(
            key = "all",
            label = "All",
            category = OrderFilterCategory.ALL,
            isSelected = true
        ),
        FilterOptionUi(
            key = "ongoing",
            label = "Ongoing",
            category = OrderFilterCategory.ONGOING
        ),
        FilterOptionUi(
            key = "completed",
            label = "Completed",
            category = OrderFilterCategory.COMPLETED
        )
    ).toImmutableList()

    val cartUiModel = CartGroupUiModel(
        restaurant = RestaurantPreviewData.restaurants[0].toUiModel(),
        menus = listOf(
            CartItemUiModel(
                cartId = "CART-001",
                menuId = "MEN-002",
                name = "Shabu Premium Set",
                imageUrl = "https://cdn.example.com/menu/shabu_premium.jpg",
                price =  150000,
                quantity = 1,
                options = "Pilih Varian Kopi: Kenangan Blend\n" +
                        "Sugar Level: No Sugar\n" +
                        "Size: Hot Regular\n" +
                        "Milk Option: Fresh Milk\n" +
                        "Ice level: Less Ice\n" +
                        "Syrup: Vanilla Syrup\n"+
                        "Topping: Whipped Cream Chocolate, Gula Aren",
                notes = "es nya dikurangi",
                formatOptions = "Pilih Varian Kopi: Kenangan Blend\n" +
                        "Sugar Level: No Sugar\n" +
                        "Size: Hot Regular\n" +
                        "Milk Option: Fresh Milk\n" +
                        "Ice level: Less Ice\n" +
                        "Syrup: Vanilla Syrup\n"+
                        "Topping: Whipped Cream Chocolate, Gula Aren\n"+
                        "Notes: es nya dikurangi",
                isSelected = true,
                isSwipeActionVisible = false,
                customizable = false,
                optionIds = listOf("OPT-001")
            )
        )
    )

}