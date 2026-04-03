package com.example.core.domain.usecase

import android.content.Context
import android.net.Uri
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(
        name: String,
        imageUri: Uri?,
        context: Context
    ): Flow<TasstyResponse<String>>{
        return repository.updateUserProfile(name,imageUri,context)
    }
}