package com.example.core.data.repository

import android.app.Activity
import androidx.activity.ComponentActivity
import com.example.core.data.mapper.toDomain
import com.example.core.data.source.local.datastore.AuthDataStore
import com.example.core.data.source.remote.datasource.AuthNetworkDataSource
import com.example.core.data.source.remote.datasource.UserNetworkDataSource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.request.SetUpRequest
import com.example.core.domain.model.User
import com.example.core.domain.model.UserAddress
import com.example.core.domain.repository.UserRepository
import com.example.core.ui.mapper.toRequestDto
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmSetupIntentParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userNetworkDataSource: UserNetworkDataSource,
    private val stripe: Stripe
) : UserRepository{

    override fun getUserAddress(): Flow<TasstyResponse<List<UserAddress>>> = flow {
        emit(TasstyResponse.Loading)

        val response = userNetworkDataSource.getUserAddress()
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> emit(TasstyResponse.Success(response.data?.map { it.toDomain() },response.meta))
            else -> {}
        }
    }

    override fun getUserProfile(): Flow<TasstyResponse<User>> = flow {
        emit(TasstyResponse.Loading)

        val response = userNetworkDataSource.getUserProfile()
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> emit(TasstyResponse.Success(response.data?.toDomain(),response.meta))
            else -> {}
        }
    }

    override fun getSetupIntentSecret(): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading)
        val response = userNetworkDataSource.createSetupIntent()
        when (response) {
            is TasstyResponse.Success -> emit(TasstyResponse.Success(response.data?.clientSecret ?: "", response.meta))
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            else -> {}
        }
    }

    override fun confirmStripeSetup(activity: ComponentActivity, params: ConfirmSetupIntentParams) {
        stripe.confirmSetupIntent(activity, params)
    }

    override fun saveCardInfo(paymentMethodId: String, color: String): Flow<TasstyResponse<String>> = flow {
        val response = userNetworkDataSource.saveCardToBackend(paymentMethodId,color)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> emit(TasstyResponse.Success(response.meta.message,response.meta))
            else -> {}
        }
    }
}