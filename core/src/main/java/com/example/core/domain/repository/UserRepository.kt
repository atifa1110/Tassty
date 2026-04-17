package com.example.core.domain.repository

import android.content.Context
import android.net.Uri
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.request.AddressRequest
import com.example.core.domain.model.CardUser
import com.example.core.domain.model.User
import com.example.core.domain.model.UserAddress
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserAddress(): Flow<TasstyResponse<List<UserAddress>>>

    fun deleteUserAddress(addressId: String): Flow<TasstyResponse<String>>

    fun createUserAddress(request: AddressRequest): Flow<TasstyResponse<String>>

    fun getUserProfile(): Flow<TasstyResponse<User>>

    fun updateUserProfile(name: String, imageUri: Uri?, context: Context): Flow<TasstyResponse<String>>

    fun getSetupIntentSecret(): Flow<TasstyResponse<String>>

    fun saveCardInfo(paymentMethodId: String, color: String, background: String): Flow<TasstyResponse<String>>

    fun getUserCard(): Flow<TasstyResponse<List<CardUser>>>

    fun deleteUserCard(cardId: String): Flow<TasstyResponse<String>>
}