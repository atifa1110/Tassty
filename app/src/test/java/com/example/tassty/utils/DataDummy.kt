package com.example.tassty.utils

import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.CartGroup
import com.example.core.domain.model.CartItem
import com.example.tassty.util.addressesDomain
import com.example.tassty.util.restaurants
import com.example.tassty.util.voucherDomain

object DataDummy {

    val cartDomain =
        CartGroup(
            restaurant = restaurants[0],
            menus = listOf(
                CartItem(
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
                    customizable = false
                )
            )
        )


    val addressResponseSuccess = TasstyResponse.Success(
        data = addressesDomain,
        meta = Meta(code = 200, status = "success", message = "")
    )

    val voucherResponseSuccess = TasstyResponse.Success(
        data = voucherDomain,
        meta = Meta(code = 200, status = "success", message = "")
    )

    val orderResponseSuccess = TasstyResponse.Success(
        data = "ORDER-123",
        meta = Meta(code = 200, status = "success", message = "Order Success")
    )
}