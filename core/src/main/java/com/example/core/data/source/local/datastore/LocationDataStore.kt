package com.example.core.data.source.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LocationDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private val LAT = doublePreferencesKey("lat")
    private val LNG = doublePreferencesKey("lng")

    suspend fun save(lat: Double, lng: Double) {
        dataStore.edit{
            it[LAT] = lat
            it[LNG] = lng
        }
    }

    suspend fun get(): Pair<Double, Double> {
        val pref = dataStore.data.first()
        return Pair(
            pref[LAT] ?: -6.3912,
            pref[LNG] ?: 106.8260
        )
    }
}