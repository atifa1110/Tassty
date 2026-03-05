package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginAndConnectChatUseCase @Inject constructor(
    private val loginUseCase: LoginEmailPasswordUseCase,
    private val connectChatUseCase: ConnectChatUseCase
) {
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    operator fun invoke(email: String, password: String): Flow<TasstyResponse<String>> {
        return loginUseCase(email, password).flatMapConcat { loginResult ->
            when (loginResult) {
                is TasstyResponse.Success -> flow {
                    emit(TasstyResponse.Success(loginResult.meta.message, loginResult.meta))

                    val user = loginResult.data
                    connectChatUseCase(
                        userId = user?.userId ?: "",
                        name = user?.name ?: "",
                        image = user?.profileImage ?: "",
                        token = user?.steamToken
                    ).collect { chatResult ->
                        emit(chatResult)
                    }
                }
                is TasstyResponse.Error -> flow{
                    emit(TasstyResponse.Error(loginResult.meta))
                }
                is TasstyResponse.Loading -> flow {
                    emit(TasstyResponse.Loading)
                }
            }
        }
    }
}