package com.example.tassty

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.example.core.data.observer.AppLifecycleObserver
import com.example.core.data.source.local.cache.LocationManager
import com.example.core.data.worker.DatabaseCleanupWorker
import com.google.firebase.messaging.FirebaseMessaging
import com.jakewharton.threetenabp.AndroidThreeTen
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp
import io.getstream.android.push.firebase.FirebaseMessagingDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(), Configuration.Provider, ImageLoaderFactory {

    @Inject lateinit var observer: AppLifecycleObserver
    @Inject lateinit var locationManager: LocationManager
    @Inject lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        CoroutineScope(Dispatchers.IO).launch {
            setupInitialCleanup()
        }

        ProcessLifecycleOwner
            .get()
            .lifecycle
            .addObserver(observer)

        PaymentConfiguration.init(
            applicationContext,
            BuildConfig.STRIPE_PUBLISH_KEY
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chatChannel = NotificationChannel(
                "chat_channel",
                "Chat Driver",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi pesan masuk"
            }

            val orderChannel = NotificationChannel(
                "order_channel",
                "Update Pesanan",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi status pesanan"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(chatChannel)
            manager.createNotificationChannel(orderChannel)
        }
    }

    private fun setupInitialCleanup() {
        val cleanupRequest = OneTimeWorkRequestBuilder<DatabaseCleanupWorker>()
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "test_cleanup",
            ExistingWorkPolicy.REPLACE,
            cleanupRequest
        )
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.15)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(this.cacheDir.resolve("image_cache"))
                    .maxSizeBytes(250L * 1024 * 1024)
                    .build()
            }
            .crossfade(true)
            .respectCacheHeaders(false)
            .build()
    }
}
