package com.example.tassty.utils

import com.example.core.data.model.AuthStatus
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.CartGroup
import com.example.core.domain.model.CartItem
import com.example.core.domain.model.Category
import com.example.core.domain.model.Menu
import com.example.core.domain.model.OtpTimer
import com.example.core.domain.model.Restaurant
import com.example.core.domain.model.Voucher
import com.example.core.ui.model.MenuUiModel
import com.example.tassty.util.MenuPreviewData
import com.example.tassty.util.RestaurantPreviewData
import com.example.tassty.util.UserData
import com.example.tassty.util.VoucherData
import kotlin.collections.listOf

object DataDummy {

    val cartDomain =
        CartGroup(
            restaurant = RestaurantPreviewData.restaurants[0],
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
                    customizable = false,
                    optionIds = listOf("OPT-001","OPT-002")
                ),
                CartItem(
                    cartId = "CART-002",
                    menuId = "MEN-005",
                    name = "Menu 2",
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
                    customizable = true,
                    optionIds = listOf("OPT-001","OPT-002")
                )
            )
        )


    val addressResponseSuccess = TasstyResponse.Success(
        data = UserData.addressesDomain,
        meta = Meta(code = 200, status = "success", message = "")
    )

    val voucherResponseSuccess = TasstyResponse.Success(
        data = VoucherData.voucherDomain,
        meta = Meta(code = 200, status = "success", message = "")
    )

    val orderResponseSuccess = TasstyResponse.Success(
        data = "ORDER-123",
        meta = Meta(code = 200, status = "success", message = "Order Success")
    )

    val dummyAuthStatus = AuthStatus(
        name = "Atifa Fiorenza",
        email = "atifa@example.com",
        profileImage = "https://photo.url",
        isDarkMode = true,
        isLoggedIn = true
    )

    val logoutResponseSuccess = TasstyResponse.Success(
        data = "Logout Success",
        meta = Meta(code = 200, status = "success", message = "")
    )

    val logoutResponseFailed = TasstyResponse.Error(
        meta = Meta(code = 400, status = "error", message = "Logout Failed")
    )

    val loginResponseSuccess = TasstyResponse.Success(
        data = "Login Success",
        meta = Meta(code = 200, status = "success", message = "")
    )

    val loginResponseFailed = TasstyResponse.Error(
        meta = Meta(code = 400, status = "error", message = "Login Failed")
    )

    val registerResponseSuccess = TasstyResponse.Success(
        data = OtpTimer(300,60),
        meta = Meta(code = 200, status = "success", message = "Register Success")
    )

    val registerResponseFailed = TasstyResponse.Error(
        meta = Meta(code = 400, status = "error", message = "Email address is already in used!")
    )

    val verificationResponseSuccess = TasstyResponse.Success(
        data = "Verification Success",
        meta = Meta(code = 200, status = "success", message = "Verification Success")
    )

    val verificationResponseFailed = TasstyResponse.Error(
        meta = Meta(code = 400, status = "error", message = "Verification failed")
    )

    val resendResponseSuccess = TasstyResponse.Success(
        data = "Resend Success",
        meta = Meta(code = 200, status = "success", message = "Resend Success")
    )

    val resendResponseFailed = TasstyResponse.Error(
        meta = Meta(code = 400, status = "error", message = "Resend failed")
    )

    val resetResponseSuccess = TasstyResponse.Success(
        data = "Reset Success",
        meta = Meta(code = 200, status = "success", message = "Reset Success")
    )

    val resetResponseFailed = TasstyResponse.Error(
        meta = Meta(code = 400, status = "error", message = "Reset failed")
    )

    val mockData = listOf(
        Category(id ="CAT-001", name = "Martabak", imageUrl = ""),
        Category(id="CAT-002", name = "Drink", imageUrl = "")
    )
    val categoryResponseSuccess = TasstyResponse.Success(
        data = mockData,
        meta = Meta(code = 200, status = "success", message = "Get Categories Success")
    )

    val categoryResponseError = TasstyResponse.Error(
        meta = Meta(code = 200, status = "success", message = "Network Error")
    )

    val menuResponseSuccess = TasstyResponse.Success(
        data = MenuPreviewData.menus,
        meta = Meta(code = 200, status = "success", message = "")
    )

    val restResponseSuccess = TasstyResponse.Success(
        data = emptyList<Restaurant>(),
        meta = Meta(code = 200, status = "success", message = "")
    )

    val refreshResponseSuccess = TasstyResponse.Success(
        data = "Success",
        meta = Meta(code = 200, status = "success", message = "")
    )

}