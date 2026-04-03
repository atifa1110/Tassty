package com.example.core.data.source.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.core.data.model.AuthStatus
import com.example.core.data.model.RegistrationStep
import com.example.core.di.AuthDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataStore @Inject constructor(
    @AuthDataStore private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val REGISTRATION_STEP = stringPreferencesKey("registration_step")
        val IS_VERIFIED = booleanPreferencesKey("is_verified")
        val HAS_COMPLETED_SETUP = booleanPreferencesKey("has_completed_setup")

        val USER_ID = stringPreferencesKey("id")
        val ROLE = stringPreferencesKey("role")
        val EMAIL = stringPreferencesKey("email")
        val PROFILE_IMAGE = stringPreferencesKey("profile_image")
        val NAME = stringPreferencesKey("name")
        val ADDRESS_NAME = stringPreferencesKey("address_name")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val STREAM_TOKEN = stringPreferencesKey("stream_token")
        val FIREBASE_TOKEN = stringPreferencesKey("firebase_token")
    }

    val authStatus: Flow<AuthStatus> = dataStore.data.map { preferences ->
        val stepString = preferences[PreferencesKeys.REGISTRATION_STEP] ?: RegistrationStep.NONE.name
        val step = try {
            RegistrationStep.valueOf(stepString)
        } catch (e: IllegalArgumentException) {
            RegistrationStep.NONE
        }

        AuthStatus(
            isLoggedIn = preferences[PreferencesKeys.IS_LOGGED_IN] ?: false,
            registrationStep = step,
            isVerified = preferences[PreferencesKeys.IS_VERIFIED]?:false,
            hasCompletedSetup = preferences[PreferencesKeys.HAS_COMPLETED_SETUP] ?: false,
            userId = preferences[PreferencesKeys.USER_ID],
            role = preferences[PreferencesKeys.ROLE],
            email = preferences[PreferencesKeys.EMAIL],
            profileImage = preferences[PreferencesKeys.PROFILE_IMAGE],
            name = preferences[PreferencesKeys.NAME],
            addressName = preferences[PreferencesKeys.ADDRESS_NAME],
            accessToken = preferences[PreferencesKeys.ACCESS_TOKEN],
            refreshToken = preferences[PreferencesKeys.REFRESH_TOKEN],
            streamToken = preferences[PreferencesKeys.STREAM_TOKEN],
            firebaseToken = preferences[PreferencesKeys.FIREBASE_TOKEN]
        )
    }

    suspend fun updateAuthStatus(transform: (AuthStatus) -> AuthStatus) {
        val currentStatus = authStatus.first()
        val newStatus = transform(currentStatus)

        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] = newStatus.isLoggedIn
            preferences[PreferencesKeys.IS_VERIFIED] = newStatus.isVerified
            preferences[PreferencesKeys.REGISTRATION_STEP] = newStatus.registrationStep.name
            preferences[PreferencesKeys.HAS_COMPLETED_SETUP] = newStatus.hasCompletedSetup

            newStatus.userId?.let { preferences[PreferencesKeys.USER_ID] = it }
            newStatus.role?.let { preferences[PreferencesKeys.ROLE] = it }
            newStatus.email?.let { preferences[PreferencesKeys.EMAIL] = it }
            newStatus.name?.let { preferences[PreferencesKeys.NAME] = it }
            newStatus.profileImage?.let { preferences[PreferencesKeys.PROFILE_IMAGE] = it }
            newStatus.addressName?.let { preferences[PreferencesKeys.ADDRESS_NAME] = it }
            newStatus.accessToken?.let { preferences[PreferencesKeys.ACCESS_TOKEN] = it }
            newStatus.refreshToken?.let { preferences[PreferencesKeys.REFRESH_TOKEN] = it }
            newStatus.streamToken?.let { preferences[PreferencesKeys.STREAM_TOKEN] = it }
            newStatus.firebaseToken?.let { preferences[PreferencesKeys.FIREBASE_TOKEN] = it }
        }
    }


    // Erase all
    suspend fun clearAuthStatus() {
        dataStore.edit { it.clear() }
    }
}
