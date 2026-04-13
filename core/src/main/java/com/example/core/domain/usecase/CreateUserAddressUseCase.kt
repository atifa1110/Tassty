package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.request.AddressRequest
import com.example.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateUserAddressUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(
        addressName: String,
        fullAddress: String,
        landmarkDetail: String,
        latitude: Double,
        longitude: Double,
        addressType:String
    ): Flow<TasstyResponse<String>> {
        val request = AddressRequest(addressName,fullAddress,landmarkDetail,latitude,longitude,addressType)
        return userRepository.createUserAddress(request)
    }
}