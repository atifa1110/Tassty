package com.example.core.data.cache

import android.util.Log
import com.example.core.data.source.remote.network.Meta
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class BaseCache<T : Any> @Inject constructor() {

    // key -> (id -> item)
    protected val dataMap = mutableMapOf<String, MutableMap<String, T>>()
    protected val metaMap = mutableMapOf<String, Meta?>()

    protected val mutex = Mutex() // thread-safety

    open suspend fun saveAll(key: String, items: List<T>, getId: (T) -> String) {
        mutex.withLock {
            val categoryMap = dataMap.getOrPut(key) { mutableMapOf() }
            items.forEach { categoryMap[getId(it)] = it }
            Log.d("DianaBaseCache", "Saved ${items.size} items to key=$key, total now=${categoryMap.size}")
        }
    }

    open suspend fun getAll(key: String): List<T> = mutex.withLock {
        dataMap[key]?.values?.toList() ?: emptyList()
    }

    open suspend fun getWithMeta(key: String): Pair<List<T>, Meta?> = mutex.withLock {
        val list = dataMap[key]?.values?.toList() ?: emptyList()
        val meta = metaMap[key]
        list to meta
    }

    open suspend fun saveMeta(key: String, meta: Meta?) {
        mutex.withLock {
            if (meta != null) metaMap[key] = meta
        }
    }

    open suspend fun getMeta(key: String): Meta? = mutex.withLock {
        metaMap[key]
    }

//    open suspend fun clear(key: String) {
//        mutex.withLock {
//            dataMap.remove(key)
//            metaMap.remove(key)
//        }
//    }
//
//    open suspend fun clearAll() {
//        mutex.withLock {
//            dataMap.clear()
//            metaMap.clear()
//        }
//    }
}
