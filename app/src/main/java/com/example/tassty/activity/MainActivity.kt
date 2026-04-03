package com.example.tassty.activity

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.core.ui.service.ChatService
import com.example.tassty.navigation.TasstyNavHost
import com.example.tassty.screen.splash.SplashScreen
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.PermissionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var permissionManager: PermissionManager

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startLocationDependentLogic()
        } else {
            showPermissionDeniedMessage()
        }
    }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.d("Permission", "Notification granted: $isGranted")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionManager = PermissionManager(
            activity = this,
            locationLauncher = locationPermissionLauncher,
            notificationLauncher = notificationPermissionLauncher
        )

        permissionManager.checkLocationPermission {
            startLocationDependentLogic()
        }

        permissionManager.checkNotificationPermission()
    }

    private fun startLocationDependentLogic() {
        setContent {
            val viewModel: MainViewModel by  viewModels()
            val snackHostState = remember { SnackbarHostState() }
            val isAuthLoaded by viewModel.isAuthLoaded.collectAsState()
            val authStatus by viewModel.authState.collectAsStateWithLifecycle()

            LaunchedEffect(authStatus.isLoggedIn) {
                Log.d("TASSTY_DEBUG", "MainActivity: Status login berubah jadi -> ${authStatus.isLoggedIn}")
                if (authStatus.isLoggedIn) {
                    Log.d("TASSTY_DEBUG", "MainActivity: Menyalakan ChatService...")
                    triggerChatService(Action.START)
                } else {
                    Log.d("TASSTY_DEBUG", "MainActivity: Mematikan ChatService...")
                    triggerChatService(Action.STOP)
                }
            }

            TasstyTheme(darkTheme = true) {
                LaunchedEffect(isAuthLoaded) {
                    if (isAuthLoaded) {
                        viewModel.snackbarMessage.collect { message ->
                            snackHostState.showSnackbar(message)
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    // SplashScreen muncul saat belum load
                    AnimatedVisibility(
                        visible = !isAuthLoaded,
                        enter = fadeIn(),
                        exit = fadeOut() + slideOutVertically { -80 }
                    ) {
                        SplashScreen()
                    }

                    // Main content muncul setelah authLoaded
                    AnimatedVisibility(
                        visible = isAuthLoaded,
                        enter = fadeIn() + slideInVertically { 80 },
                        exit = fadeOut()
                    ) {
                        TransparentStatusBar(true)

                        Scaffold (
                            snackbarHost = {
                                SnackbarHost(hostState = snackHostState)
                            }
                        ) { innerPadding ->
                            TasstyNavHost(
                                authStatus = authStatus,
                                modifier = Modifier.padding(innerPadding),
                                navController = rememberNavController()
                            )
                        }
                    }
                }
            }
        }
    }

    // Fallback for when permission is denied
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

    /**
     * Helper function untuk mengelola siklus hidup ChatService.
     */
    private fun triggerChatService(action: Action) {
        val intent = Intent(this, ChatService::class.java)
        if (action == Action.START) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        } else {
            stopService(intent)
        }
    }

    private enum class Action { START, STOP }
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


