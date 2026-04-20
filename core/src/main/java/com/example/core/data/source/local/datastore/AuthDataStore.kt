package com.example.core.data.source.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
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
    @AuthDataStore private val dataStore: DataStore<Preferences>,
    private val securePrefs: SecurePreferences
) {
    private object PreferencesKeys {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val IS_BOARDING = booleanPreferencesKey("is_boarding_completed")
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

    private fun Preferences.getSecure(key: Preferences.Key<String>): String? {
        val encryptedValue = this[key]
        return if (!encryptedValue.isNullOrEmpty()) {
            securePrefs.decrypt(encryptedValue)
        } else null
    }

    private fun MutablePreferences.setSecure(key: Preferences.Key<String>, value: String?) {
        if (value != null) {
            this[key] = securePrefs.encrypt(value)
        } else {
            this.remove(key)
        }
    }

    val authStatus: Flow<AuthStatus> = dataStore.data.map { preferences ->
        mapToAuthStatus(preferences)
    }

    private fun mapToAuthStatus(preferences: Preferences): AuthStatus {
        val stepString = preferences[PreferencesKeys.REGISTRATION_STEP] ?: RegistrationStep.NONE.name
        val step = try {
            RegistrationStep.valueOf(stepString)
        } catch (e: IllegalArgumentException) {
            RegistrationStep.NONE
        }

        return AuthStatus(
            isDarkMode = preferences[PreferencesKeys.IS_DARK_MODE] ?: false,
            isBoardingCompleted = preferences[PreferencesKeys.IS_BOARDING] ?: false,
            isLoggedIn = preferences[PreferencesKeys.IS_LOGGED_IN] ?: false,
            registrationStep = step,
            isVerified = preferences[PreferencesKeys.IS_VERIFIED] ?: false,
            hasCompletedSetup = preferences[PreferencesKeys.HAS_COMPLETED_SETUP] ?: false,

            role = preferences[PreferencesKeys.ROLE],
            email = preferences[PreferencesKeys.EMAIL],
            name = preferences[PreferencesKeys.NAME],
            profileImage = preferences[PreferencesKeys.PROFILE_IMAGE],
            addressName = preferences[PreferencesKeys.ADDRESS_NAME],

            userId = preferences.getSecure(PreferencesKeys.USER_ID),
            accessToken = preferences.getSecure(PreferencesKeys.ACCESS_TOKEN),
            refreshToken = preferences.getSecure(PreferencesKeys.REFRESH_TOKEN),
            streamToken =preferences.getSecure(PreferencesKeys.STREAM_TOKEN),
            firebaseToken = preferences.getSecure(PreferencesKeys.FIREBASE_TOKEN),
        )
    }

    suspend fun updateAuthStatus(transform: (AuthStatus) -> AuthStatus) {
        dataStore.edit { preferences ->
            val currentStatus = mapToAuthStatus(preferences)
            val newStatus = transform(currentStatus)

            preferences[PreferencesKeys.IS_DARK_MODE] = newStatus.isDarkMode
            preferences[PreferencesKeys.IS_BOARDING] = newStatus.isBoardingCompleted
            preferences[PreferencesKeys.IS_LOGGED_IN] = newStatus.isLoggedIn
            preferences[PreferencesKeys.IS_VERIFIED] = newStatus.isVerified
            preferences[PreferencesKeys.REGISTRATION_STEP] = newStatus.registrationStep.name
            preferences[PreferencesKeys.HAS_COMPLETED_SETUP] = newStatus.hasCompletedSetup

            preferences[PreferencesKeys.ROLE] = newStatus.role ?: ""
            preferences[PreferencesKeys.EMAIL] = newStatus.email ?: ""
            preferences[PreferencesKeys.NAME] = newStatus.name ?: ""
            preferences[PreferencesKeys.PROFILE_IMAGE] = newStatus.profileImage ?: ""
            preferences[PreferencesKeys.ADDRESS_NAME] = newStatus.addressName ?: ""

            preferences.setSecure(PreferencesKeys.USER_ID, newStatus.userId)
            preferences.setSecure(PreferencesKeys.ACCESS_TOKEN, newStatus.accessToken)
            preferences.setSecure(PreferencesKeys.REFRESH_TOKEN, newStatus.refreshToken)
            preferences.setSecure(PreferencesKeys.STREAM_TOKEN, newStatus.streamToken)
            preferences.setSecure(PreferencesKeys.FIREBASE_TOKEN, newStatus.firebaseToken)
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            val keysToRemove = listOf(
                PreferencesKeys.IS_LOGGED_IN,
                PreferencesKeys.REGISTRATION_STEP,
                PreferencesKeys.IS_VERIFIED,
                PreferencesKeys.HAS_COMPLETED_SETUP,
                PreferencesKeys.USER_ID,
                PreferencesKeys.ROLE,
                PreferencesKeys.EMAIL,
                PreferencesKeys.PROFILE_IMAGE,
                PreferencesKeys.NAME,
                PreferencesKeys.ADDRESS_NAME,
                PreferencesKeys.ACCESS_TOKEN,
                PreferencesKeys.REFRESH_TOKEN,
                PreferencesKeys.STREAM_TOKEN,
                PreferencesKeys.FIREBASE_TOKEN
            )

            keysToRemove.forEach { key ->
                preferences.remove(key)
            }
        }
    }

    suspend fun clearAuthStatus() {
        dataStore.edit { it.clear() }
    }
}
