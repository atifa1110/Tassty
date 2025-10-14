package com.example.core.data.source.remote.datasource

import android.content.Context
import android.util.Log
import com.example.core.data.model.RestaurantDto
import com.example.core.data.source.remote.api.RestaurantApi
import com.example.core.data.source.remote.network.ApiResponse
import com.example.core.data.source.remote.network.ErrorMapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay

class DummyRestaurantApi(
    private val context: Context,
    private val gson: Gson
) : RestaurantApi {

    // Define a Tag for easy filtering in Logcat
    private val TAG = "DummyRestaurantApi"

    private suspend fun readJson(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    override suspend fun getRestaurants(): ApiResponse<List<RestaurantDto>> {
        return try {
            delay(300) // simulate network delay
            val jsonStr = readJson("restaurant.json")

            // 1. LOG THE RAW JSON STRING
            Log.d(TAG, "Raw JSON read: $jsonStr")

            // Parse langsung ke ApiResponse<List<RestaurantDto>>
            val type = object : TypeToken<ApiResponse<List<RestaurantDto>>>() {}.type
            val apiResponse = gson.fromJson<ApiResponse<List<RestaurantDto>>>(jsonStr, type)

            // 2. LOG THE PARSED OBJECT
            if (apiResponse != null) {
                // Log success status
                Log.i(TAG, "Parsing successful. Number of restaurants: ${apiResponse.data?.size}")
                // Log the entire parsed object (use caution with very large objects)
                // Log.d(TAG, "Parsed Response: $apiResponse")
            } else {
                Log.e(TAG, "Parsing FAILED. gson.fromJson returned null.")
            }


            return apiResponse
                ?: ApiResponse(meta = ErrorMapper.mapError(Exception("Parsing error")), data = null)
        } catch (e: Exception) {
            // 3. LOG ANY EXCEPTIONS (like file not found or IO error)
            Log.e(TAG, "Error reading or parsing restaurant.json", e)
            ApiResponse(meta = ErrorMapper.mapError(e), data = null)
        }
    }
}

