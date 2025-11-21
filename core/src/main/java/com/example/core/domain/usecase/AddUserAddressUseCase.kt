package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.request.UserAddressRequest
import com.example.core.data.source.remote.request.UserSetUpRequest
import com.example.core.domain.repository.AuthRepository
import com.example.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddUserAddressUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(request: UserSetUpRequest) : Flow<TasstyResponse<String>> =
        flow {
            userRepository.addUserAddress(request).collect { result ->
                when(result){
                    is TasstyResponse.Error ->  emit(result)
                    is TasstyResponse.Success-> {
                        authRepository.updateAuthStatus { current ->
                            current.copy(
                                hasCompletedSetup = true,
                                addressName = request.address.addressName
                            )
                        }
                        emit(result)
                    }
                    is TasstyResponse.Loading -> emit(TasstyResponse.Loading)
                }
            }
    }
}