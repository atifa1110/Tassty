package com.example.core.data.source.local.datasource

import com.example.core.data.source.local.datastore.LocationDataStore
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Local Data Source acting as the "Librarian" for location data.
 * * Responsibilities:
 * - Direct communication with the persistent storage layer (DataStore).
 * - Handling Disk I/O operations which are relatively slow and resource-heavy.
 */
@Singleton
class LocationLocalDataSource @Inject constructor(
    private val dataStore: LocationDataStore
) {
    /** Fetches raw coordinates from the physical storage (Disk).*/
    suspend fun getLocation(): Pair<Double, Double> {
        return dataStore.get()
    }

    /** Permanently commits coordinates to the physical storage (Disk).*/
    suspend fun saveLocation(lat: Double, lng: Double) {
        dataStore.save(lat, lng)
    }
}