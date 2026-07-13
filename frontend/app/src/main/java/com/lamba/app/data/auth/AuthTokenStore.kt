package com.lamba.app.data.auth

import android.content.Context

class AuthTokenStore(context: Context) {
    private val preferences = context.applicationContext.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    fun getAccessToken(): String? {
        return preferences.getString(KEY_ACCESS_TOKEN, null)?.takeIf { it.isNotBlank() }
    }

    fun saveAccessToken(accessToken: String) {
        preferences.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .apply()
    }

    fun clearAccessToken() {
        preferences.edit()
            .remove(KEY_ACCESS_TOKEN)
            .apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "auth_session"
        const val KEY_ACCESS_TOKEN = "access_token"
    }
}
