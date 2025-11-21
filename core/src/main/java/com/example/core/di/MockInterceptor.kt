package com.example.core.di

import android.content.Context
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockInterceptor(
    private val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        val fileName = when {
            path.contains("login") -> "login.json"
            path.contains("register") -> "register.json"
            path.contains("verify") -> "verify.json"
            path.contains("category") -> "category.json"
            path.contains("user-addresses") -> "post_address.json"
            path.contains("recommended_restaurant") -> "restaurant.json"
            path.contains("nearby_restaurant") -> "restaurant.json"
            path.contains("search_restaurant") -> "restaurant.json"
            path.contains("sorting_restaurant") -> "restaurant_menus.json"
            path.contains("recommended_menu") -> "menu.json"
            path.contains("suggested_menu") -> "menu.json"
            path.contains("search_menu") -> "menu.json"
            path.contains("vouchers") -> "voucher.json"
            path.contains("filter_option") -> "search_options.json"
            else -> null
        }

        return if (fileName != null) {
            val json = context.assets.open(fileName).bufferedReader().use { it.readText() }

            Response.Builder()
                .code(200)
                .message(json)
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .body(json.toResponseBody("application/json".toMediaType()))
                .build()
        } else {
            chain.proceed(request)
        }
    }
}
