package com.example.core.data.source.local.datasource

import androidx.room.Transaction
import com.example.core.data.source.local.database.dao.CleanupDao
import javax.inject.Inject

class CleanDataSource @Inject constructor(
    private val cleanupDao: CleanupDao
) {
    @Transaction
    suspend fun cleanupOrphanedData() {
        cleanupDao.deleteOrphanedMenus()
        cleanupDao.deleteOrphanedRestaurants()
    }
}