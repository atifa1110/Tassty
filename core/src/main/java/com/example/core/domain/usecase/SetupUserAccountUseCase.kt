package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.UserAddress
import com.example.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SetupUserAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(address: UserAddress, cuisines: List<String>) : Flow<TasstyResponse<String>>{
        return authRepository.setupAccount(address,cuisines)
    }
}