package com.example.core.data.repository

import android.Manifest
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresPermission
import com.example.core.data.provider.LocationTracker
import com.example.core.data.source.local.cache.LocationManager
import com.example.core.di.ApplicationScope
import com.example.core.domain.model.LocationDetail
import com.example.core.domain.repository.LocationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val tracker: LocationTracker,
    private val locationManager: LocationManager,
    private val geocoder: Geocoder,
    @ApplicationScope private val scope: CoroutineScope
) : LocationRepository {

    init {
        scope.launch {
            locationManager.load()
        }

        tracker.locationFlow
            .onEach { location ->
                locationManager.updateIfFar(location.latitude, location.longitude)
            }
            .launchIn(scope)
    }

    /**
     * Fungsi ini yang akan dipanggil pas Setup Account/Map.
     * Menggabungkan GPS Akurat + Geocoding Alamat.
     */
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun getCurrentLocationDetail(): LocationDetail {
        tracker.getCurrentLocation()
        val (lat, lng) = locationManager.getLatestLocation()

        if (lat == null || lng == null) {
            return LocationDetail(fullAddress = "Lokasi tidak tersedia", latitude = 0.0, longitude = 0.0, city = "")
        }
        val addressObj = fetchAddress(lat, lng)

        return LocationDetail(
            latitude = lat,
            longitude = lng,
            fullAddress = addressObj?.getAddressLine(0) ?: "Alamat tidak ditemukan",
            city = addressObj?.locality ?: "Kota tidak diketahui",
        )
    }

    override suspend fun getAddressFromCoordinate(lat: Double, lng: Double): String {
        val addressObj = fetchAddress(lat, lng)
        return addressObj?.getAddressLine(0) ?: "Alamat tidak ditemukan"
    }

    private suspend fun fetchAddress(lat: Double, lng: Double): Address? {
        return withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { continuation ->
                val listener = Geocoder.GeocodeListener { addresses ->
                    continuation.resume(addresses.firstOrNull()) { cause, _, _ -> null?.let { it(cause) } }
                }

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        geocoder.getFromLocation(lat, lng, 1, listener)
                    } else {
                        val addresses = geocoder.getFromLocation(lat, lng, 1)
                        continuation.resume(addresses?.firstOrNull()) { cause, _, _ -> null?.let { it(cause) } }
                    }
                } catch (e: Exception) {
                    continuation.resume(null) { cause, _, _ -> null?.let { it(cause) } }
                }
            }
        }
    }
}
