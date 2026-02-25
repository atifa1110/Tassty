package com.example.tassty

import android.app.Application
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
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
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
            locationManager.load()
            setupInitialCleanup()
        }

        ProcessLifecycleOwner
            .get()
            .lifecycle
            .addObserver(observer)
    }

    private fun setupInitialCleanup() {
        // supaya dia langsung jalan di background saat app dibuka.
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
