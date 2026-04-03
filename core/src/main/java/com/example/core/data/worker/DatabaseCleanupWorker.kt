package com.example.core.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.core.domain.repository.MenuRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DatabaseCleanupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: MenuRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            Log.i("DatabaseCleanupWorker", "Sapu-sapu database dimulai...")

            // Memanggil UseCase atau Repository yang tadi kita buat
            repository.runDatabaseMaintenance()

            Log.i("DatabaseCleanupWorker", "Sapu-sapu selesai! Database kinclong.")
            Result.success()
        } catch (e: Exception) {
            Log.e("DatabaseCleanupWorker", "Gagal bersih-bersih: ${e.message}")
            // Jika gagal, sistem akan mencoba lagi nanti sesuai kebijakan
            Result.retry()
        }
    }
}