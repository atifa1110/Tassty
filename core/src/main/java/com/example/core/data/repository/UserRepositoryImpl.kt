package com.example.core.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.core.data.mapper.toDomain
import com.example.core.data.model.RegistrationStep
import com.example.core.data.source.local.datastore.AuthDataStore
import com.example.core.data.source.remote.datasource.UserNetworkDataSource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.request.AddressRequest
import com.example.core.data.source.remote.request.ProfileRequest
import com.example.core.domain.model.CardUser
import com.example.core.domain.model.User
import com.example.core.domain.model.UserAddress
import com.example.core.domain.repository.UserRepository
import com.stripe.android.Stripe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userNetworkDataSource: UserNetworkDataSource,
    private val authDataStore: AuthDataStore
) : UserRepository{

    override fun getUserAddress(): Flow<TasstyResponse<List<UserAddress>>> = flow {
        emit(TasstyResponse.Loading())

        val response = userNetworkDataSource.getUserAddress()
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> emit(TasstyResponse.Success(response.data?.map { it.toDomain() },response.meta))
            else -> {}
        }
    }

    override fun createUserAddress(request: AddressRequest): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading())

        val response = userNetworkDataSource.createUserAddress(request)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> emit(TasstyResponse.Success(response.meta.message,response.meta))
            else -> {}
        }
    }

    override fun getUserProfile(): Flow<TasstyResponse<User>> = flow {
        emit(TasstyResponse.Loading())

        val response = userNetworkDataSource.getUserProfile()
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> emit(TasstyResponse.Success(response.data?.toDomain(),response.meta))
            else -> {}
        }
    }

    override fun updateUserProfile(
        name: String,
        imageUri: Uri?,
        context: Context
    ): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading())

        val response = userNetworkDataSource.updateUserProfile(name,imageUri,context)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                val profile = response.data
                authDataStore.updateAuthStatus { current ->
                    current.copy(
                        name = profile?.name,
                        profileImage = profile?.profileImage
                    )
                }
                emit(TasstyResponse.Success(response.meta.message, response.meta))
            }
            else -> {}
        }
    }

    override fun getSetupIntentSecret(): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading())
        val response = userNetworkDataSource.createSetupIntent()
        when (response) {
            is TasstyResponse.Success -> emit(TasstyResponse.Success(response.data?.clientSecret ?: "", response.meta))
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            else -> {}
        }
    }


    override fun saveCardInfo(paymentMethodId: String, color: String, background: String): Flow<TasstyResponse<String>> = flow {
        val response = userNetworkDataSource.saveCardToBackend(paymentMethodId,color,background)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> emit(TasstyResponse.Success(response.meta.message,response.meta))
            else -> {}
        }
    }

    override fun getUserCard(): Flow<TasstyResponse<List<CardUser>>> = flow {
        val response = userNetworkDataSource.getUserCard()
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> emit(TasstyResponse.Success(response.data?.map { it.toDomain() },response.meta))
            else -> {}
        }
    }
}