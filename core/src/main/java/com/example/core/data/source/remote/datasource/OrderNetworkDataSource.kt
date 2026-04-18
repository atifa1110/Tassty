package com.example.core.data.source.remote.datasource

import android.util.Log
import com.example.core.data.model.ChatChannelResponse
import com.example.core.data.model.DetailOrderDto
import com.example.core.data.model.OrderData
import com.example.core.data.model.OrderDto
import com.example.core.data.model.PaymentChannelDto
import com.example.core.data.model.PaymentDto
import com.example.core.data.model.RouteDto
import com.example.core.data.model.RouteUpdatePayload
import com.example.core.data.source.remote.api.OrderApiService
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall
import com.example.core.data.source.remote.request.CreateChannelRequest
import com.example.core.data.source.remote.request.OrderRequest
import com.example.core.data.source.remote.request.PaymentRequest
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.realtime.broadcastFlow
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderNetworkDataSource @Inject constructor(
    private val apiService: OrderApiService,
    private val supabase: SupabaseClient
) {
    private val TAG = "DriverTracking"

    suspend fun getPaymentChannel() : TasstyResponse<List<PaymentChannelDto>>{
        return safeApiCall { apiService.getPaymentChannel() }
    }

    suspend fun createOrder(request: OrderRequest) : TasstyResponse<OrderData>{
        return safeApiCall { apiService.createOrder(request) }
    }

    suspend fun getUserOrder() : TasstyResponse<List<OrderDto>>{
        return safeApiCall { apiService.getUserOrders() }
    }

    suspend fun paymentStripe(orderId: String, request: PaymentRequest) : TasstyResponse<PaymentDto>{
        return safeApiCall { apiService.paymentStripe(orderId,request)}
    }

    suspend fun getDetailOrder(orderId: String) : TasstyResponse<DetailOrderDto>{
        return safeApiCall { apiService.getDetailOrder(orderId) }
    }

    suspend fun getDetailRoute(orderId: String): TasstyResponse<RouteDto>{
        return safeApiCall { apiService.getDetailRoute(orderId)}
    }

    suspend fun getOrderSummary(orderId: String) : TasstyResponse<OrderDto>{
        return safeApiCall { apiService.getOrderSummary(orderId) }
    }

    suspend fun createChatChannel(request: CreateChannelRequest) : TasstyResponse<ChatChannelResponse>{
        return safeApiCall { apiService.createChatChannel(request) }
    }

    fun getDriverLocationStream(orderId: String): Flow<RouteUpdatePayload> = callbackFlow {
        Log.d(TAG, "Start tracking orderId=$orderId")

        val channel = supabase.realtime.channel("tracking:$orderId")

        val job = channel
            .broadcastFlow<RouteUpdatePayload>("location_update")
            .onEach { payload ->
                Log.d(TAG, "Received: $payload")

                val result = trySend(payload)
                if (result.isFailure) {
                    Log.e(TAG, "Failed to emit payload")
                }
            }
            .catch { e ->
                Log.e(TAG, "Flow error", e)
                close(e)
            }
            .launchIn(this)

        try {
            channel.subscribe()
            Log.i(TAG, "Subscribed to tracking:$orderId")
        } catch (e: Exception) {
            Log.e(TAG, "Subscribe failed", e)
            close(e)
        }

        awaitClose {
            Log.d(TAG, "Stop tracking orderId=$orderId")

            try {
                launch {
                    channel.unsubscribe()
                }
                Log.i(TAG, "Unsubscribed")
            } catch (e: Exception) {
                Log.e(TAG, "Unsubscribe error", e)
            }

            job.cancel()
        }
    }
}