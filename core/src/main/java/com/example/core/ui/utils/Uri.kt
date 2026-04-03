package com.example.core.ui.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun Uri.toFile(context: Context): File? {
    val contentResolver = context.contentResolver
    val tempFile = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")

    return try {
        contentResolver.openInputStream(this)?.use { inputStream ->
            FileOutputStream(tempFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}