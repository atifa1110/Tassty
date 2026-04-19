package com.example.core.data.observer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.core.data.provider.LocationTracker
import com.example.core.data.provider.LocationManager
import com.example.core.domain.repository.LocationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Observes the entire Application Lifecycle to manage global tasks.
 * This ensures GPS tracking only runs when the app is active in the foreground.
 */
@Singleton
class AppLifecycleObserver @Inject constructor(
    @ApplicationContext private val context: Context,
    private val locationTracker: LocationTracker,
    private val locationRepository: LocationRepository
) : DefaultLifecycleObserver {

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        // Start GPS tracking to get fresh updates while the app is open
        if (hasLocationPermission()) {
            locationTracker.startTracking()
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        // Stop GPS when app is in background to save user's battery
        locationTracker.stopTracking()
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}