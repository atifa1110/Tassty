package com.example.tassty.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

class PermissionManager(
    private val activity: Activity,
    private val locationLauncher: ActivityResultLauncher<String>,
    private val notificationLauncher: ActivityResultLauncher<String>? = null
) {
    fun checkLocationPermission(onGranted: () -> Unit) {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        when {
            ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED -> {
                onGranted()
            }
            else -> {
                locationLauncher.launch(permission)
            }
        }
    }

    fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                notificationLauncher?.launch(permission)
            }
        }
    }
}