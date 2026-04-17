package com.example.core.data.source.remote.datasource

import android.content.Context
import android.net.Uri
import com.example.core.data.model.CardUserDto
import com.example.core.data.model.ProfileDto
import com.example.core.data.model.SetupDto
import com.example.core.data.model.UserAddressDto
import com.example.core.data.model.UserDto
import com.example.core.data.source.remote.api.UserApiService
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall
import com.example.core.data.source.remote.request.AddressRequest
import com.example.core.data.source.remote.request.ProfileRequest
import com.example.core.data.source.remote.request.SaveCardRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class UserNetworkDataSource@Inject constructor(
    private val userApiService: UserApiService
) {
    suspend fun getUserProfile(): TasstyResponse<UserDto>{
        return safeApiCall { userApiService.getUserProfile() }
    }

    suspend fun updateUserProfile(name: String, imageUri: Uri?, context: Context): TasstyResponse<ProfileDto> {
        return safeApiCall {
            userApiService.updateUserProfile(
                name = name.toPart(),
                profileImage = imageUri?.toMultipartPart(context, "profileImage")
            )
        }
    }

    suspend fun getUserAddress(): TasstyResponse<List<UserAddressDto>>{
        return safeApiCall { userApiService.getUserAddress()}
    }

    suspend fun createUserAddress(request: AddressRequest): TasstyResponse<Unit>{
        return safeApiCall { userApiService.createUserAddress(request)}
    }

    suspend fun deleteUserAddress(addressId: String): TasstyResponse<Unit>{
        return safeApiCall { userApiService.deleteUserAddress(addressId)}
    }

    suspend fun createSetupIntent(): TasstyResponse<SetupDto> {
        return safeApiCall { userApiService.createSetupIntent() }
    }

    suspend fun saveCardToBackend(paymentMethodId: String, color: String, background: String)  : TasstyResponse<Unit>{
        return safeApiCall { userApiService.saveCard(SaveCardRequest(paymentMethodId,
            color,background))
        }
    }

    suspend fun getUserCard(): TasstyResponse<List<CardUserDto>>{
        return safeApiCall { userApiService.getUserCard()}
    }

    suspend fun deleteUserCard(cardId: String): TasstyResponse<Unit>{
        return safeApiCall { userApiService.deleteUserCard(cardId)}
    }
}

fun String.toPart(): RequestBody {
    return this.toRequestBody("text/plain".toMediaTypeOrNull())
}

fun Uri.toMultipartPart(context: Context, partName: String): MultipartBody.Part? {
    return try {
        context.contentResolver.openInputStream(this)?.use { inputStream ->
            val bytes = inputStream.readBytes()
            val requestFile = bytes.toRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(partName, "profile_picture.jpg", requestFile)
        }
    } catch (e: Exception) {
        null
    }
}