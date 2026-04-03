package com.example.core.data.source.local.cache

import com.example.core.data.source.local.datasource.LocationLocalDataSource
import com.example.core.data.source.local.datastore.LocationDataStore
import com.example.core.domain.utils.calculateDistance
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager class acting as the "Executive Assistant" for location management.
 * * Strategy: In-Memory Caching.
 * This class maintains a copy of the location in RAM to avoid frequent Disk I/O.
 * * Why this Architecture?
 * 1. Performance: Accessing RAM variables is nearly instantaneous compared to Disk.
 * 2. Battery Efficiency: Prevents unnecessary Disk writes unless the user moves significantly.
 * 3. Abstraction: Segregates business logic (distance calculation) from storage logic.
 */
@Singleton
class LocationManager @Inject constructor(
    private val storage: LocationLocalDataSource
) {
    // In-Memory Cache: Stores coordinates in RAM for instant access by Interceptors and UI.
    var lat: Double? = null
    var lng: Double? = null

    /**
     * Synchronizes the RAM cache with the data stored on Disk.
     * Usually called during the Application's "Cold Start."
     */
    suspend fun load() {
        val (savedLat, savedLng) = storage.getLocation()
        lat = savedLat
        lng = savedLng
    }

    /**
     * The primary entry point for Interceptors.
     * Returns the location from RAM (Fast). If RAM is empty, it attempts a one-time Disk load.
     */
    suspend fun getLatestLocation(): Pair<Double?, Double?> {
        if (lat == null || lng == null) {
            load()
        }
        return Pair(lat, lng)
    }

    /**
     * Core business logic to determine if a location update is required.
     * It only triggers a Disk Write (Persistent Storage) if the distance moved
     * exceeds the 1000-meter (1km) threshold.
     */
    suspend fun updateIfFar(newLat: Double, newLng: Double) {
        val currentLat = lat
        val currentLng = lng

        // If RAM is empty, treat the first received coordinate as the starting point.
        if (currentLat == null || currentLng == null) {
            save(newLat, newLng)
            return
        }

        // Calculate distance using the Haversine formula (Domain Utility).
        val distance = calculateDistance(
            currentLat, currentLng,
            newLat, newLng
        )

        // I/O Optimization Strategy: Only write to Disk if the change is significant (> 1km).
        if (distance > 100) {
            this.lat = newLat
            this.lng = newLng

            // wait until distance > 500 meter (can save batery/disk)
            if (distance > 500) {
                storage.saveLocation(newLat, newLng)
            }
        }
    }

    /**Updates both the RAM cache and the persistent storage concurrently.*/
    private suspend fun save(lat: Double, lng: Double) {
        this.lat = lat
        this.lng = lng
        storage.saveLocation(lat, lng)
    }
}