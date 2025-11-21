package com.example.core.data.repository

import android.util.Log
import com.example.core.data.source.local.database.entity.CollectionMenuCrossRef
import com.example.core.data.source.local.database.entity.CollectionWithMenus
import com.example.core.data.source.local.database.entity.MenuCollectionEntity
import com.example.core.data.source.local.datasource.MenuCacheLocalDataSource
import com.example.core.data.source.local.datasource.MenuDatabaseLocalDataSource
import com.example.core.data.source.remote.network.LOCAL_SUCCESS_META
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.model.CollectionListItem
import com.example.core.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(
    private val dataSource: MenuDatabaseLocalDataSource,
    private val menuCacheLocalDataSource: MenuCacheLocalDataSource
): CollectionRepository {

    override suspend fun getCollectionIdsForMenu(menuId: String): List<Int> {
        return dataSource.getCollectionIdsForMenu(menuId)
    }

    override fun getCollectionListForView(): Flow<List<CollectionListItem>> {
        return dataSource.getCollectionListForView()
    }

    override suspend fun createNewCollectionOnly(
        collectionName: String): ResultWrapper<String> {
        return try {
            val newCollectionEntity = MenuCollectionEntity(name = collectionName)
            dataSource.insertCollection(newCollectionEntity)

            ResultWrapper.Success(
                data = "Success",
                meta = LOCAL_SUCCESS_META
            )

        } catch (e: Exception) {
            Log.e("Repository", "Failed to create collection: $collectionName", e)

            ResultWrapper.Error(
                meta = Meta(
                    code = 500,
                    status = "",
                    message = "Gagal membuat koleksi: " + (e.message ?: "Kesalahan tak terduga.")
                )
            )
        }
    }

    override suspend fun saveMenuToCollections(
        menuId: String,
        selectedCollectionIds: List<Int>
    ): ResultWrapper<String> {

        val menuDetail = menuCacheLocalDataSource.getFullDetailFromCache(menuId)

        // Pastikan menuDetail tidak null
        return try {
            dataSource.deleteMenuFromAllCollections(menuId)

            // 2. CHECK: Jika list yang dipilih kosong, proses selesai (berarti user ingin menghapus dari semua koleksi).
            if (selectedCollectionIds.isEmpty()) {
                return ResultWrapper.Success("Menu berhasil dihapus dari semua koleksi.", meta = LOCAL_SUCCESS_META)
            }

            val crossRefs = selectedCollectionIds.map { collectionId ->
                CollectionMenuCrossRef(
                    collectionId = collectionId,
                    serverId = menuDetail.serverId,
                    name = menuDetail.name,
                    imageUrl = menuDetail.imageUrl,
                    price = menuDetail.price,
                    description = menuDetail.description,
                    restaurantName = menuDetail.restaurantName,
                    restaurantLocation = menuDetail.restaurantLocation
                )
            }
            dataSource.insertCollectionWithMenu(crossRefs)
            ResultWrapper.Success("Menu Berhasil Disimpan", meta = LOCAL_SUCCESS_META)

        } catch (e: Exception) {
            ResultWrapper.Error(
                meta = Meta(
                    code = 500,
                    status = "",
                    message = e.message ?: "Gagal menyimpan menu ke koleksi yang sudah ada."
                )
            )
        }
    }

    override fun getCollectionDetail(collectionId: Int): Flow<CollectionWithMenus> {
        return dataSource.getCollectionDetail(collectionId)
    }

    override suspend fun checkMenuWishlistStatus(menuId: String): Boolean {
        return dataSource.isMenuInAnyCollection(menuId)
    }
}