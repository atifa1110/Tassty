package com.example.core.data.source.remote.datasource

import android.content.Context
import android.util.Log
import com.example.core.data.model.CategoryDto
import com.example.core.data.model.MenuDto
import com.example.core.data.model.RestaurantDto
import com.example.core.data.model.VoucherDto
import com.example.core.data.source.remote.api.CategoryApi
import com.example.core.data.source.remote.api.MenuApi
import com.example.core.data.source.remote.api.RestaurantApi
import com.example.core.data.source.remote.api.VoucherApi
import com.example.core.data.source.remote.network.ApiResponse
import com.example.core.data.source.remote.network.ErrorMapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.lang.reflect.Type

abstract class BaseDummyApi<T>(
    private val context: Context,
    private val gson: Gson,
    private val fileName: String
) {

    private val TAG = this::class.simpleName ?: "BaseDummyApi"

    // 🚨 BARU: Properti abstrak untuk menampung TypeToken yang SPESIFIK
    protected abstract val apiResponseType: Type

    // Membaca file blocking → pakai IO dispatcher saat dipanggil
    protected suspend fun readJson(): String = withContext(Dispatchers.IO) {
        context.assets.open(fileName).use { inputStream ->
            inputStream.bufferedReader().use { it.readText() }
        }
    }

    // Fungsi generik untuk load & parse JSON
    protected suspend fun loadData(): ApiResponse<List<T>> {
        return try {
            delay(300) // simulate network

            val jsonStr = readJson()
            Log.d(TAG, "Raw JSON read: $jsonStr")

            // 🚨 UBAH: Gunakan properti Type abstrak yang spesifik, BUKAN TypeToken generik
            val apiResponse = gson.fromJson<ApiResponse<List<T>>>(jsonStr, apiResponseType)

            if (apiResponse != null) {
                Log.i(TAG, "Parsing successful. Number of items: ${apiResponse.data?.size}")
            } else {
                Log.e(TAG, "Parsing FAILED. gson.fromJson returned null.")
            }

            apiResponse ?: ApiResponse(meta = ErrorMapper.mapError(Exception("Parsing error")), data = null)
        } catch (e: Exception) {
            Log.e(TAG, "Error reading or parsing $fileName", e)
            ApiResponse(meta = ErrorMapper.mapError(e), data = null)
        }
    }
}

class DummyRestaurantApi(context: Context, gson: Gson) :
    BaseDummyApi<RestaurantDto>(context, gson, "restaurant.json"), RestaurantApi {

    override val apiResponseType: Type = object : TypeToken<ApiResponse<List<RestaurantDto>>>() {}.type

    override suspend fun getRecommendedRestaurants(): ApiResponse<List<RestaurantDto>> = loadData()
    override suspend fun getNearbyRestaurants(): ApiResponse<List<RestaurantDto>> = loadData()
}

class DummyVoucherApi(context: Context, gson: Gson) :
    BaseDummyApi<VoucherDto>(context, gson, "voucher.json"), VoucherApi {

    override val apiResponseType: Type = object : TypeToken<ApiResponse<List<VoucherDto>>>() {}.type

    override suspend fun getTodayVoucher(): ApiResponse<List<VoucherDto>> = loadData()
}


class DummyMenuApi(context: Context, gson: Gson) :
    BaseDummyApi<MenuDto>(context, gson, "menu.json"), MenuApi {

    override val apiResponseType: Type = object : TypeToken<ApiResponse<List<MenuDto>>>() {}.type

    override suspend fun getRecommendedMenus(): ApiResponse<List<MenuDto>> = loadData()
    override suspend fun getSuggestedMenus(): ApiResponse<List<MenuDto>> = loadData()
}

class DummyCategoryApi(context: Context, gson: Gson) :
    BaseDummyApi<CategoryDto>(context, gson, "category.json"), CategoryApi {

    override val apiResponseType: Type = object : TypeToken<ApiResponse<List<CategoryDto>>>() {}.type
    override suspend fun getAllCategories(): ApiResponse<List<CategoryDto>> = loadData()

}