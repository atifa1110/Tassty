package com.example.core.data.source.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.core.di.LocationDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationDataStore @Inject constructor(
    @LocationDataStore private val dataStore: DataStore<Preferences>
) {

    private object PreferencesKeys {
        val LAT = doublePreferencesKey("lat")
        val LNG = doublePreferencesKey("lng")
    }

    suspend fun save(lat: Double, lng: Double) {
        dataStore.edit{
            it[PreferencesKeys.LAT] = lat
            it[PreferencesKeys.LNG] = lng
        }
    }

    suspend fun get(): Pair<Double, Double> {
        val pref = dataStore.data.first()
        return Pair(
            pref[PreferencesKeys.LAT] ?: -6.3912,
            pref[PreferencesKeys.LNG] ?: 106.8260
        )
    }
}