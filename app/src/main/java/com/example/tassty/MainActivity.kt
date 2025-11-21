package com.example.tassty

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tassty.component.ErrorScreen
import com.example.tassty.navigation.TasstyNavHost
import com.example.tassty.screen.SplashScreen
import com.example.tassty.screen.home.HomeScreen
import com.example.tassty.ui.theme.TasstyTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: AuthViewModel by viewModels()
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

        //WindowCompat.setDecorFitsSystemWindows(window, false)
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
            val isAuthLoaded by viewModel.isAuthLoaded.collectAsState()

            TasstyTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    // ✅ SplashScreen muncul saat belum load
                    AnimatedVisibility(
                        visible = !isAuthLoaded,
                        enter = fadeIn(),
                        exit = fadeOut() + slideOutVertically { -80 }
                    ) {
                        SplashScreen() // sudah termasuk gradient background kamu
                    }

                    // ✅ Main content muncul setelah authLoaded
                    AnimatedVisibility(
                        visible = isAuthLoaded,
                        enter = fadeIn() + slideInVertically { 80 },
                        exit = fadeOut()
                    ) {
                        TransparentStatusBar(true)
                        val navController = rememberNavController()
                        TasstyNavHost(
                            navController = navController,
                            onBackButtonClick = { navController.popBackStack() }
                        )
                    }
                }
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

@Composable
fun TransparentStatusBar(lightIcons: Boolean) {
    val window = LocalView.current.context.findActivity()?.window
    SideEffect {
        window?.statusBarColor = Color.Transparent.toArgb()
        WindowCompat.getInsetsController(window!!, window.decorView)
            .isAppearanceLightStatusBars = lightIcons
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
