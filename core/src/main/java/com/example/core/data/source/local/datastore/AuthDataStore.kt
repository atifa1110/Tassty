package com.example.core.data.source.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.core.data.model.AuthStatus
import com.example.core.data.model.RegistrationStep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private object PreferencesKeys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val REGISTRATION_STEP = stringPreferencesKey("registration_step")
        val IS_VERIFIED = booleanPreferencesKey("is_verified")
        val HAS_COMPLETED_SETUP = booleanPreferencesKey("has_completed_setup")

        val EMAIL = stringPreferencesKey("email")
        val PROFILE_IMAGE = stringPreferencesKey("profile_image")
        val NAME = stringPreferencesKey("name")
        val ADDRESS_NAME = stringPreferencesKey("address_name")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val STEAM_TOKEN = stringPreferencesKey("steam_token")
    }

    // Flow to read authentication data
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
            email = preferences[PreferencesKeys.EMAIL],
            profileImage = preferences[PreferencesKeys.PROFILE_IMAGE],
            name = preferences[PreferencesKeys.NAME],
            addressName = preferences[PreferencesKeys.ADDRESS_NAME],
            accessToken = preferences[PreferencesKeys.ACCESS_TOKEN],
            refreshToken = preferences[PreferencesKeys.REFRESH_TOKEN],
            steamToken = preferences[PreferencesKeys.STEAM_TOKEN]
        )
    }

    // Update status
    suspend fun updateAuthStatus(transform: (AuthStatus) -> AuthStatus) {
        dataStore.edit { preferences ->
            val currentStatus = authStatus.first()
            val newStatus = transform(currentStatus)

            preferences[PreferencesKeys.IS_LOGGED_IN] = newStatus.isLoggedIn
            preferences[PreferencesKeys.IS_VERIFIED] = newStatus.isVerified
            preferences[PreferencesKeys.REGISTRATION_STEP] = newStatus.registrationStep.name
            preferences[PreferencesKeys.HAS_COMPLETED_SETUP] = newStatus.hasCompletedSetup

            newStatus.email?.let {
                preferences[PreferencesKeys.EMAIL] = it
            }

            newStatus.profileImage?.let {
                preferences[PreferencesKeys.PROFILE_IMAGE] = it
            }

            newStatus.name?.let {
                preferences[PreferencesKeys.NAME] = it
            }

            newStatus.addressName?.let {
                preferences[PreferencesKeys.ADDRESS_NAME] = it
            }

            newStatus.accessToken?.let {
                preferences[PreferencesKeys.ACCESS_TOKEN] = it
            }

            newStatus.refreshToken?.let {
                preferences[PreferencesKeys.REFRESH_TOKEN] = it
            }

            newStatus.steamToken?.let {
                preferences[PreferencesKeys.STEAM_TOKEN] = it
            }
        }
    }


    // Erase all
    suspend fun clearAuthStatus() {
        dataStore.edit { it.clear() }
    }
}
