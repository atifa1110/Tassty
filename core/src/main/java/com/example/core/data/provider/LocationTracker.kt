package com.example.core.data.provider

import android.Manifest
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * LocationTracker is responsible for listening to real-time GPS updates.
 * It acts as the "Sensor" that feeds data into the LocationManager.
 */
@Singleton
class LocationTracker @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
) {

    private val _locationFlow = MutableSharedFlow<Location>(replay = 1)
    val locationFlow = _locationFlow.asSharedFlow()

    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_BALANCED_POWER_ACCURACY, // Balanced for battery
        TimeUnit.MINUTES.toMillis(5) // Check every 5 minutes
    ).apply {
        setMinUpdateDistanceMeters(100f) // Only trigger if moved > 100m
    }.build()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { _locationFlow.tryEmit(it) }
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun startTracking() {
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (unlikely: SecurityException) {
            Log.e("LocationTracker", "Lost location permission. Could not request updates. $unlikely")
        }
    }

    fun stopTracking() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend fun getCurrentLocation(): Location? {
        return try {
            val cts = CancellationTokenSource()
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cts.token
            ).await()

            location?.let {
                _locationFlow.tryEmit(it)
            }
            location
        } catch (e: Exception) {
            Log.e("LocationTracker", "Gagal ambil lokasi instan: ${e.message}")
            null
        }
    }
}