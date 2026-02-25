package com.example.core.data.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.core.data.source.local.cache.LocationManager
import com.example.core.domain.model.LocationDetail
import com.example.core.domain.model.UserAddress
import com.example.core.domain.provider.LocationProvider
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class DefaultLocationProvider @Inject constructor(
    private val context: Context,
    private val geocoder: Geocoder,
    private val locationManager: LocationManager
) : LocationProvider {
    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun getCurrentLocation(): Flow<LocationDetail> = flow {
        // Jalankan operasi ambil data di dalam flow
        locationManager.load()
        val currentLat = locationManager.lat
        val currentLng = locationManager.lng

        val detail = LocationDetail(
            fullAddress = "",
            latitude = currentLat ?: 0.0,
            longitude = currentLng ?: 0.0,
            city = ""
        )

        emit(detail) // Kirim datanya ke subscriber
    }.flowOn(Dispatchers.IO) // Pastikan jalan di background thread

    @SuppressLint("MissingPermission")
    override suspend fun syncLocation() {
        if (!hasLocationPermission()) return

        val location = getFreshLocation()
        sendToServer(location)
    }

    @SuppressLint("MissingPermission")
    private suspend fun getFreshLocation(): Location =
        suspendCancellableCoroutine { cont ->

            val request = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                3000
            ).build()

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val location = result.lastLocation
                    if (location != null && cont.isActive) {
                        cont.resume(location)
                        fusedClient.removeLocationUpdates(this)
                    }
                }
            }

            fusedClient.requestLocationUpdates(
                request,
                callback,
                Looper.getMainLooper()
            )

            cont.invokeOnCancellation {
                fusedClient.removeLocationUpdates(callback)
            }
        }

    private suspend fun sendToServer(location: Location) {
        withContext(Dispatchers.IO) {
            Log.d("Location", "Lat=${location.latitude}, Lng=${location.longitude}")

            locationManager.updateIfFar(
                newLat = location.latitude,
                newLng = location.longitude
            )
        }
    }
}

