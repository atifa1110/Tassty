package com.example.tassty

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.tassty.component.ErrorScreen
import com.example.tassty.screen.home.HomeScreen
import com.example.tassty.ui.theme.TasstyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // 1. Define the Permission Launcher and its callback
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted: Call the function that loads the HomeScreen
            startLocationDependentLogic()
        } else {
            // Permission denied: Handle the failure gracefully (e.g., show a default UI)
            Log.e("LocationPermission", "Location permission denied by user.")
            // You might show a different screen or a message here.
            showPermissionDeniedMessage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 2. Start the permission check as soon as the Activity is created
        checkAndRequestLocationPermission()
    }

    // Function to check current permission status and request if needed
    private fun checkAndRequestLocationPermission() {
        when {
            // Permission is ALREADY granted (the ideal case)
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("LocationPermission", "Location permission already granted.")
                startLocationDependentLogic()
            }

            // Should show an educational UI before requesting (optional but good practice)
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Log.w("LocationPermission", "Showing rationale before requesting permission.")
                // You can show a dialog here before launching the request
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }

            // Request the permission for the first time
            else -> {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    // 3. The logic that runs ONLY after permission is granted or already existed
    private fun startLocationDependentLogic() {
        setContent {
            TasstyTheme {
                // Your main entry point for the app
                HomeScreen()
            }
        }
    }

    // 4. Fallback for when permission is denied
    private fun showPermissionDeniedMessage() {
        setContent {
            TasstyTheme {
                // Replace this with a Composable that clearly tells the user
                // that location is required or loads data without location.
                //ErrorScreen(message = "Location is required to find recommended restaurants.")
                Log.e("MainActivity", "Location is required to find recommended restaurants.")
            }
        }
    }
}