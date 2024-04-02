package edu.temple.convoy3.utility

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {
    private const val PREF_NAME = "YourPrefName"
    private const val SESSION_KEY = "session_key"
    private const val USERNAME_KEY = "username"
    private const val CONVOY_SESSION_KEY = "c_session_key"
    private const val FCM_KEY = "keyyyy"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    fun saveFCMKey(context: Context, sessionKey: String) {
        getSharedPreferences(context).edit().putString(FCM_KEY, sessionKey).apply()
    }

    fun getFCMKey(context: Context): String? {
        return getSharedPreferences(context).getString(FCM_KEY, null)
    }

    fun saveSessionKey(context: Context, sessionKey: String) {
        getSharedPreferences(context).edit().putString(SESSION_KEY, sessionKey).apply()
    }

    fun getSessionKey(context: Context): String? {
        return getSharedPreferences(context).getString(SESSION_KEY, null)
    }

    fun saveUsername(context: Context, username: String) {
        getSharedPreferences(context).edit().putString(USERNAME_KEY, username).apply()
    }

    fun getUsername(context: Context): String? {
        return getSharedPreferences(context).getString(USERNAME_KEY, null)
    }

    fun getConvoySessionKey(context: Context): String? {
        return getSharedPreferences(context).getString(CONVOY_SESSION_KEY, null)
    }

    fun saveConvoySessionKey(context: Context, convoySessionKey: String) {
        getSharedPreferences(context).edit().putString(CONVOY_SESSION_KEY, convoySessionKey).apply()
    }
}
