package com.example.core.data.source.local.cache

import com.example.core.data.source.local.datastore.LocationDataStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationManager @Inject constructor(
    private val storage: LocationDataStore
) {

    var lat: Double? = null
    var lng: Double? = null

    suspend fun load() {
        val (savedLat, savedLng) = storage.get()
        lat = savedLat
        lng = savedLng
    }

    suspend fun updateIfFar(newLat: Double, newLng: Double) {

        if (lat == null || lng == null) {
            save(newLat, newLng)
            return
        }

        val distance = calculateDistance(
            lat!!, lng!!,
            newLat, newLng
        )

        if (distance > 1000) { // > 1km
            save(newLat, newLng)
        }
    }

    private suspend fun save(lat: Double, lng: Double) {
        this.lat = lat
        this.lng = lng
        storage.save(lat, lng)
    }
}


fun calculateDistance(
    lat1: Double, lon1: Double,
    lat2: Double, lon2: Double
): Double {
    val R = 6371000.0 // radius bumi dalam meter
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) *
            Math.cos(Math.toRadians(lat2)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2)

    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

    return R * c // hasil dalam meter
}
