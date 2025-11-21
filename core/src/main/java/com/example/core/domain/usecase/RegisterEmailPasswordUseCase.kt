package com.example.core.domain.usecase

import com.example.core.data.model.RegistrationStep
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterEmailPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String, fullName: String): Flow<TasstyResponse<String>> =
        flow {
            // Step 1: Update status to REGISTERING
            authRepository.updateAuthStatus { current ->
                current.copy(registrationStep = RegistrationStep.REGISTERING)
            }

            // Step 2: Call repository register
            authRepository.register(email, password, fullName).collect { result ->
                when (result) {
                    is TasstyResponse.Success -> {
                        // Step 3: Update status to AWAITING_CONFIRMATION & save email
                        authRepository.updateAuthStatus { current ->
                            current.copy(
                                registrationStep = RegistrationStep.AWAITING_CONFIRMATION,
                                email = email,
                                name = result.data?.name,
                                profileImage = result.data?.profileName,
                                addressName = result.data?.addressName
                            )
                        }
                        emit(TasstyResponse.Success(result.meta.message,result.meta))
                    }

                    is TasstyResponse.Error -> {
                        emit(result)
                    }

                    is TasstyResponse.Loading -> emit(TasstyResponse.Loading)
                }
            }
        }
}


