package com.example.tassty.activity

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.core.ui.service.ChatService
import com.example.tassty.navigation.MainDestination
import com.example.tassty.navigation.MessageDestination
import com.example.tassty.navigation.TasstyNavHost
import com.example.tassty.screen.splash.SplashScreen
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.PermissionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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
        enableEdgeToEdge()
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
    private fun startLocationDependentLogic() {
        setContent {
            val viewModel: MainViewModel by  viewModels()
            val snackHostState = remember { SnackbarHostState() }
            val isAuthLoaded by viewModel.isAuthLoaded.collectAsState()
            val authStatus by viewModel.authState.collectAsStateWithLifecycle()
            val chatReady by viewModel.isChatConnected.collectAsState()

            val navController = rememberNavController()

            LaunchedEffect(authStatus.isLoggedIn, authStatus.streamToken) {
                if (authStatus.isLoggedIn && authStatus.streamToken != null) {
                    Log.d("NAV_DEBUG", "Data Auth dapet! Nyalain Chat Service tanpa nunggu Splash...")
                    triggerChatService(Action.START)
                } else if (!authStatus.isLoggedIn && isAuthLoaded) {
                    triggerChatService(Action.STOP)
                }
            }

            Log.d("NAV_DEBUG", "ChatReady $chatReady")

            val target = intent.getStringExtra("target_screen")
            val cid = intent.getStringExtra("channel_id")

            LaunchedEffect(isAuthLoaded, chatReady, target, cid) {
                // 1. Pagar pertama: Tunggu sampai NavHost siap
                if (!isAuthLoaded) return@LaunchedEffect

                // 2. Pagar kedua: Cek apakah ini memang niatnya mau navigasi ke chat?
                // Jika target bukan 'message' atau cid-nya bolong, JANGAN JALAN.
                if (target != MessageDestination.route || cid.isNullOrBlank()) {
                    return@LaunchedEffect
                }

                // 3. Pagar ketiga: Tunggu Chat Service konek biar gak crash pas loading data
                if (!chatReady) {
                    Log.d("NAV_DEBUG", "Target ada, tapi nunggu Chat Service ready...")
                    return@LaunchedEffect
                }

                try {
                    Log.d("NAV_DEBUG", "Semua syarat terpenuhi. Gas navigasi ke: $cid")

                    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.cancel(cid.hashCode())

                    // Pondasi Home
                    navController.navigate(MainDestination.route) {
                        popUpTo(0) { inclusive = true }
                    }

                    // Tumpuk Chat (cid di sini dijamin gak null/blank karena pagar di atas)
                    navController.navigate(MessageDestination.createRoute(cid))

                    // Bersihkan biar gak ke-trigger lagi pas recompose/rotasi
                    intent.removeExtra("target_screen")
                    intent.removeExtra("channel_id")

                } catch (e: Exception) {
                    Log.e("NAV_ERROR", "Gagal navigasi: ${e.message}")
                }
            }

//            val navBackStackEntry by navController.currentBackStackEntryAsState()
//            val currentRoute = navBackStackEntry?.destination?.route
//
//            // Contoh penggunaan:
//            LaunchedEffect(currentRoute) {
//                Log.d("NAV_DEBUG", "Luna lagi ada di halaman: $currentRoute")
//            }

            TasstyTheme(authStatus.isDarkMode) {
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
                        TasstyNavHost(
                            authStatus = authStatus,
                            modifier = Modifier.fillMaxSize(),
                            navController = navController
                        )
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

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}


