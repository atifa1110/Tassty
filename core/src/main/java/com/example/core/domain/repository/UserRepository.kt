package com.example.core.domain.repository

import android.app.Activity
import androidx.activity.ComponentActivity
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.request.SetUpRequest
import com.example.core.domain.model.User
import com.example.core.domain.model.UserAddress
import com.stripe.android.model.ConfirmSetupIntentParams
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserAddress(): Flow<TasstyResponse<List<UserAddress>>>
    fun getUserProfile(): Flow<TasstyResponse<User>>
    fun getSetupIntentSecret(): Flow<TasstyResponse<String>>
    fun confirmStripeSetup(activity: ComponentActivity, params: ConfirmSetupIntentParams)
    fun saveCardInfo(paymentMethodId: String, color: String): Flow<TasstyResponse<String>>
}