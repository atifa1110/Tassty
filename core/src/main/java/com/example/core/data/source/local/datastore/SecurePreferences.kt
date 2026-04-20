package com.example.core.data.source.local.datastore

import android.content.Context
import android.util.Base64
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecurePreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // 1. Inisialisasi MasterKey (KeyStore Android)
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // 2. Setup Tink KeysetManager (Ini yang bikin enkripsi String jadi satu pintu)
    private val aead: Aead = AndroidKeysetManager.Builder()
        .withSharedPref(context, "tink_keyset", "tink_prefs")
        .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
        .withMasterKeyUri("android-keystore://${MasterKey.DEFAULT_MASTER_KEY_ALIAS}")
        .build()
        .keysetHandle
        .getPrimitive(Aead::class.java)

    fun encrypt(value: String): String {
        return try {
            val ciphertext = aead.encrypt(value.toByteArray(), null)
            Base64.encodeToString(ciphertext, Base64.DEFAULT)
        } catch (e: Exception) {
            value
        }
    }

    fun decrypt(value: String): String {
        return try {
            val decoded = Base64.decode(value, Base64.DEFAULT)
            String(aead.decrypt(decoded, null))
        } catch (e: Exception) {
            ""
        }
    }
}