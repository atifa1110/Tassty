package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.UserAddress
import com.example.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserAddressUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<TasstyResponse<List<UserAddress>>>{
        return repository.getUserAddress()
    }
}