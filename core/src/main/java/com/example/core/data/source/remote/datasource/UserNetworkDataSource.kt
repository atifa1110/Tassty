package com.example.core.data.source.remote.datasource

import com.example.core.data.model.SetupDto
import com.example.core.data.model.UserAddressDto
import com.example.core.data.model.UserDto
import com.example.core.data.source.remote.api.UserApiService
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall
import com.example.core.data.source.remote.request.SaveCardRequest
import com.example.core.data.source.remote.request.SetUpRequest
import javax.inject.Inject

class UserNetworkDataSource@Inject constructor(
    private val userApiService: UserApiService
) {
    suspend fun getUserProfile(): TasstyResponse<UserDto>{
        return safeApiCall { userApiService.getUserProfile() }
    }

    suspend fun getUserAddress(): TasstyResponse<List<UserAddressDto>>{
        return safeApiCall { userApiService.getUserAddress()}
    }

    suspend fun createSetupIntent(): TasstyResponse<SetupDto> {
        return safeApiCall { userApiService.createSetupIntent() }
    }

    suspend fun saveCardToBackend(paymentMethodId: String, color: String)  : TasstyResponse<Unit>{
        return safeApiCall { userApiService.saveCard(SaveCardRequest(paymentMethodId,
            color))
        }
    }
}