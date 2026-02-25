package com.jobsniper.ai.util

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenStore(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "jobsniper_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveGmailToken(token: String) {
        prefs.edit().putString(KEY_GMAIL, token).apply()
    }

    fun saveOpenAiToken(token: String) {
        prefs.edit().putString(KEY_OPENAI, token).apply()
    }

    fun gmailToken(): String = prefs.getString(KEY_GMAIL, "") ?: ""
    fun openAiToken(): String = prefs.getString(KEY_OPENAI, "") ?: ""

    companion object {
        private const val KEY_GMAIL = "gmail_access_token"
        private const val KEY_OPENAI = "openai_api_key"
    }
}
