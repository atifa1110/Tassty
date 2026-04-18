package com.example.core.data.source.local.database.converter

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

// TypeConverter.kt
class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return Json.Default.decodeFromString(value)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Json.Default.encodeToString(list)
    }
}