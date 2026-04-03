package com.example.core.data.source.local.database.model

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDiskIOException
import android.database.sqlite.SQLiteFullException

/**
 * Fungsi ini akan memetakan error SQLite menjadi pesan yang dimengerti user
 * dan langsung melempar Exception baru.
 */
fun Throwable.throwUserFriendlyDbError(): Nothing {
    val message = when (this) {
        is SQLiteConstraintException ->
            "Gagal menyimpan: Data sudah ada atau melanggar aturan."
        is SQLiteFullException ->
            "Memori HP penuh, silakan hapus beberapa file untuk melanjutkan."
        is SQLiteDiskIOException ->
            "Gagal mengakses penyimpanan HP. Coba lagi."
        else -> this.message ?: "Terjadi kesalahan pada database."
    }
    throw Exception(message)
}