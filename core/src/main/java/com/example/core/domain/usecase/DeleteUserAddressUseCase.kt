package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteUserAddressUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    operator fun invoke(addressId: String): Flow<TasstyResponse<String>>{
        return userRepository.deleteUserAddress(addressId)
    }
}